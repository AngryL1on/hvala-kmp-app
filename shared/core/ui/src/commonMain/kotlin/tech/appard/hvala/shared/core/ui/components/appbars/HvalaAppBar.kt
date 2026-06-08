package tech.appard.hvala.shared.core.ui.components.appbars

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import tech.appard.hvala.shared.core.ui.theme.SecondaryMain
import tech.appard.hvala.shared.core.ui.theme.TitleLarge
import tech.appard.hvala.shared.core.ui.theme.White

@Immutable
data class HvalaAppBarState(
    val title: String? = null,
    val showBackButton: Boolean = false,
)

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun HvalaAppBar(
    state: HvalaAppBarState,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    TopAppBar(
        modifier = modifier,
        title = {
            state.title?.let { title ->
                Text(
                    text = title,
                    style = TitleLarge,
                )
            }
        },
        navigationIcon = {
            if (state.showBackButton) {
                IconButton(onClick = onBackClick) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back",
                        tint = SecondaryMain,
                    )
                }
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = White,
        ),
    )
}

@Composable
fun rememberHvalaAppBarState(
    title: String? = null,
    showBackButton: Boolean = false,
): HvalaAppBarState = remember(title, showBackButton) {
    HvalaAppBarState(
        title = title,
        showBackButton = showBackButton,
    )
}
