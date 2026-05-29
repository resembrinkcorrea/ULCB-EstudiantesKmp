package pe.lecordonbleu.universidadestudiante

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
expect fun WebViewComposable(
    url: String,
    returnDomain: String,
    onClose: () -> Unit,
    modifier: Modifier = Modifier
)
