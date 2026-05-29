package pe.lecordonbleu.universidadestudiante.presentation.screens.tramitedocumentario.customcell

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import pe.lecordonbleu.universidadestudiante.getColorsTheme
import pe.lecordonbleu.universidadestudiante.presentation.components.MyComboBoxComponentModel2

@Composable
fun ListadoDocumentos(
    paises: List<String>,
    selectedPais: String,
    onPaisSelected: (String) -> Unit
) {
    val colors = getColorsTheme()
    val opciones = if (paises.isNotEmpty()) paises else listOf(selectedPais).filter { it.isNotBlank() }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        Text(
            text = "Selecciona un pais",
            color = colors.textColor,
            modifier = Modifier.padding(start = 4.dp, bottom = 4.dp)
        )

        Card(
            colors = CardDefaults.cardColors(containerColor = colors.backGroundColor),
            shape = RoundedCornerShape(0.dp),
            border = null
        ) {
            Column(modifier = Modifier.padding(8.dp)) {
                MyComboBoxComponentModel2(
                    items = opciones,
                    label = "",
                    initialSelection = selectedPais,
                    onItemSelected = onPaisSelected
                )
            }
        }

        Spacer(modifier = Modifier.height(12.dp))
    }
}
