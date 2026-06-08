package tech.appard.hvala.shared.feature.messages

import tech.appard.hvala.shared.core.ui.model.PickedMedia
import tech.appard.hvala.shared.feature.messages.domain.GetChatMessagesUseCase
import tech.appard.hvala.shared.feature.messages.domain.GetChatThreadUseCase
import tech.appard.hvala.shared.feature.messages.domain.GetChatThreadsUseCase
import tech.appard.hvala.shared.feature.messages.domain.OpenChatForListingUseCase
import tech.appard.hvala.shared.feature.messages.domain.SendChatMessageUseCase
import tech.appard.hvala.shared.core.mvi.MviEffect
import tech.appard.hvala.shared.core.mvi.MviIntent
import tech.appard.hvala.shared.core.mvi.MviState
import tech.appard.hvala.shared.core.mvi.MviViewModel
import tech.appard.hvala.shared.feature.messages.ui.mapper.toDomain
import tech.appard.hvala.shared.feature.messages.ui.mapper.toMessagesUi
import tech.appard.hvala.shared.feature.messages.ui.mapper.toThreadsUi
import tech.appard.hvala.shared.feature.messages.ui.mapper.toUi
import tech.appard.hvala.shared.feature.messages.ui.model.UIChatMessage
import tech.appard.hvala.shared.feature.messages.ui.model.UIChatThread
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class MessagesUiState(
    val searchQuery: String = "",
    val threads: List<UIChatThread> = emptyList(),
    val isLoading: Boolean = false,
) : MviState

data class ChatUiState(
    val thread: UIChatThread? = null,
    val messages: List<UIChatMessage> = emptyList(),
    val inputText: String = "",
    val pendingAttachments: List<PickedMedia> = emptyList(),
    val isLoading: Boolean = false,
) : MviState

sealed interface MessagesIntent : MviIntent {
    data object LoadThreads : MessagesIntent
    data class SearchQueryChanged(val query: String) : MessagesIntent
}

sealed interface ChatIntent : MviIntent {
    data class Load(val threadId: String) : ChatIntent
    data class InputChanged(val text: String) : ChatIntent
    data class AttachmentsPicked(val attachments: List<PickedMedia>) : ChatIntent
    data object Send : ChatIntent
}

sealed interface MessagesEffect : MviEffect

class MessagesViewModel(
    private val getChatThreadsUseCase: GetChatThreadsUseCase,
    private val getChatThreadUseCase: GetChatThreadUseCase,
    private val getChatMessagesUseCase: GetChatMessagesUseCase,
    private val sendChatMessageUseCase: SendChatMessageUseCase,
    private val openChatForListingUseCase: OpenChatForListingUseCase,
) : MviViewModel<MessagesIntent, MessagesUiState, MessagesEffect>(MessagesUiState()) {

    val messagesState: StateFlow<MessagesUiState> = state

    private val _chatState = MutableStateFlow(ChatUiState())
    val chatState: StateFlow<ChatUiState> = _chatState.asStateFlow()

    private var threadsLoaded = false

    override suspend fun handleIntent(intent: MessagesIntent) {
        when (intent) {
            MessagesIntent.LoadThreads -> performLoadThreads()
            is MessagesIntent.SearchQueryChanged -> updateState { it.copy(searchQuery = intent.query) }
        }
    }

    fun loadThreads() = onIntent(MessagesIntent.LoadThreads)

    fun onSearchQueryChange(query: String) = onIntent(MessagesIntent.SearchQueryChanged(query))

    fun loadChat(threadId: String) {
        viewModelScope.launch {
            _chatState.value = _chatState.value.copy(isLoading = true)
            val thread = getChatThreadUseCase(threadId)?.toUi()
            val messages = getChatMessagesUseCase(threadId).toMessagesUi()
            _chatState.value = ChatUiState(
                isLoading = false,
                thread = thread,
                messages = messages,
                inputText = "",
                pendingAttachments = emptyList(),
            )
        }
    }

    fun onChatInputChange(text: String) {
        _chatState.value = _chatState.value.copy(inputText = text)
    }

    fun onAttachmentsPicked(attachments: List<PickedMedia>) {
        if (attachments.isEmpty()) return
        _chatState.value = _chatState.value.copy(
            pendingAttachments = _chatState.value.pendingAttachments + attachments,
        )
    }

    suspend fun openChatForListing(listingId: String): String? {
        val threadId = openChatForListingUseCase(listingId)
        refreshThreads()
        return threadId
    }

    fun sendMessage() {
        viewModelScope.launch {
            val snapshot = _chatState.value
            val threadId = snapshot.thread?.id ?: return@launch
            sendChatMessageUseCase(
                threadId,
                snapshot.inputText,
                snapshot.pendingAttachments.toDomain(),
            )
            _chatState.value = ChatUiState(
                thread = getChatThreadUseCase(threadId)?.toUi(),
                messages = getChatMessagesUseCase(threadId).toMessagesUi(),
                inputText = "",
                pendingAttachments = emptyList(),
                isLoading = false,
            )
            refreshThreads()
        }
    }

    private suspend fun performLoadThreads() {
        if (threadsLoaded || currentState().isLoading) return
        updateState { it.copy(isLoading = true) }
        val threads = getChatThreadsUseCase().toThreadsUi()
        updateState { it.copy(isLoading = false, threads = threads) }
        threadsLoaded = true
    }

    private suspend fun refreshThreads() {
        val threads = getChatThreadsUseCase().toThreadsUi()
        updateState { it.copy(threads = threads) }
        threadsLoaded = true
    }
}

typealias MessagesStateHolder = MessagesViewModel

internal fun formatLastMessagePreview(messages: List<UIChatMessage>): String {
    val lastMessage = messages.lastOrNull { !it.isDateDivider } ?: return "Нет сообщений"
    val preview = if (lastMessage.text.length > 72) {
        lastMessage.text.take(69) + "..."
    } else {
        lastMessage.text
    }
    return if (lastMessage.isOutgoing) "Вы: $preview" else preview
}
