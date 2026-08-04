@file:OptIn(ExperimentalMaterial3Api::class)

package pe.lecordonbleu.universidadestudiante.presentation.screens.vermatricula.registrar

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import pe.lecordonbleu.universidadestudiante.ThemedHtmlWebView
import pe.lecordonbleu.universidadestudiante.getColorsTheme

@Composable
fun InicioMatriculaTab(
    welcomeHtml: String,
    turnoInicio: String,
    turnoHorario: String,
    flagEnabled: Int,
    onIniciarClick: () -> Unit
) {
    val colors = getColorsTheme()

    Column(modifier = Modifier.fillMaxSize()) {
        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
        ) {
            if (welcomeHtml.isNotEmpty()) {
                ThemedHtmlWebView(
                    html = welcomeHtml,
                    modifier = Modifier.fillMaxSize()
                )
            }
        }

        if (turnoHorario.isNotEmpty()) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Turno:  $turnoInicio  $turnoHorario",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = colors.textColor
                )
            }
        }

        if (flagEnabled == 1) {
            Button(
                onClick = onIniciarClick,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = colors.colorMixPrimary
                )
            ) {
                Text(
                    text = "Iniciar Matricula",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))
    }
}
