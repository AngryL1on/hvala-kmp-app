package tech.appard.hvala.shared.feature.messages.presentation.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import tech.appard.hvala.shared.feature.messages.presentation.model.UIChatMessage
import tech.appard.hvala.shared.feature.messages.presentation.model.UIChatThread
import tech.appard.hvala.shared.feature.messages.presentation.mapper.resolvedListingId
import tech.appard.hvala.shared.core.ui.components.refresh.HvalaPullToRefreshBox
import tech.appard.hvala.shared.core.ui.model.MediaPickerMode
import tech.appard.hvala.shared.core.ui.theme.HvalaTheme
import tech.appard.hvala.shared.core.ui.theme.LocalDimensions
import tech.appard.hvala.shared.core.ui.theme.ScreenBackground
import tech.appard.hvala.shared.core.ui.theme.SecondaryMain
import tech.appard.hvala.shared.core.ui.theme.White
import tech.appard.hvala.shared.core.ui.platform.rememberMediaPickerLauncher
import tech.appard.hvala.shared.feature.messages.presentation.viewmodels.ChatUiState
import tech.appard.hvala.shared.feature.messages.presentation.viewmodels.MessagesViewModel
import tech.appard.hvala.shared.feature.messages.presentation.components.ChatInputBar
import tech.appard.hvala.shared.feature.messages.presentation.components.ChatListingCard
import tech.appard.hvala.shared.feature.messages.presentation.components.ChatMessageItem

@Composable
fun ChatScreen(
    threadId: String,
    viewModel: MessagesViewModel,
    modifier: Modifier = Modifier,
    onListingClick: (String) -> Unit = {},
) {
    val state by viewModel.chatState.collectAsState()
    val filePicker = rememberMediaPickerLauncher(
        mode = MediaPickerMode.Files,
        onResult = viewModel::onAttachmentsPicked,
    )

    LaunchedEffect(threadId) {
        viewModel.loadChat(threadId)
    }

    ChatContent(
        modifier = modifier,
        state = state,
        onInputChange = viewModel::onChatInputChange,
        onSendClick = viewModel::sendMessage,
        onListingClick = onListingClick,
        onAttachClick = { filePicker.launch(maxItems = 5) },
        onRefresh = { viewModel.refreshChat(threadId) },
    )
}

@Composable
private fun ChatContent(
    state: ChatUiState,
    onInputChange: (String) -> Unit,
    onSendClick: () -> Unit,
    onListingClick: (String) -> Unit,
    onAttachClick: () -> Unit,
    onRefresh: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val dimensions = LocalDimensions.current
    val thread = state.thread
    val focusManager = LocalFocusManager.current
    val keyboardController = LocalSoftwareKeyboardController.current
    val dismissKeyboard: () -> Unit = {
        focusManager.clearFocus()
        keyboardController?.hide()
    }

    DisposableEffect(Unit) {
        onDispose { dismissKeyboard() }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(ScreenBackground)
            .imePadding(),
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
            val dismissKeyboardOnUserScroll = remember(focusManager, keyboardController) {
                object : NestedScrollConnection {
                    override fun onPreScroll(available: Offset, source: NestedScrollSource): Offset {
                        if (source == NestedScrollSource.UserInput) {
                            focusManager.clearFocus()
                            keyboardController?.hide()
                        }
                        return Offset.Zero
                    }
                }
            }

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

            HvalaPullToRefreshBox(
                isRefreshing = state.isRefreshing,
                onRefresh = onRefresh,
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
            ) {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .nestedScroll(dismissKeyboardOnUserScroll),
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
                        ChatMessageItem(
                            message = message,
                            modifier = Modifier.clickable(
                                interactionSource = remember(message.id) { MutableInteractionSource() },
                                indication = null,
                                onClick = dismissKeyboard,
                            ),
                        )
                    }
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
                thread = UIChatThread(
                    id = "niko",
                    participantName = "Нико Б.",
                    lastMessagePreview = "Да, без проблем. Если что — напишите.",
                    avatarColorArgb = 0xFFFFB74D,
                    listingTitle = "Худи Number Nine",
                    listingPriceUsd = 150,
                    listingPriceRub = 12_570,
                ),
                messages = listOf(
                    UIChatMessage(
                        id = "divider",
                        text = "Сегодня, 8:43",
                        isOutgoing = false,
                        isDateDivider = true,
                    ),
                    UIChatMessage(
                        id = "1",
                        text = "Буду к 18:30. С собой наличные подойдут?",
                        isOutgoing = true,
                    ),
                    UIChatMessage(
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
            onRefresh = {},
        )
    }
}
