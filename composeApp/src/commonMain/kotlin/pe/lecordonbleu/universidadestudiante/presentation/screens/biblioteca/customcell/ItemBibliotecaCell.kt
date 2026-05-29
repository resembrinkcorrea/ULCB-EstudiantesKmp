package pe.lecordonbleu.universidadestudiante.presentation.screens.biblioteca.customcell

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Link
import androidx.compose.material.icons.outlined.OpenInNew
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import pe.lecordonbleu.universidadestudiante.DarkModeColors
import pe.lecordonbleu.universidadestudiante.data.remote.dto.LinksBiblioteca

@Composable
fun ItemBibliotecaCell(
    item: LinksBiblioteca,
    colors: DarkModeColors,
    onClick: () -> Unit
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(horizontal = 2.dp),
        shape = RoundedCornerShape(20.dp),
        color = colors.colorBlancoGris,
        tonalElevation = 0.dp,
        shadowElevation = 1.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.Top,
            horizontalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(72.dp)
                    .background(
                        color = colors.colorMixPrimary.copy(alpha = 0.10f),
                        shape = RoundedCornerShape(18.dp)
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Link,
                    contentDescription = null,
                    tint = colors.colorMixPrimary,
                    modifier = Modifier.size(34.dp)
                )
            }

            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Text(
                    text = item.nombre_biblioteca_det,
                    style = MaterialTheme.typography.titleMedium,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = colors.textColor,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
                if (item.descri_biblioteca_det.isNotEmpty() &&
                    item.descri_biblioteca_det.uppercase() != "NULL"
                ) {
                    Text(
                        text = item.descri_biblioteca_det,
                        style = MaterialTheme.typography.bodyLarge,
                        color = colors.textColor.copy(alpha = 0.62f),
                        maxLines = 3,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }

            Spacer(modifier = Modifier.size(2.dp))

            Icon(
                imageVector = Icons.Outlined.OpenInNew,
                contentDescription = null,
                tint = colors.colorMixPrimary,
                modifier = Modifier
                    .padding(top = 6.dp)
                    .size(34.dp)
            )
        }
    }
}
