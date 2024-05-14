package ui.chart

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.ExperimentalTextApi
import androidx.compose.ui.text.TextMeasurer
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalTextApi::class)
fun DrawScope.textCanvas(
    x: Dp, y: Float, color: Color, textMeasurer: TextMeasurer, infoText: String,
) {
    val textStyle = TextStyle(
        fontSize = 16.sp,
        color = color,
        fontWeight = FontWeight.SemiBold,
        fontFamily = FontFamily.SansSerif
    )
    val textLayoutResult = textMeasurer.measure(
        text = AnnotatedString(infoText),
        style = textStyle,
        constraints = Constraints.fixedWidth(40.dp.toPx().toInt())
    )

    val textOffset = Offset(
        x.toPx() - textLayoutResult.size.width / 2,
        y - textLayoutResult.size.height / 2
    )

    drawText(
        textLayoutResult = textLayoutResult,
        brush = SolidColor(color),
        topLeft = textOffset
    )
}