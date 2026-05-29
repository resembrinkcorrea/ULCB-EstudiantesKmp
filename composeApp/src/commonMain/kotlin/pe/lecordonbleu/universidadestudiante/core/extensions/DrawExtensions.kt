package pe.lecordonbleu.universidadestudiante.core.extensions

import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path

fun Modifier.drawFoodAppBackground(
    topColor: Color,
    bottomColor: Color,
    blueHeightRatio: Float
): Modifier = this.drawBehind {
    val topHeight = size.height * blueHeightRatio

    drawRect(color = bottomColor, size = size)

    val path = Path().apply {
        moveTo(0f, 0f)
        lineTo(0f, topHeight)
        cubicTo(
            size.width * 0.25f, topHeight + 80f,
            size.width * 0.75f, topHeight - 80f,
            size.width, topHeight
        )
        lineTo(size.width, 0f)
        close()
    }

    drawPath(
        path = path,
        brush = Brush.verticalGradient(
            colors = listOf(topColor, topColor.copy(alpha = 0.85f))
        )
    )
}
