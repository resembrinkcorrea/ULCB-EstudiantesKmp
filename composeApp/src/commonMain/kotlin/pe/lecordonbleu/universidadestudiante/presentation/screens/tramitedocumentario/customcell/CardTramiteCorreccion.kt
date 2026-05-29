package pe.lecordonbleu.universidadestudiante.presentation.screens.tramitedocumentario.customcell

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Save
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import pe.lecordonbleu.universidadestudiante.data.remote.dto.TrcTramiteItem
import pe.lecordonbleu.universidadestudiante.getColorsTheme

@Composable
fun CardTramiteCorreccion(
    trcList: List<TrcTramiteItem>,
    saveClickCorregir: (String) -> Unit
) {
    val colors = getColorsTheme()
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .border(1.dp, colors.colorGrisAzulado, RoundedCornerShape(4.dp))
            .padding(3.dp)
    ) {
        trcList.forEach { item ->
            val deshabilitado = estaDeshabilitado(item.accionHtml)
            val estadoTitulo = extraerTituloEstado(item.estadoHtml)
            val estadoColor = colorEstadoTramite(estadoTitulo)
            var textInput by remember(item.contador) { mutableStateOf(if (deshabilitado) item.descripcion else "") }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 6.dp, horizontal = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                OutlinedTextField(
                    value = textInput,
                    onValueChange = { textInput = it },
                    label = { Text("Correccion") },
                    enabled = !deshabilitado,
                    modifier = Modifier
                        .weight(0.6f)
                        .padding(end = 8.dp),
                    minLines = 2,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = colors.textColor,
                        unfocusedTextColor = colors.textColor,
                        disabledTextColor = colors.colorGrisNeutro
                    )
                )

                Box(
                    modifier = Modifier
                        .size(20.dp)
                        .clip(CircleShape)
                        .background(estadoColor)
                )

                Spacer(modifier = Modifier.width(12.dp))

                Box(
                    modifier = Modifier
                        .size(34.dp)
                        .clip(RoundedCornerShape(4.dp))
                        .background(if (deshabilitado) colors.colorGrisAzulado else colors.colorVerdeMedio)
                        .clickable(enabled = !deshabilitado) { saveClickCorregir(textInput) },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Save,
                        contentDescription = "Guardar",
                        tint = Color.White,
                        modifier = Modifier.size(18.dp)
                    )
                }
            }
        }
    }
}
