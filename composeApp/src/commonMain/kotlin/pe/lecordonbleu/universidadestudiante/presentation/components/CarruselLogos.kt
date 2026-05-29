package pe.lecordonbleu.universidadestudiante.presentation.components

import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.graphicsLayer
import ulcbintranetkmp.composeapp.generated.resources.Res
import ulcbintranetkmp.composeapp.generated.resources.logo_ilcb_1
import ulcbintranetkmp.composeapp.generated.resources.logo_ilcb_2
import ulcbintranetkmp.composeapp.generated.resources.logo_ilcb_3
import kotlinx.coroutines.delay
import org.jetbrains.compose.resources.painterResource

@Composable
fun CarruselLogos(modifier: Modifier = Modifier) {
    val goldGradient = Brush.linearGradient(
        colors = listOf(Color(0xFFC5A059), Color(0xFFFFDFA1), Color(0xFF9B865C), Color(0xFFE2C57B))
    )

    val logosData = listOf(
        Triple(Res.drawable.logo_ilcb_1, Color.White, goldGradient),
        Triple(Res.drawable.logo_ilcb_2, Color.White, goldGradient),
        Triple(Res.drawable.logo_ilcb_3, Color.White, goldGradient),
    )

    val durations = listOf(5000L, 2500L, 2500L)
    var currentIndex by remember { mutableStateOf(0) }

    LaunchedEffect(currentIndex) {
        delay(durations[currentIndex])
        currentIndex = (currentIndex + 1) % logosData.size
    }

    Box(modifier = modifier, contentAlignment = Alignment.Center) {
        Crossfade(targetState = currentIndex, animationSpec = tween(800), label = "logo_fade") { index ->
            val currentLogo = logosData[index]
            Image(
                painter = painterResource(currentLogo.first),
                contentDescription = "Logo",
                modifier = Modifier
                    .fillMaxSize()
                    .graphicsLayer { alpha = 0.99f }
                    .drawWithCache {
                        onDrawWithContent {
                            drawContent()
                            currentLogo.third?.let { brush ->
                                drawRect(brush = brush, blendMode = BlendMode.SrcAtop)
                            }
                        }
                    },
                colorFilter = if (currentLogo.second != Color.Unspecified && currentLogo.third == null) {
                    ColorFilter.tint(currentLogo.second)
                } else null
            )
        }
    }
}
