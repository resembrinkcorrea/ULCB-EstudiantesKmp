package pe.lecordonbleu.universidadestudiante.presentation.screens.mallacurricular.customcell

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import pe.lecordonbleu.universidadestudiante.data.remote.dto.ListTablaPlanEstudio
import pe.lecordonbleu.universidadestudiante.getColorsTheme

@Composable
fun GraficaItemCicloColumnCell(
    ciclo: Int,
    asignaturas: List<ListTablaPlanEstudio>,
    coloresAsignaturas: Map<String, Color>
) {
    val colors = getColorsTheme()

    Column(
        modifier = Modifier
            .width(180.dp)
            .padding(4.dp)
    ) {
        Text(
            text = "Ciclo $ciclo",
            fontWeight = FontWeight.Bold,
            fontSize = 14.sp,
            color = colors.colorMixPrimary,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp),
            textAlign = TextAlign.Center
        )

        asignaturas.forEach { asignatura ->
            val color = coloresAsignaturas[asignatura.ASIGNATURA.trim()] ?: colors.colorExpenseItem

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp)
                    .background(color, RoundedCornerShape(6.dp))
                    .border(1.dp, Color.DarkGray, RoundedCornerShape(6.dp))
                    .padding(8.dp)
            ) {
                Text(
                    text = asignatura.ASIGNATURA,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center
                )

                if (asignatura.PREREQUISITO.isNotBlank() && asignatura.PREREQUISITO.trim() != "-") {
                    Spacer(Modifier.height(4.dp))
                    Text(
                        text = "Prerrequisito: ${asignatura.PREREQUISITO}",
                        fontSize = 10.sp,
                        fontStyle = FontStyle.Italic,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        }
    }
}
