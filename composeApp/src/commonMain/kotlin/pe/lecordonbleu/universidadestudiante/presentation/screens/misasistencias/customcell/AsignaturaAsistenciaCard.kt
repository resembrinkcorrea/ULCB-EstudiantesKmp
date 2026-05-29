package pe.lecordonbleu.universidadestudiante.presentation.screens.misasistencias.customcell

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import pe.lecordonbleu.universidadestudiante.DarkModeColors
import pe.lecordonbleu.universidadestudiante.data.remote.dto.AsignaturaEstudiante

@Composable
fun AsignaturaAsistenciaCard(
    item: AsignaturaEstudiante,
    colors: DarkModeColors,
    onClick: () -> Unit
) {
    val porcentajeInasistencia = item.matric_asig_porc_inasistencia.toFloatOrNull() ?: 0f
    val porcentajePermitido = item.pest_det_asis_min.toFloatOrNull() ?: 30f
    val porcentajeDisplay = item.matric_asig_porc_inasistencia
    val faltasRestantes = item.total_max_inas.toDoubleOrNull()?.toInt() ?: 0

    val (estadoColor, estadoBgColor) = when {
        porcentajeInasistencia >= porcentajePermitido ->
            Pair(colors.colorRojo, colors.colorRojo.copy(alpha = 0.1f))
        porcentajeInasistencia >= (porcentajePermitido * 0.7f) ->
            Pair(colors.colorNaranjaOscuro, colors.colorNaranjaOscuro.copy(alpha = 0.1f))
        else ->
            Pair(colors.colorMixPrimary, colors.colorMixPrimary.copy(alpha = 0.1f))
    }

    val cuadritoColor = if (porcentajeInasistencia > 0) colors.colorNaranjaOscuro else colors.colorMixPrimary
    val cuadritoBgColor = if (porcentajeInasistencia > 0) colors.colorNaranjaOscuro.copy(alpha = 0.1f) else colors.colorMixPrimary.copy(alpha = 0.1f)

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        shape = RoundedCornerShape(20.dp),
        color = colors.colorExpenseItem,
        border = BorderStroke(1.dp, colors.textColor.copy(alpha = 0.25f)),
        shadowElevation = 8.dp
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {

            HeaderImageSection(
                imageUrl = item.peda_url_imagen ?: "",
                nombre = item.pest_asign_nombre
            )

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp)
            ) {
                AttendanceProgressSection(
                    porcentajeInasistencia = porcentajeInasistencia,
                    porcentajePermitido = porcentajePermitido,
                    porcentajeDisplay = porcentajeDisplay,
                    estadoColor = estadoColor,
                    colors = colors
                )

                Spacer(modifier = Modifier.height(24.dp))

                FooterActionSection(
                    faltasRestantes = faltasRestantes,
                    estadoColor = estadoColor,
                    estadoBgColor = estadoBgColor,
                    cuadritoColor = cuadritoColor,
                    cuadritoBgColor = cuadritoBgColor,
                    colors = colors
                )
            }
        }
    }
}

@Composable
private fun HeaderImageSection(
    imageUrl: String,
    nombre: String
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(130.dp)
    ) {
        AsyncImage(
            model = imageUrl,
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            Color.Transparent,
                            Color.Black.copy(alpha = 0.3f),
                            Color.Black.copy(alpha = 0.8f)
                        )
                    )
                )
        )

        Column(
            modifier = Modifier
                .align(Alignment.BottomStart)
                .padding(16.dp)
        ) {
            Text(
                text = "ASIGNATURA",
                color = Color.White,
                fontSize = 10.sp,
                fontWeight = FontWeight.ExtraBold,
                letterSpacing = 1.5.sp
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = nombre,
                color = Color.White,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                lineHeight = 22.sp
            )
        }
    }
}

@Composable
private fun AttendanceProgressSection(
    porcentajeInasistencia: Float,
    porcentajePermitido: Float,
    porcentajeDisplay: String,
    estadoColor: Color,
    colors: DarkModeColors
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = androidx.compose.foundation.layout.Arrangement.SpaceBetween,
            verticalAlignment = Alignment.Bottom
        ) {
            Text(
                text = "Inasistencia acumulada",
                color = colors.textColor.copy(alpha = 0.6f),
                fontSize = 13.sp,
                fontWeight = FontWeight.Medium
            )
            Row(verticalAlignment = Alignment.Bottom) {
                Text(
                    text = "$porcentajeDisplay%",
                    color = estadoColor,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.ExtraBold
                )
                Text(
                    text = " / ${porcentajePermitido.toInt()}% max",
                    color = colors.textColor.copy(alpha = 0.4f),
                    fontSize = 12.sp,
                    modifier = Modifier.padding(bottom = 2.dp, start = 2.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(6.dp)
                .clip(CircleShape)
                .background(colors.textColor.copy(alpha = 0.08f))
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth(fraction = (porcentajeInasistencia / 100f).coerceIn(0f, 1f))
                    .fillMaxHeight()
                    .clip(CircleShape)
                    .background(estadoColor)
            )
        }
    }
}

@Composable
private fun FooterActionSection(
    faltasRestantes: Int,
    estadoColor: Color,
    estadoBgColor: Color,
    cuadritoColor: Color,
    cuadritoBgColor: Color,
    colors: DarkModeColors
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = androidx.compose.foundation.layout.Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = androidx.compose.foundation.layout.Arrangement.spacedBy(10.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(42.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(cuadritoBgColor)
                    .border(1.dp, cuadritoColor.copy(alpha = 0.2f), RoundedCornerShape(12.dp)),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "$faltasRestantes",
                    color = cuadritoColor,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Black
                )
            }

            Column {
                Text(
                    text = "Faltas restantes",
                    color = colors.textColor.copy(alpha = 0.8f),
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        Surface(
            shape = CircleShape,
            color = Color.Transparent,
            border = BorderStroke(1.dp, colors.colorMixPrimary.copy(alpha = 0.3f))
        ) {
            Row(
                modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = androidx.compose.foundation.layout.Arrangement.spacedBy(2.dp)
            ) {
                Text(
                    text = "Detalle",
                    color = colors.colorMixPrimary,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold
                )
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                    contentDescription = null,
                    tint = colors.colorMixPrimary,
                    modifier = Modifier.size(16.dp)
                )
            }
        }
    }
}
