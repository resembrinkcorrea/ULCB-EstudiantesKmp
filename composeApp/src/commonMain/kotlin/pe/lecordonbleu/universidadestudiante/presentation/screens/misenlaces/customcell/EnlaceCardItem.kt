package pe.lecordonbleu.universidadestudiante.presentation.screens.misenlaces.customcell

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import pe.lecordonbleu.universidadestudiante.data.remote.dto.ListLinksInstitucional
import pe.lecordonbleu.universidadestudiante.getColorsTheme

@Composable
fun EnlaceCardItem(
    item: ListLinksInstitucional,
    onClick: (String) -> Unit
) {
    val colors = getColorsTheme()

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(0.55f)
            .clickable { onClick(item.plat_web_cab_url) },
        shape = RoundedCornerShape(8.dp),
        elevation = 4.dp,
        backgroundColor = colors.backGroundColor
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            AsyncImage(
                model = item.plat_web_cab_imagen,
                contentDescription = item.plat_web_cab_nombre,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            )
            Text(
                text = item.plat_web_cab_abrev,
                fontSize = 10.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                color = colors.textColor,
                modifier = Modifier
                    .padding(horizontal = 4.dp, vertical = 6.dp)
            )
        }
    }
}
