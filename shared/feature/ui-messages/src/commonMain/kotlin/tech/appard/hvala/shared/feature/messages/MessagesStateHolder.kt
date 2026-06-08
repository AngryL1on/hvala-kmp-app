package tech.appard.hvala.shared.feature.messages

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import tech.appard.hvala.shared.core.contracts.model.ChatMessage
import tech.appard.hvala.shared.core.contracts.model.ChatThread

data class MessagesUiState(
    val searchQuery: String = "",
    val threads: List<ChatThread> = emptyList(),
    val isLoading: Boolean = false,
)

data class ChatUiState(
    val thread: ChatThread? = null,
    val messages: List<ChatMessage> = emptyList(),
    val inputText: String = "",
    val isLoading: Boolean = false,
)

private data class Conversation(
    val thread: ChatThread,
    val messages: MutableList<ChatMessage>,
)

class MessagesStateHolder {
    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.Default)

    private val conversations: MutableMap<String, Conversation> = mockConversations()

    private val _messagesState = MutableStateFlow(MessagesUiState())
    val messagesState: StateFlow<MessagesUiState> = _messagesState.asStateFlow()

    private val _chatState = MutableStateFlow(ChatUiState())
    val chatState: StateFlow<ChatUiState> = _chatState.asStateFlow()

    private var threadsLoaded = false

    fun loadThreads() {
        if (threadsLoaded || _messagesState.value.isLoading) return
        scope.launch {
            _messagesState.update { it.copy(isLoading = true) }
            _messagesState.update {
                it.copy(
                    isLoading = false,
                    threads = conversations.values.map { conversation ->
                        conversation.thread.withPreview(conversation.messages)
                    },
                )
            }
            threadsLoaded = true
        }
    }

    fun onSearchQueryChange(query: String) {
        _messagesState.update { it.copy(searchQuery = query) }
    }

    fun loadChat(threadId: String) {
        scope.launch {
            _chatState.update { it.copy(isLoading = true) }
            val conversation = conversations[threadId]
            _chatState.update {
                it.copy(
                    isLoading = false,
                    thread = conversation?.thread?.withPreview(conversation.messages),
                    messages = conversation?.messages?.toList().orEmpty(),
                    inputText = "",
                )
            }
        }
    }

    fun onChatInputChange(text: String) {
        _chatState.update { it.copy(inputText = text) }
    }

    fun sendMessage() {
        val snapshot = _chatState.value
        val threadId = snapshot.thread?.id ?: return
        val text = snapshot.inputText.trim()
        if (text.isEmpty()) return

        val conversation = conversations[threadId] ?: return
        val newMessage = ChatMessage(
            id = "${threadId}-msg-${conversation.messages.size}",
            text = text,
            isOutgoing = true,
        )
        conversation.messages.add(newMessage)

        val updatedThread = conversation.thread.withPreview(conversation.messages)
        conversations[threadId] = conversation.copy(thread = updatedThread)

        _chatState.update { current ->
            current.copy(
                inputText = "",
                thread = updatedThread,
                messages = conversation.messages.toList(),
            )
        }
        _messagesState.update { current ->
            current.copy(
                threads = conversations.values.map { item ->
                    item.thread.withPreview(item.messages)
                },
            )
        }
    }

    private fun ChatThread.withPreview(messages: List<ChatMessage>): ChatThread =
        copy(lastMessagePreview = formatLastMessagePreview(messages))

    private fun mockConversations(): MutableMap<String, Conversation> = linkedMapOf(
        "niko" to Conversation(
            thread = ChatThread(
                id = "niko",
                participantName = "Нико Б.",
                lastMessagePreview = "",
                avatarColorArgb = 0xFFFFB74D,
                listingTitle = "Худи Number Nine",
                listingPriceUsd = 150,
                listingPriceRub = 12_570,
            ),
            messages = mutableListOf(
                divider("niko", "Вчера, 19:12"),
                message("niko", 0, "Здравствуйте! Худи Number Nine ещё в продаже?", outgoing = true),
                message("niko", 1, "Привет! Да, размер L, состояние отличное."),
                message("niko", 2, "Можно посмотреть сегодня вечером в Химках?", outgoing = true),
                message("niko", 3, "Да, после 18:00 у метро. Напишите за час."),
                divider("niko", "Сегодня, 8:43"),
                message("niko", 4, "Буду к 18:30. С собой наличные подойдут?", outgoing = true),
                message("niko", 5, "Да, без проблем. Если что — напишите."),
            ),
        ),
        "lincoln" to Conversation(
            thread = ChatThread(
                id = "lincoln",
                participantName = "Авраам Линкольн",
                lastMessagePreview = "",
                avatarColorArgb = 0xFF03989F,
                listingTitle = "Пальто зимнее",
                listingPriceUsd = 220,
                listingPriceRub = 18_400,
            ),
            messages = mutableListOf(
                divider("lincoln", "Вчера, 14:05"),
                message("lincoln", 0, "Добрый день! Пальто ещё актуально?"),
                message("lincoln", 1, "Здравствуйте! Да, актуально.", outgoing = true),
                message("lincoln", 2, "Можем встретиться завтра у ТЦ, удобно?"),
            ),
        ),
        "yeltsin" to Conversation(
            thread = ChatThread(
                id = "yeltsin",
                participantName = "Борис Ельцин",
                lastMessagePreview = "",
                avatarColorArgb = 0xFF5C9FD6,
                listingTitle = "Кроссовки Nike Air",
                listingPriceUsd = 90,
                listingPriceRub = 7_540,
            ),
            messages = mutableListOf(
                divider("yeltsin", "Понедельник, 11:20"),
                message("yeltsin", 0, "За 8 000 отдадите?", outgoing = true),
                message("yeltsin", 1, "Минимум 8 500, они почти новые."),
                message("yeltsin", 2, "Ладно, договорились. Где забрать?", outgoing = true),
                message("yeltsin", 3, "Москва, м. Сокол. Напишу точный адрес."),
            ),
        ),
        "pushkin" to Conversation(
            thread = ChatThread(
                id = "pushkin",
                participantName = "Александр Пушкин",
                lastMessagePreview = "",
                avatarColorArgb = 0xFFFFBF34,
                listingTitle = "Книжная полка",
                listingPriceUsd = 45,
                listingPriceRub = 3_770,
            ),
            messages = mutableListOf(
                divider("pushkin", "Суббота, 16:40"),
                message("pushkin", 0, "Забрал сегодня, всё супер!", outgoing = true),
                message("pushkin", 1, "Спасибо! Рад, что понравилось."),
            ),
        ),
        "gagarin" to Conversation(
            thread = ChatThread(
                id = "gagarin",
                participantName = "Юрий Гагарин",
                lastMessagePreview = "",
                avatarColorArgb = 0xFFFF8A65,
                listingTitle = "Фотоаппарат Canon",
                listingPriceUsd = 310,
                listingPriceRub = 25_940,
            ),
            messages = mutableListOf(
                divider("gagarin", "Сегодня, 10:15"),
                message("gagarin", 0, "Скинул фото комплекта в чат.", outgoing = true),
                message("gagarin", 1, "Вижу, спасибо! Объектив без царапин?"),
                message("gagarin", 2, "Да, всё чисто. Могу прислать видео.", outgoing = true),
            ),
        ),
        "tereshkova" to Conversation(
            thread = ChatThread(
                id = "tereshkova",
                participantName = "Валентина Терешкова",
                lastMessagePreview = "",
                avatarColorArgb = 0xFFE57373,
                listingTitle = "Детская коляска",
                listingPriceUsd = 180,
                listingPriceRub = 15_080,
            ),
            messages = mutableListOf(
                divider("tereshkova", "Сегодня, 9:02"),
                message("tereshkova", 0, "Когда сможете отправить СДЭКом?"),
                message("tereshkova", 1, "Сегодня до 17:00 оформлю отправку.", outgoing = true),
                message("tereshkova", 2, "Отлично, жду трек-номер."),
            ),
        ),
    )

    private fun divider(threadId: String, label: String): ChatMessage =
        ChatMessage(
            id = "$threadId-divider-$label",
            text = label,
            isOutgoing = false,
            isDateDivider = true,
        )

    private fun message(
        threadId: String,
        index: Int,
        text: String,
        outgoing: Boolean = false,
    ): ChatMessage = ChatMessage(
        id = "$threadId-msg-$index",
        text = text,
        isOutgoing = outgoing,
    )
}

internal fun formatLastMessagePreview(messages: List<ChatMessage>): String {
    val lastMessage = messages.lastOrNull { !it.isDateDivider } ?: return "Нет сообщений"
    val preview = if (lastMessage.text.length > 72) {
        lastMessage.text.take(69) + "..."
    } else {
        lastMessage.text
    }
    return if (lastMessage.isOutgoing) "Вы: $preview" else preview
}
