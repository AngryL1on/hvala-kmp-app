package tech.appard.hvala.shared.core.i18n

data class MessagesStrings(
    val searchPlaceholder: String,
    val messagePlaceholder: String,
    val attachment: String,
    val send: String,
    val noMessages: String,
    val youPrefix: String,
    val chatOpener: (listingTitle: String) -> String,
)

internal fun AppLanguage.messagesStrings(): MessagesStrings = when (this) {
    AppLanguage.RU -> MessagesStrings(
        searchPlaceholder = "Поиск по чатам",
        messagePlaceholder = "Сообщение",
        attachment = "Вложение",
        send = "Отправить",
        noMessages = "Нет сообщений",
        youPrefix = "Вы: ",
        chatOpener = { title -> "Здравствуйте! Меня интересует «$title»." },
    )
    AppLanguage.EN -> MessagesStrings(
        searchPlaceholder = "Search chats",
        messagePlaceholder = "Message",
        attachment = "Attachment",
        send = "Send",
        noMessages = "No messages",
        youPrefix = "You: ",
        chatOpener = { title -> "Hello! I'm interested in \"$title\"." },
    )
    AppLanguage.SR -> MessagesStrings(
        searchPlaceholder = "Pretraga ćaskanja",
        messagePlaceholder = "Poruka",
        attachment = "Prilog",
        send = "Pošalji",
        noMessages = "Nema poruka",
        youPrefix = "Vi: ",
        chatOpener = { title -> "Zdravo! Zanima me \"$title\"." },
    )
    AppLanguage.CNR -> MessagesStrings(
        searchPlaceholder = "Pretraga ćaskanja",
        messagePlaceholder = "Poruka",
        attachment = "Prilog",
        send = "Pošalji",
        noMessages = "Nema poruka",
        youPrefix = "Vi: ",
        chatOpener = { title -> "Zdravo! Zanima me \"$title\"." },
    )
    AppLanguage.HR -> MessagesStrings(
        searchPlaceholder = "Pretraži razgovore",
        messagePlaceholder = "Poruka",
        attachment = "Privitak",
        send = "Pošalji",
        noMessages = "Nema poruka",
        youPrefix = "Vi: ",
        chatOpener = { title -> "Bok! Zanima me \"$title\"." },
    )
    AppLanguage.BS -> MessagesStrings(
        searchPlaceholder = "Pretraga razgovora",
        messagePlaceholder = "Poruka",
        attachment = "Prilog",
        send = "Pošalji",
        noMessages = "Nema poruka",
        youPrefix = "Vi: ",
        chatOpener = { title -> "Zdravo! Zanima me \"$title\"." },
    )
}

fun formatLastMessagePreview(
    messages: List<PreviewMessage>,
    strings: MessagesStrings,
): String {
    val lastMessage = messages.lastOrNull { !it.isDateDivider } ?: return strings.noMessages
    val preview = if (lastMessage.text.length > 72) {
        lastMessage.text.take(69) + "..."
    } else {
        lastMessage.text
    }
    return if (lastMessage.isOutgoing) strings.youPrefix + preview else preview
}

data class PreviewMessage(
    val text: String,
    val isOutgoing: Boolean,
    val isDateDivider: Boolean = false,
)
