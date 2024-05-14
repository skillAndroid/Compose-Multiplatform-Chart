package ui.chart

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.AnimationVector1D
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp

internal fun DrawScope.chartCircle(
    x: Float,
    y: Float,
    color: Color,
) {
    val circleRadius = 6.5.dp.toPx()
    val strokeWidth = 2.5.dp.toPx()


    // Draw circle fill
    drawCircle(
        color = color,
        radius = circleRadius,
        center = Offset(x, y)
    )

    // Draw white border
    drawCircle(
        color = Color.Black,
        radius = circleRadius,
        center = Offset(x, y),
        style = Stroke(width = strokeWidth),
        blendMode = BlendMode.Darken
    )
}