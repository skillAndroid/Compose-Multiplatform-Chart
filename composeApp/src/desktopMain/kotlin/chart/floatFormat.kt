package ui.chart

import kotlin.math.abs


internal fun Float.formatWithSuffix(): String {
    val absValue = abs(this)
    return when {
        absValue < 1_000 -> String.format("%.0f", this)
        absValue < 1_000_000 -> String.format("%.1fK", this / 1_000)
        absValue < 1_000_000_000 -> String.format("%.1fM", this / 1_000_000)
        absValue < 1_000_000_000_000 -> String.format("%.1fB", this / 1_000_000_000)
        absValue < 1_000_000_000_000_000 -> String.format("%.1fT", this / 1_000_000_000_000)
        else -> "Infinity"
    }
}