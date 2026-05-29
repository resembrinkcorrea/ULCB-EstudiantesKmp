package pe.lecordonbleu.universidadestudiante.presentation.screens.notas.helpers

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.runtime.toMutableStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import pe.lecordonbleu.universidadestudiante.getColorsTheme
import pe.lecordonbleu.universidadestudiante.presentation.screens.notas.uibuilder.SimuladorNota
import kotlin.math.round

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SimuladorNotasSheet(
    notas    : List<SimuladorNota>,
    notaMin  : String,
    onDismiss: () -> Unit
) {
    val colors     = getColorsTheme()
    val notaMinVal = notaMin.toDoubleOrNull() ?: 0.0

    val inputValues: SnapshotStateList<androidx.compose.runtime.MutableState<String>> =
        remember(notas) { notas.map { mutableStateOf(it.notaSimulada) }.toMutableStateList() }

    fun calcularResultado(): Double {
        var suma = 0.0
        notas.forEachIndexed { i, item ->
            if (item.peso > 0.0) {
                val nota = inputValues[i].value.toDoubleOrNull() ?: item.notaActual
                suma += nota * item.peso / 100.0
            }
        }
        return suma
    }

    fun formatNota(value: Double): String {
        val centesimas = round(value * 100).toInt().coerceAtLeast(0)
        val intPart    = centesimas / 100
        val decPart    = centesimas % 100
        return "$intPart.${decPart.toString().padStart(2, '0')}"
    }

    val resultado    = calcularResultado()
    val resultadoStr = formatNota(resultado)
    val aprobado     = resultado >= notaMinVal

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState       = rememberModalBottomSheetState(skipPartiallyExpanded = true),
        containerColor   = colors.backGroundColor
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp)
                .padding(bottom = 32.dp)
        ) {
            // Titulo
            Text(
                text          = "Simulador de Promedio",
                fontWeight    = FontWeight.Bold,
                fontSize      = 16.sp,
                color         = colors.textColor
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text  = "Las notas ingresadas son solo referenciales",
                style = MaterialTheme.typography.bodySmall,
                color = colors.textColor.copy(alpha = 0.5f)
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Filas de input
            notas.forEachIndexed { i, item ->
                FilaInputSimulacion(
                    nombre        = item.nombreEvaluacion,
                    peso          = item.peso,
                    inputVal      = inputValues[i].value,
                    onValueChange = { inputValues[i].value = it }
                )
                Spacer(modifier = Modifier.height(10.dp))
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Promedio Final Proyectado — al final
            val colorResultado = if (aprobado) colors.colorVerdeMedio else colors.colorRojo
            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape    = RoundedCornerShape(14.dp),
                color    = colors.colorExpenseItem
            ) {
                Row(
                    modifier          = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text       = "Promedio Final Proyectado",
                        style      = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Bold,
                        color      = colors.textColor,
                        modifier   = Modifier.weight(1f)
                    )
                    Row(verticalAlignment = Alignment.Bottom) {
                        Text(
                            text       = resultadoStr,
                            fontSize   = 30.sp,
                            fontWeight = FontWeight.Black,
                            color      = colorResultado
                        )
                        Text(
                            text     = " / 20",
                            style    = MaterialTheme.typography.titleMedium,
                            color    = colorResultado.copy(alpha = 0.7f),
                            modifier = Modifier.padding(bottom = 3.dp)
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun FilaInputSimulacion(
    nombre       : String,
    peso         : Double,
    inputVal     : String,
    onValueChange: (String) -> Unit
) {
    val colors = getColorsTheme()

    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape    = RoundedCornerShape(12.dp),
        color    = colors.colorExpenseItem
    ) {
        Row(
            modifier          = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text       = nombre,
                    style      = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.SemiBold,
                    color      = colors.textColor
                )
                if (peso > 0.0) {
                    Text(
                        text  = "Peso: ${peso.toInt()}%",
                        style = MaterialTheme.typography.labelSmall,
                        color = colors.textColor.copy(alpha = 0.4f)
                    )
                }
            }
            Spacer(modifier = Modifier.width(12.dp))
            OutlinedTextField(
                value           = inputVal,
                onValueChange   = onValueChange,
                modifier        = Modifier.width(88.dp),
                singleLine      = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                textStyle       = TextStyle(
                    fontWeight = FontWeight.Bold,
                    fontSize   = 16.sp,
                    color      = colors.textColor
                )
            )
        }
    }
}
