package pe.lecordonbleu.universidadestudiante.presentation.screens.archivoscompartidos.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import pe.lecordonbleu.universidadestudiante.data.remote.dto.ContenidoTags
import pe.lecordonbleu.universidadestudiante.getColorsTheme

@Composable
fun ContenidoTabCompartidos(
    items: List<ContenidoTags>,
    isLoading: Boolean,
    onItemClick: (ContenidoTags) -> Unit,
    onCheckClick: (ContenidoTags) -> Unit
) {
    val colors = getColorsTheme()

    when {
        isLoading -> {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(color = colors.colorMixPrimary)
            }
        }
        items.isEmpty() -> {
            Text("Sin archivos disponibles.", modifier = Modifier.padding(16.dp))
        }
        else -> {
            LazyColumn(modifier = Modifier.fillMaxSize()) {
                items(items) { doc ->
                    ContenidoArchivoItem(
                        doc = doc,
                        onClick = { onItemClick(doc) },
                        onCheckClick = { onCheckClick(doc) }
                    )
                }
            }
        }
    }
}
