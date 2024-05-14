package ui.chart

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun QobilChart(modifier: Modifier) {
    CurvedChart(
        listOf(
            2000.3,
            2200.5,
            2500.0,
            2000.4,
            2500.0,
            3000.3,
            3200.5,
            2300.0,
            2200.0,
            3000.0,
            3400.0,
            2700.0,
            2000.3,
            2200.5,
            2500.0,
            2000.4,
            2500.0,
            3000.3,
            3200.5,
            2300.0,
            2200.0,
            3000.0,
            3400.0,
            2700.0
        ),
        listOf(
            "1",
            "2",
            "3",
            "4",
            "5",
            "6",
            "7",
            "8",
            "9",
            "10",
            "11",
            "12",
            "13",
            "14",
            "15",
            "16",
            "17",
            "18",
            "19",
            "20",
            "21",
            "22",
            "23",
            "24"
        ),
        modifier = modifier
    )
}