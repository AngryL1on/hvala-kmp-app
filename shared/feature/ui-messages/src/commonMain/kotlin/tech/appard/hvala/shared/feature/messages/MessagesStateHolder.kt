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
import tech.appard.hvala.shared.core.contracts.model.PickedMedia
import tech.appard.hvala.shared.core.contracts.model.ListingMockCatalog
import tech.appard.hvala.shared.core.contracts.model.SellerMockCatalog

data class MessagesUiState(
    val searchQuery: String = "",
    val threads: List<ChatThread> = emptyList(),
    val isLoading: Boolean = false,
)

data class ChatUiState(
    val thread: ChatThread? = null,
    val messages: List<ChatMessage> = emptyList(),
    val inputText: String = "",
    val pendingAttachments: List<PickedMedia> = emptyList(),
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
                    pendingAttachments = emptyList(),
                )
            }
        }
    }

    fun onChatInputChange(text: String) {
        _chatState.update { it.copy(inputText = text) }
    }

    fun openChatForListing(listingId: String): String? {
        val listing = ListingMockCatalog.listingById(listingId) ?: return null
        val threadId = listingChatThreadId(listingId)

        if (conversations[threadId] == null) {
            conversations[threadId] = Conversation(
                thread = ChatThread(
                    id = threadId,
                    participantName = listing.sellerName,
                    lastMessagePreview = "",
                    avatarColorArgb = SellerMockCatalog.sellerById(listing.sellerId)?.avatarColorArgb
                        ?: avatarColorFor(listing.sellerName),
                    listingId = listing.id,
                    sellerId = listing.sellerId,
                    listingTitle = listing.title,
                    listingPriceUsd = listing.priceUsd,
                    listingPriceRub = listing.priceRub,
                ),
                messages = mutableListOf(
                    divider(threadId, "Today"),
                    message(
                        threadId = threadId,
                        index = 0,
                        text = "Hello! I'm interested in \"${listing.title}\".",
                        outgoing = true,
                    ),
                ),
            )
            refreshThreads()
        }

        return threadId
    }

    fun onAttachmentsPicked(attachments: List<PickedMedia>) {
        if (attachments.isEmpty()) return
        _chatState.update { current ->
            current.copy(
                pendingAttachments = current.pendingAttachments + attachments,
            )
        }
    }

    fun sendMessage() {
        val snapshot = _chatState.value
        val threadId = snapshot.thread?.id ?: return
        val text = snapshot.inputText.trim()
        val attachments = snapshot.pendingAttachments
        if (text.isEmpty() && attachments.isEmpty()) return

        val messageText = buildString {
            if (text.isNotEmpty()) append(text)
            if (attachments.isNotEmpty()) {
                if (isNotEmpty()) append('\n')
                append(
                    attachments.joinToString(separator = "\n") { attachment ->
                        "📎 ${attachment.name}"
                    },
                )
            }
        }

        val conversation = conversations[threadId] ?: return
        val newMessage = ChatMessage(
            id = "${threadId}-msg-${conversation.messages.size}",
            text = messageText,
            isOutgoing = true,
        )
        conversation.messages.add(newMessage)

        val updatedThread = conversation.thread.withPreview(conversation.messages)
        conversations[threadId] = conversation.copy(thread = updatedThread)

        _chatState.update { current ->
            current.copy(
                inputText = "",
                pendingAttachments = emptyList(),
                thread = updatedThread,
                messages = conversation.messages.toList(),
            )
        }
        refreshThreads()
    }

    private fun refreshThreads() {
        _messagesState.update { current ->
            current.copy(
                threads = conversations.values.map { item ->
                    item.thread.withPreview(item.messages)
                },
            )
        }
        threadsLoaded = true
    }

    private fun listingChatThreadId(listingId: String): String = "listing-$listingId"

    private fun avatarColorFor(key: String): Long {
        val colors = listOf(
            0xFFFFB74DL,
            0xFF03989FL,
            0xFF5C9FD6L,
            0xFFFFBF34L,
            0xFFFF8A65L,
            0xFFE57373L,
        )
        val index = key.hashCode().mod(colors.size).let { if (it < 0) it + colors.size else it }
        return colors[index]
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
                listingId = "favorite-0",
                listingTitle = "Number Nine Hoodie",
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
                listingId = "favorite-2",
                listingTitle = "Burberry Coat",
                listingPriceUsd = 280,
                listingPriceRub = 23_450,
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
                listingId = "favorite-1",
                listingTitle = "Nike Air Max Sneakers",
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
                listingId = "favorite-9",
                listingTitle = "Corner Sofa",
                listingPriceUsd = 175,
                listingPriceRub = 14_650,
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
                listingId = "favorite-6",
                listingTitle = "AirPods Pro",
                listingPriceUsd = 180,
                listingPriceRub = 15_080,
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
                listingId = "favorite-7",
                listingTitle = "Tiffany Ring",
                listingPriceUsd = 1_200,
                listingPriceRub = 100_500,
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
