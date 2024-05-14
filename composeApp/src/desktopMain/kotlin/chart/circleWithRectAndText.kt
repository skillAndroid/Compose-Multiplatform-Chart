package ui.chart

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.AnimationVector1D
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.RoundRect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.ExperimentalTextApi
import androidx.compose.ui.text.TextMeasurer
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import primaryColor


@OptIn(ExperimentalTextApi::class)
internal fun DrawScope.circleWithRectAndText(
    textMeasure: TextMeasurer,
    info: Double,
    x: Dp,
    y: Double,
) {
    chartCircle(x.toPx(), y.toFloat(), primaryColor)
    chartRectangleWithText(x, y, primaryColor, textMeasure, info)
}

@OptIn(ExperimentalTextApi::class)
private fun DrawScope.chartRectangleWithText(
    x: Dp, y: Double, color: Color, textMeasurer: TextMeasurer, infoText: Double,
) {
    val rectSize = Size(70.dp.toPx(), 40.dp.toPx())
    val verticalAdjustment = 20.dp.toPx()  // Adjust to move the bubble up or down
    val rectTopLeft = Offset(
        x.toPx() - rectSize.width / 2,
        y.toFloat() - rectSize.height - verticalAdjustment
    )

    val tailWidth = 15.dp.toPx()
    val tailHeight = 10.dp.toPx()
    val tailCenter = Offset(rectTopLeft.x + rectSize.width / 2, rectTopLeft.y + rectSize.height)


    val path = Path().apply {
        addRoundRect(
            RoundRect(
                rect = Rect(
                    offset = rectTopLeft,
                    size = rectSize
                ),
                topLeft = CornerRadius(8.dp.toPx()),
                topRight = CornerRadius(8.dp.toPx()),
                bottomLeft = CornerRadius(8.dp.toPx()),
                bottomRight = CornerRadius(8.dp.toPx())
            )
        )
        moveTo(tailCenter.x, tailCenter.y)
        lineTo(tailCenter.x - tailWidth / 2, tailCenter.y)
        lineTo(tailCenter.x, tailCenter.y + tailHeight)
        lineTo(tailCenter.x + tailWidth / 2, tailCenter.y)
        close()
    }

    drawPath(
        path = path,
        color = color
    )

    val text = infoText.toFloat().formatWithSuffix()
    val textStyle = TextStyle(fontSize = 14.sp, color = Color.White, fontWeight = FontWeight.Bold)
    val textLayoutResult = textMeasurer.measure(
        text = AnnotatedString(text),
        style = textStyle,
        constraints = Constraints(
            maxWidth = rectSize.width.toInt(),
            maxHeight = rectSize.height.toInt()
        )
    )

    val textOffset = Offset(
        rectTopLeft.x + (rectSize.width - textLayoutResult.size.width) / 2,
        rectTopLeft.y + (rectSize.height - textLayoutResult.size.height) / 2
    )

    drawText(
        textLayoutResult = textLayoutResult,
        brush = SolidColor(Color.White),
        topLeft = textOffset
    )
}

