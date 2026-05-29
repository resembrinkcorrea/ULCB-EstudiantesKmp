package pe.lecordonbleu.universidadestudiante

import androidx.compose.runtime.Composable

@Composable
expect fun SelectorImagenes(
    onImagenSeleccionada: (ByteArray, String, String) -> Unit,
    onDismiss: () -> Unit
)
