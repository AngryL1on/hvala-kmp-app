package tech.appard.hvala.navigation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import tech.appard.hvala.shared.core.ui.theme.FieldTitle
import tech.appard.hvala.shared.core.ui.theme.GrayText
import tech.appard.hvala.shared.core.ui.theme.ScreenBackground

@Composable
fun MainPlaceholderScreen(
    title: String,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(ScreenBackground),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = title,
            style = FieldTitle.copy(color = GrayText),
        )
    }
}
