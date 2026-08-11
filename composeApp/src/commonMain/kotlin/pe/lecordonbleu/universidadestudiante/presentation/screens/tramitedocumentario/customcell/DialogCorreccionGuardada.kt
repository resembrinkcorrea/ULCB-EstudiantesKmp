package pe.lecordonbleu.universidadestudiante.presentation.screens.tramitedocumentario.customcell

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import pe.lecordonbleu.universidadestudiante.getColorsTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DialogCorreccionGuardada(
    titulo: String,
    mensaje: String,
    onConfirm: () -> Unit
) {
    val colors = getColorsTheme()

    BasicAlertDialog(onDismissRequest = onConfirm) {
        Column(
            modifier = Modifier
                .clip(RoundedCornerShape(20.dp))
                .background(colors.colorExpenseItem)
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(64.dp)
                    .clip(CircleShape)
                    .background(colors.colorVerdeMedio.copy(alpha = 0.15f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Check,
                    contentDescription = null,
                    tint = colors.colorVerdeMedio,
                    modifier = Modifier.size(36.dp)
                )
            }

            Text(
                text = titulo,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = colors.textColor,
                textAlign = TextAlign.Center
            )

            Text(
                text = mensaje,
                fontSize = 14.sp,
                color = colors.colorGrisNeutro,
                textAlign = TextAlign.Center,
                lineHeight = 20.sp
            )

            Spacer(modifier = Modifier.height(4.dp))

            Button(
                onClick = onConfirm,
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = colors.colorVerdeMedio,
                    contentColor = Color.White
                )
            ) {
                Text("Aceptar", fontWeight = FontWeight.SemiBold)
            }
        }
    }
}
