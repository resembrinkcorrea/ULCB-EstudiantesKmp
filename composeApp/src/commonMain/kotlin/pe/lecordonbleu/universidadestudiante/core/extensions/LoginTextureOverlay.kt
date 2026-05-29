package pe.lecordonbleu.universidadestudiante.core.extensions

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import coil3.compose.AsyncImage

@Composable
fun LoginTextureOverlay(
    imageUrl: String,
    modifier: Modifier = Modifier,
    tintColor: Color
) {
    AsyncImage(
        model = imageUrl,
        contentDescription = null,
        contentScale = ContentScale.Crop,
        colorFilter = ColorFilter.tint(tintColor, BlendMode.Multiply),
        modifier = modifier
            .graphicsLayer { alpha = 0.9f }
            .drawWithContent {
                drawContent()
                drawRect(
                    brush = Brush.verticalGradient(
                        colors = listOf(Color.Transparent, Color.Black),
                        startY = size.height * 0.35f,
                        endY = size.height
                    ),
                    blendMode = BlendMode.DstIn
                )
            }
    )
}
