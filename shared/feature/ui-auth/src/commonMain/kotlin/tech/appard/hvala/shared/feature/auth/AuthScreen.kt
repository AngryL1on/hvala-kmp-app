package tech.appard.hvala.shared.feature.auth

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import tech.appard.hvala.shared.core.ui.SharedScreenScaffold

@Composable
fun AuthScreen(
    stateHolder: AuthStateHolder,
    modifier: Modifier = Modifier,
    onAuthenticated: () -> Unit = {},
) {
    val state by stateHolder.state.collectAsState()
    SharedScreenScaffold(title = "Auth") {
        AuthContent(
            modifier = modifier,
            state = state,
            onLoginChange = stateHolder::onLoginChange,
            onPasswordChange = stateHolder::onPasswordChange,
            onSignIn = { stateHolder.signIn(onAuthenticated) },
        )
    }
}

@Composable
private fun AuthContent(
    modifier: Modifier = Modifier,
    state: AuthUiState,
    onLoginChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onSignIn: () -> Unit,
) {
    OutlinedTextField(
        value = state.login,
        onValueChange = onLoginChange,
        modifier = modifier.fillMaxWidth(),
        label = { Text("Login") },
    )

    Spacer(modifier = modifier.height(12.dp))

    OutlinedTextField(
        value = state.password,
        onValueChange = onPasswordChange,
        modifier = modifier.fillMaxWidth(),
        label = { Text("Password") },
    )

    Spacer(modifier = modifier.height(24.dp))

    if (state.error != null) {
        Text(text = state.error)
        Spacer(modifier = modifier.height(8.dp))
    }

    Button(
        enabled = !state.isLoading,
        onClick = onSignIn,
    ) {
        if (state.isLoading) {
            CircularProgressIndicator()
        } else {
            Text("Sign in")
        }
    }
}
