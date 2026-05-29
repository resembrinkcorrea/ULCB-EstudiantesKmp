package pe.lecordonbleu.universidadestudiante

import androidx.compose.runtime.Composable

@Composable
expect fun SelectorDocumentoPDF(
    onDocumentoSeleccionado: (ByteArray, String) -> Unit,
    onDismiss: () -> Unit
)
