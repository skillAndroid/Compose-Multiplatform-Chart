package ui.chart

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.PathMeasure
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import primaryColor
import silverColor
import timerBg
import kotlin.math.max
import kotlin.math.min


@Composable
fun CurvedChart(
    dataPoints: List<Double>,
    horizontalLabels: List<String>,
    lineColor: Color = primaryColor.copy(0.85f),
    lineShadow: Boolean = true,
    modifier: Modifier = Modifier,
    animateChart: Boolean = true
) {
    val textMeasurer = rememberTextMeasurer()
    val originalMaxY = dataPoints.maxOrNull() ?: 1.0
    val maxY = originalMaxY * 1.10  // Set maxY to 110% of the maximum value in dataPoints

    val minY = dataPoints.minOrNull() ?: 0.0

    val rangeY = maxY - minY
    val interval = rangeY / 4  // Divide the range into 5 equal parts

    val animatedProgress = remember {
        Animatable(0f)
    }

    var tooltipPosition by remember { mutableStateOf<Offset?>(null) }
    var tooltipValue by remember { mutableStateOf<Double?>(null) }

    val pointSpacing = 50.dp
    val chartWidth = pointSpacing * (dataPoints.size - 1)

    LaunchedEffect(key1 = true) {
        if (animateChart) {
            animatedProgress.animateTo(
                targetValue = 1f,
                animationSpec = tween(durationMillis = 1000, easing = LinearEasing)
            )
        }
    }

    val detectTapGestures = Modifier.pointerInput(Unit) {
        detectTapGestures { offset ->
            val marginLeft2 =
                40.dp.toPx()
            val width2 = size.width
            val height2 = size.height
            val marginTop2 = 30.dp.toPx()  // Increased top margin
            val gridWidth = width2 - marginLeft2
            val stepX = gridWidth / max(1, dataPoints.size - 1)
            val marginBottom2 = 30.dp.toPx()
            // Calculate the nearest data point index to the tapped x position
            val index =
                (offset.x / (size.width / dataPoints.size)).toInt().coerceIn(dataPoints.indices)
            // Get the exact position of the nearest data point on the path
            val usableHeight2 = height2 - marginTop2 - marginBottom2
            val exactPositionOnPath = Offset(
                x = marginLeft2 + index * stepX,
                y = (marginTop2 + (1 - (dataPoints[index] - minY) / rangeY) * usableHeight2).toFloat()
            )
            tooltipPosition = exactPositionOnPath
            tooltipValue = dataPoints[index]
        }
    }
    val scrollState = rememberScrollState()
    val dragStartOffset = remember { mutableStateOf(0f) }
    val dragAmount = remember { mutableStateOf(0f) }
    val scope = rememberCoroutineScope()
    // Sensitivity factor - adjust this value to make scrolling more or less sensitive
    val sensitivity = 1f  // Increasing this number increases sensitivity

    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Draw horizontal labels in a column
        Column(
            modifier = Modifier.fillMaxHeight().width(50.dp)
                .padding(start = 12.dp, bottom = 52.dp, top = 20.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            repeat(5) { index ->
                val labelValue = (maxY - index * interval).toFloat().formatWithSuffix()
                Text(
                    text = labelValue,
                    color = Color.Black.copy(0.4f),
                    fontWeight = FontWeight.SemiBold,
                    fontFamily = FontFamily.SansSerif
                )
            }
        }
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(scrollState)
                .pointerInput(Unit) {
                    detectDragGestures(onDragStart = { pointerInputChange ->
                        dragStartOffset.value = pointerInputChange.x
                    }, onDrag = { change, dragAmount ->
                        val delta = (dragStartOffset.value - change.position.x) * sensitivity
                        dragStartOffset.value = change.position.x
                        val newScrollPosition =
                            (scrollState.value + delta).coerceIn(0f, scrollState.maxValue.toFloat())
                        scope.launch {
                            scrollState.scrollTo(newScrollPosition.toInt())
                        }

                    })
                }
                .fillMaxHeight()
        ) {
            Canvas(
                modifier = Modifier
                    .width(chartWidth)  // Ensure this width is significantly larger than the screen width
                    .fillMaxHeight().padding(bottom = 30.dp, end = 16.dp).offset(x = -32.dp)
                    .then(detectTapGestures) // Match height with Box
            ) {
                val width = size.width
                val height = size.height
                val marginTop = 30.dp.toPx()  // Increased top margin
                val marginBottom = 30.dp.toPx()  // Increased bottom margin
                val marginRight = 10.dp.toPx()  // Optional right margin

                val marginLeft =
                    40.dp.toPx() // Calculate marginLeft based on available width and any other margins

                val usableHeight = height - marginTop - marginBottom

                val gridWidth = width - marginLeft
                val stepX = gridWidth / max(1, dataPoints.size - 1)

                // Define the curve path for stroke
                val curvePath = Path().apply {
                    val scaledPoints = dataPoints.mapIndexed { index, dataPoint ->
                        Offset(
                            x = marginLeft + index * stepX,
                            y = (marginTop + (1 - (dataPoint - minY) / rangeY) * usableHeight).toFloat()
                        )
                    }
                    moveTo(scaledPoints.first().x, scaledPoints.first().y)
                    if (scaledPoints.size > 1) {
                        var currentPoint = scaledPoints.first()
                        for (i in 1 until scaledPoints.size) {
                            val nextPoint = scaledPoints[i]
                            val nextNextPoint =
                                if (i < scaledPoints.size - 1) scaledPoints[i + 1] else nextPoint
                            val controlPoint1 =
                                Offset((currentPoint.x + nextPoint.x) / 2, currentPoint.y)
                            val controlPoint2 =
                                Offset((currentPoint.x + nextPoint.x) / 2, nextPoint.y)
                            cubicTo(
                                controlPoint1.x,
                                controlPoint1.y,
                                controlPoint2.x,
                                controlPoint2.y,
                                nextPoint.x,
                                nextPoint.y
                            )
                            currentPoint = nextPoint
                        }
                    }
                }
                // Animate drawing the curve
                val pathMeasure = PathMeasure()
                pathMeasure.setPath(curvePath, false)
                val pathLength = pathMeasure.length
                val animatedPath = Path().also {
                    val stop = pathLength * animatedProgress.value
                    pathMeasure.getSegment(0f, stop, it, true)
                }

                drawPath(
                    path = animatedPath,
                    color = lineColor,
                    style = Stroke(width = 3.dp.toPx(), cap = StrokeCap.Round)
                )

                // Optionally draw shadow if needed
                if (lineShadow) {
                    val fillPath = Path().apply {
                        addPath(animatedPath)
                        lineTo(animatedPath.getBounds().right, height)
                        lineTo(animatedPath.getBounds().left, height)
                        close()
                    }
                    drawPath(
                        path = fillPath,
                        brush = Brush.verticalGradient(
                            colors = listOf(
                                lineColor.copy(alpha = 0.7f),
                                Color.Transparent,
                            ),
                            startY = 0f,
                            endY = height
                        )
                    )
                }

                // Draw circles at each data point with animation
                dataPoints.forEachIndexed { index, dataPoint ->
                    val circlePosition = Offset(
                        x = marginLeft + index * stepX,
                        y = (marginTop + (1 - (dataPoint - minY) / rangeY) * usableHeight).toFloat()
                    )
                    val pathEffect = PathMeasure()
                    pathEffect.setPath(curvePath, false)
                    val pointOnPathDistance =
                        pathEffect.length * index.toFloat() / max(1, dataPoints.size - 1)
                    val visibility =
                        if (animatedProgress.value * pathEffect.length >= pointOnPathDistance) 1f else 0f
                    val scale = min(1f, visibility * 2)  // Scale up to 1 as it becomes visible

                   /* drawCircle(
                        color = lineColor.copy(alpha = visibility),  // Animate the opacity
                        radius = 3.dp.toPx() * scale,  // Animate the scale
                        center = circlePosition
                    )*/
                }


                // Draw 5 horizontal grid lines and labels at equal intervals
                for (i in 0..4) {
                    val verticalPosition = maxY - i * interval
                    val yPos = marginTop + (1 - (verticalPosition - minY) / rangeY) * usableHeight

                    /*  // Format label with suffix and draw it
                      val label = verticalPosition.toFloat().formatWithSuffix()
                      textCanvas(
                          x = 0.dp,
                          y = yPos.toFloat(),
                          color = Color.Gray.copy(0.85f),
                          textMeasurer = textMeasurer,
                          infoText = label
                      )*/

                    // Draw horizontal grid line for each interval
                    drawLine(
                        color = primaryColor.copy(alpha = 0.15f),  // Light gray color for the grid line
                        start = Offset(marginLeft, yPos.toFloat()),
                        end = Offset(width, yPos.toFloat()),
                        strokeWidth = 1.dp.toPx(),
                        pathEffect = PathEffect.dashPathEffect(
                            floatArrayOf(
                                10f,
                                5f
                            )
                        )  // Dashed effect
                    )
                }

                // Draw horizontal labels below the chart
                horizontalLabels.forEachIndexed { index, label ->
                    textCanvas(
                        x = 15.dp + marginLeft.toDp() + index.toDp() * stepX,  // Center text horizontally under each data point
                        y = height,
                        color = if (label == "11") primaryColor else silverColor,
                        textMeasurer = textMeasurer,
                        infoText = label
                    )
                }

                // Draw tooltip if position and value are not null
                tooltipPosition?.let { position ->
                    tooltipValue?.let { value ->
                        circleWithRectAndText(
                            textMeasure = textMeasurer,
                            info = value,
                            x = position.x.toDp(),
                            y = position.y.toDouble()
                        )
                    }

                    // Draw vertical grid line at the selected location
                    drawLine(
                        color = lineColor.copy(alpha = 0.2f),  // Adjust the color and transparency
                        start = Offset(
                            x = position.x,
                            y = height - marginBottom
                        ),  // Start from the bottom of the chart area
                        end = Offset(
                            x = position.x,
                            y = position.y
                        ),  // Extend to the height of the tooltip on the path
                        strokeWidth = 1.dp.toPx(),  // Adjust thickness of the line
                        pathEffect = PathEffect.dashPathEffect(
                            floatArrayOf(10f, 5f),
                            0f
                        )  // Optional: dashed effect
                    )
                }

                tooltipPosition?.let { position ->
                    tooltipValue?.let { value ->
                        circleWithRectAndText(
                            textMeasure = textMeasurer,
                            info = value,
                            x = position.x.toDp(),
                            y = position.y.toDouble()
                        )
                    }
                }

            }
        }
    }

}










