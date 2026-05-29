package pe.lecordonbleu.universidadestudiante

import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
actual fun LoadingIndicator(modifier: Modifier) {
    CircularProgressIndicator(modifier = modifier)
}
