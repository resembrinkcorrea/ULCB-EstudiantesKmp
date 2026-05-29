package pe.lecordonbleu.universidadestudiante.presentation.screens.notas.customcell

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import pe.lecordonbleu.universidadestudiante.getColorsTheme

@Composable
fun SubTabCard(
    title      : String,
    prom       : String,
    peso       : Int?,
    isSelected : Boolean,
    modifier   : Modifier = Modifier,
    onClick    : () -> Unit
) {
    val colors = getColorsTheme()
    Surface(
        modifier = modifier.clickable { onClick() },
        color    = if (isSelected) colors.colorMixPrimary.copy(alpha = 0.05f) else colors.colorExpenseItem,
        shape    = RoundedCornerShape(12.dp),
        border   = BorderStroke(if (isSelected) 1.5.dp else 0.5.dp, if (isSelected) colors.colorMixPrimary else colors.textColor.copy(alpha = 0.08f))
    ) {
        Column(
            modifier            = Modifier.fillMaxWidth().padding(vertical = 12.dp, horizontal = 8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = title, fontSize = 10.sp, fontWeight = FontWeight.Bold, letterSpacing = 0.5.sp, color = if (isSelected) colors.colorMixPrimary else colors.textColor.copy(alpha = 0.6f))
            Spacer(modifier = Modifier.height(8.dp))
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(text = "PROM", fontSize = 9.sp, fontWeight = FontWeight.Bold, color = colors.textColor.copy(alpha = 0.4f))
                    Text(text = prom.ifEmpty { "-" }, fontSize = 16.sp, fontWeight = FontWeight.Black, color = colors.textColor)
                }
                if (peso != null && peso > 0) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(text = "PESO", fontSize = 9.sp, fontWeight = FontWeight.Bold, color = colors.textColor.copy(alpha = 0.4f))
                        Text(text = "$peso%", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = colors.textColor.copy(alpha = 0.7f))
                    }
                }
            }
        }
    }
}
