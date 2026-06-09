package tech.appard.hvala.shared.core.i18n

import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.descriptors.buildClassSerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.JsonDecoder
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonEncoder
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.contentOrNull
import kotlinx.serialization.json.jsonPrimitive
import kotlinx.serialization.json.put

/**
 * API/JSON field that can be either a plain string or a per-locale map.
 *
 * Examples accepted by [LocalizedStringSerializer]:
 * - `"Hello"`
 * - `{ "en": "Hello", "ru": "Привет" }`
 */
@Serializable(with = LocalizedStringSerializer::class)
data class LocalizedString(
    internal val translations: Map<String, String>,
) {
    fun resolve(language: AppLanguage): String {
        translations[language.code]?.let { return it }
        translations[AppLanguage.default.code]?.let { return it }
        translations[AppLanguage.EN.code]?.let { return it }
        return translations.values.firstOrNull().orEmpty()
    }

    companion object {
        fun single(value: String): LocalizedString = LocalizedString(mapOf(AppLanguage.default.code to value))

        fun of(vararg pairs: Pair<AppLanguage, String>): LocalizedString =
            LocalizedString(pairs.associate { (language, value) -> language.code to value })
    }
}

object LocalizedStringSerializer : KSerializer<LocalizedString> {
    override val descriptor: SerialDescriptor =
        buildClassSerialDescriptor("LocalizedString")

    override fun serialize(encoder: Encoder, value: LocalizedString) {
        val jsonEncoder = encoder as? JsonEncoder ?: error("LocalizedString supports JSON only")
        if (value.translations.size == 1) {
            jsonEncoder.encodeJsonElement(JsonPrimitive(value.translations.values.first()))
            return
        }
        jsonEncoder.encodeJsonElement(
            buildJsonObject {
                value.translations.forEach { (code, text) -> put(code, text) }
            },
        )
    }

    override fun deserialize(decoder: Decoder): LocalizedString {
        val jsonDecoder = decoder as? JsonDecoder ?: error("LocalizedString supports JSON only")
        return when (val element = jsonDecoder.decodeJsonElement()) {
            is JsonPrimitive -> LocalizedString.single(element.content)
            is JsonObject -> LocalizedString(
                element.mapValues { (_, value) ->
                    value.jsonPrimitive.contentOrNull.orEmpty()
                },
            )
            else -> LocalizedString(emptyMap())
        }
    }
}

fun String.toLocalizedString(): LocalizedString = LocalizedString.single(this)

fun LocalizedString?.resolveOrFallback(
    language: AppLanguage,
    fallback: String,
): String = this?.resolve(language)?.takeIf { it.isNotBlank() } ?: fallback
