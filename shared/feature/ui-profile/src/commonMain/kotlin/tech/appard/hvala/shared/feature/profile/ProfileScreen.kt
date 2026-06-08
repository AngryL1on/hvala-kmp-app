package tech.appard.hvala.shared.feature.profile

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import tech.appard.hvala.shared.core.ui.theme.LocalDimensions

@Composable
fun ProfileScreen(
    stateHolder: ProfileStateHolder,
    modifier: Modifier = Modifier,
    onLogout: () -> Unit = {},
) {
    val state by stateHolder.state.collectAsState()
    val dimensions = LocalDimensions.current

    LaunchedEffect(Unit) {
        stateHolder.load()
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = dimensions.horizontalMedium),
    ) {
        ProfileContent(
            state = state,
            onLogout = onLogout,
        )
    }
}

@Composable
private fun ProfileContent(
    state: ProfileUiState,
    onLogout: () -> Unit,
    modifier: Modifier = Modifier,
) {
    if (state.isLoading) {
        CircularProgressIndicator(modifier = modifier)
    } else {
        Text(
            text = "User profile: ${state.profile?.fullName ?: "unknown"}",
            modifier = modifier,
            style = MaterialTheme.typography.bodyLarge,
        )
        Text(
            text = "Email: ${state.profile?.email ?: "unknown"}",
            modifier = modifier,
            style = MaterialTheme.typography.bodyLarge,
        )
    }

    Spacer(modifier = modifier.height(24.dp))

    Button(onClick = onLogout) {
        Text(
            text = "Logout",
            style = MaterialTheme.typography.labelMedium,
        )
    }
}
