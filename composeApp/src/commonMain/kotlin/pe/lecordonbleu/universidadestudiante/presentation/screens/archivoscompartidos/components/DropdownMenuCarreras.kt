package pe.lecordonbleu.universidadestudiante.presentation.screens.archivoscompartidos.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun DropdownMenuCarreras(
    carreras: List<Pair<String, Int>>,
    selectedIndex: Int,
    onCarreraSelected: (index: Int, idTipoServa: Int) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    val selectedText = if (selectedIndex in carreras.indices) carreras[selectedIndex].first else "Programa"

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .clickable { expanded = true }
            .background(Color.LightGray.copy(alpha = 0.1f), shape = RoundedCornerShape(8.dp))
            .padding(12.dp)
    ) {
        Text(text = selectedText)

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            carreras.forEachIndexed { index, carrera ->
                DropdownMenuItem(
                    text = { Text(text = carrera.first) },
                    onClick = {
                        expanded = false
                        onCarreraSelected(index, carrera.second)
                    }
                )
            }
        }
    }
}
