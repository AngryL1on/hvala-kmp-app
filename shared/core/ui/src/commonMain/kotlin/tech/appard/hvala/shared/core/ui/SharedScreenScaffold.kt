package tech.appard.hvala.shared.core.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import tech.appard.hvala.shared.core.ui.theme.LocalDimensions

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun SharedScreenScaffold(
    title: String,
    content: @Composable ColumnScope.() -> Unit,
) {
    val dimensions = LocalDimensions.current
    Scaffold(
        topBar = {
            TopAppBar(title = { Text(text = title) })
        },
        modifier = Modifier.fillMaxSize(),
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .padding(horizontal = dimensions.horizontalMedium),
            content = content,
        )
    }
}
