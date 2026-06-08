package tech.appard.hvala.shared.feature.messages

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import tech.appard.hvala.shared.core.contracts.model.ChatMessage
import tech.appard.hvala.shared.core.contracts.model.ChatThread
import tech.appard.hvala.shared.core.contracts.model.resolvedListingId
import tech.appard.hvala.shared.core.ui.theme.HvalaTheme
import tech.appard.hvala.shared.core.ui.theme.LocalDimensions
import tech.appard.hvala.shared.core.ui.theme.ScreenBackground
import tech.appard.hvala.shared.core.ui.theme.SecondaryMain
import tech.appard.hvala.shared.core.ui.theme.White
import tech.appard.hvala.shared.core.contracts.model.MediaPickerMode
import tech.appard.hvala.shared.core.ui.platform.rememberMediaPickerLauncher
import tech.appard.hvala.shared.feature.messages.components.ChatInputBar
import tech.appard.hvala.shared.feature.messages.components.ChatListingCard
import tech.appard.hvala.shared.feature.messages.components.ChatMessageItem

@Composable
fun ChatScreen(
    threadId: String,
    stateHolder: MessagesStateHolder,
    modifier: Modifier = Modifier,
    onListingClick: (String) -> Unit = {},
) {
    val state by stateHolder.chatState.collectAsState()
    val filePicker = rememberMediaPickerLauncher(
        mode = MediaPickerMode.Files,
        onResult = stateHolder::onAttachmentsPicked,
    )

    LaunchedEffect(threadId) {
        stateHolder.loadChat(threadId)
    }

    ChatContent(
        modifier = modifier,
        state = state,
        onInputChange = stateHolder::onChatInputChange,
        onSendClick = stateHolder::sendMessage,
        onListingClick = onListingClick,
        onAttachClick = { filePicker.launch(maxItems = 5) },
    )
}

@Composable
private fun ChatContent(
    state: ChatUiState,
    onInputChange: (String) -> Unit,
    onSendClick: () -> Unit,
    onListingClick: (String) -> Unit,
    onAttachClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val dimensions = LocalDimensions.current
    val thread = state.thread

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(ScreenBackground),
    ) {
        if (state.isLoading || thread == null) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center,
            ) {
                CircularProgressIndicator(color = SecondaryMain)
            }
        } else {
            val listingTitle = thread.listingTitle
            val listingPriceUsd = thread.listingPriceUsd
            val listingPriceRub = thread.listingPriceRub
            val hasListingCard = listingTitle != null &&
                listingPriceUsd != null &&
                listingPriceRub != null
            val listingId = thread.resolvedListingId()
            val listState = rememberLazyListState()

            LaunchedEffect(thread.id, state.messages.size, state.messages.lastOrNull()?.id) {
                val lastIndex = state.messages.lastIndex
                if (lastIndex >= 0) {
                    listState.animateScrollToItem(lastIndex)
                }
            }

            if (hasListingCard) {
                ChatListingCard(
                    title = listingTitle,
                    priceUsd = listingPriceUsd,
                    priceRub = listingPriceRub,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(
                            start = dimensions.horizontalMedium,
                            end = dimensions.horizontalMedium,
                            top = dimensions.verticalMedium,
                            bottom = dimensions.verticalSmall,
                        ),
                    onClick = listingId?.let { id -> { onListingClick(id) } },
                )
            }

            LazyColumn(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
                state = listState,
                contentPadding = PaddingValues(
                    top = if (hasListingCard) 0.dp else dimensions.verticalMedium,
                    bottom = dimensions.verticalSmall,
                ),
                verticalArrangement = Arrangement.spacedBy(dimensions.verticalXXSmall),
            ) {
                items(
                    items = state.messages,
                    key = { it.id },
                ) { message ->
                    ChatMessageItem(message = message)
                }
            }

            ChatInputBar(
                value = state.inputText,
                onValueChange = onInputChange,
                onSendClick = onSendClick,
                onAttachClick = onAttachClick,
                modifier = Modifier
                    .fillMaxWidth()
                    .background(White)
                    .padding(
                        horizontal = dimensions.horizontalMedium,
                        vertical = dimensions.verticalSmall,
                    ),
            )
        }
    }
}

@Composable
@Preview
private fun ChatScreenPreview() {
    HvalaTheme {
        ChatContent(
            state = ChatUiState(
                thread = ChatThread(
                    id = "niko",
                    participantName = "Нико Б.",
                    lastMessagePreview = "Да, без проблем. Если что — напишите.",
                    avatarColorArgb = 0xFFFFB74D,
                    listingTitle = "Худи Number Nine",
                    listingPriceUsd = 150,
                    listingPriceRub = 12_570,
                ),
                messages = listOf(
                    ChatMessage(
                        id = "divider",
                        text = "Сегодня, 8:43",
                        isOutgoing = false,
                        isDateDivider = true,
                    ),
                    ChatMessage(
                        id = "1",
                        text = "Буду к 18:30. С собой наличные подойдут?",
                        isOutgoing = true,
                    ),
                    ChatMessage(
                        id = "2",
                        text = "Да, без проблем. Если что — напишите.",
                        isOutgoing = false,
                    ),
                ),
            ),
            onInputChange = {},
            onSendClick = {},
            onListingClick = {},
            onAttachClick = {},
        )
    }
}
