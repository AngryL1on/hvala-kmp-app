package tech.appard.hvala.shared.core.ui.platform

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import tech.appard.hvala.shared.core.ui.model.PickedMedia

@Composable
expect fun PickedMediaImage(
    media: PickedMedia,
    modifier: Modifier = Modifier,
    contentDescription: String? = null,
)
