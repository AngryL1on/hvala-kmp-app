package tech.appard.hvala.shared.feature.messages.presentation.screens

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
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import tech.appard.hvala.shared.feature.messages.presentation.model.UIChatThread
import tech.appard.hvala.shared.core.ui.theme.HvalaTheme
import tech.appard.hvala.shared.core.ui.theme.LocalDimensions
import tech.appard.hvala.shared.core.ui.theme.ScreenBackground
import tech.appard.hvala.shared.core.ui.theme.SecondaryMain
import tech.appard.hvala.shared.core.ui.theme.White
import tech.appard.hvala.shared.feature.messages.presentation.viewmodels.MessagesStateHolder
import tech.appard.hvala.shared.feature.messages.presentation.viewmodels.MessagesUiState
import tech.appard.hvala.shared.feature.messages.presentation.components.ChatThreadCard
import tech.appard.hvala.shared.feature.messages.presentation.components.MessagesSearchBar

@Composable
fun MessagesScreen(
    stateHolder: MessagesStateHolder,
    modifier: Modifier = Modifier,
    onChatClick: (String) -> Unit = {},
) {
    val state by stateHolder.messagesState.collectAsState()

    LaunchedEffect(Unit) {
        stateHolder.loadThreads()
    }

    MessagesContent(
        modifier = modifier,
        state = state,
        onSearchQueryChange = stateHolder::onSearchQueryChange,
        onChatClick = onChatClick,
    )
}

@Composable
private fun MessagesContent(
    state: MessagesUiState,
    onSearchQueryChange: (String) -> Unit,
    onChatClick: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    val dimensions = LocalDimensions.current
    val filteredThreads = state.threads.filter { thread ->
        val query = state.searchQuery.trim()
        if (query.isEmpty()) return@filter true
        thread.participantName.contains(query, ignoreCase = true) ||
            thread.lastMessagePreview.contains(query, ignoreCase = true)
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(ScreenBackground),
    ) {
        MessagesSearchBar(
            query = state.searchQuery,
            onQueryChange = onSearchQueryChange,
            modifier = Modifier
                .fillMaxWidth()
                .background(White)
                .padding(
                    horizontal = dimensions.horizontalMedium,
                    vertical = dimensions.verticalMedium,
                ),
        )

        if (state.isLoading) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center,
            ) {
                CircularProgressIndicator(color = SecondaryMain)
            }
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(
                    start = dimensions.horizontalMedium,
                    end = dimensions.horizontalMedium,
                    top = dimensions.verticalMedium,
                    bottom = dimensions.verticalMedium,
                ),
                verticalArrangement = Arrangement.spacedBy(dimensions.verticalMedium),
            ) {
                items(
                    items = filteredThreads,
                    key = { it.id },
                ) { thread ->
                    ChatThreadCard(
                        thread = thread,
                        onClick = { onChatClick(thread.id) },
                    )
                }
            }
        }
    }
}

@Composable
@Preview
private fun MessagesScreenPreview() {
    HvalaTheme {
        MessagesContent(
            state = MessagesUiState(
                threads = listOf(
                    UIChatThread(
                        id = "niko",
                        participantName = "Нико Б.",
                        lastMessagePreview = "Да, без проблем. Если что — напишите.",
                        avatarColorArgb = 0xFFFFB74D,
                    ),
                ),
            ),
            onSearchQueryChange = {},
            onChatClick = {},
        )
    }
}
