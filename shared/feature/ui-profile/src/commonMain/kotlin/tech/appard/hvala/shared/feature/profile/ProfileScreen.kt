package tech.appard.hvala.shared.feature.profile

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import tech.appard.hvala.shared.core.ui.SharedScreenScaffold

@Composable
fun ProfileScreen(
    stateHolder: ProfileStateHolder,
    modifier: Modifier = Modifier,
) {
    val state by stateHolder.state.collectAsState()
    LaunchedEffect(Unit) {
        stateHolder.load()
    }

    SharedScreenScaffold(title = "Profile") {
        ProfileContent(
            modifier = modifier,
            state = state,
            onLogout = stateHolder::logout,
        )
    }
}

@Composable
private fun ProfileContent(
    modifier: Modifier = Modifier,
    state: ProfileUiState,
    onLogout: () -> Unit,
) {
    if (state.isLoading) {
        CircularProgressIndicator()
    } else {
        Text(
            text = "User profile: ${state.profile?.fullName ?: "unknown"}",
            modifier = modifier,
        )
        Text(
            text = "Email: ${state.profile?.email ?: "unknown"}",
            modifier = modifier,
        )
    }

    Spacer(modifier = modifier.height(24.dp))

    Button(onClick = onLogout) {
        Text("Logout")
    }
}
