package pe.lecordonbleu.universidadestudiante.presentation.screens.vermatricula.registrar

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import pe.lecordonbleu.universidadestudiante.ThemedHtmlWebView
import pe.lecordonbleu.universidadestudiante.getColorsTheme

@Composable
fun CondicionesMatriculaTab(
    condicionesHtml: String,
    onAceptarClick: () -> Unit,
    onAtrasClick: () -> Unit
) {
    val colors = getColorsTheme()

    Column(modifier = Modifier.fillMaxSize()) {
        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
        ) {
            if (condicionesHtml.isNotEmpty()) {
                ThemedHtmlWebView(
                    html = condicionesHtml,
                    modifier = Modifier.fillMaxSize()
                )
            }
        }

        androidx.compose.foundation.layout.Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp),
            horizontalArrangement = androidx.compose.foundation.layout.Arrangement.spacedBy(8.dp)
        ) {
            Button(
                onClick = onAtrasClick,
                modifier = Modifier.weight(1f),
                colors = ButtonDefaults.buttonColors(
                    containerColor = colors.colorExpenseItem,
                    contentColor = colors.colorMixPrimary
                )
            ) {
                Text(
                    text = "Atrás",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }
            Button(
                onClick = onAceptarClick,
                modifier = Modifier.weight(1f),
                colors = ButtonDefaults.buttonColors(
                    containerColor = colors.colorMixPrimary
                )
            ) {
                Text(
                    text = "Continuar",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))
    }
}
