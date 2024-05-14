import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import ui.chart.QobilChart


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StatisticsScreen() {

    val scope = rememberCoroutineScope()

    var show by remember { mutableStateOf(false) }
    var show2 by remember { mutableStateOf(false) }
    var show3 by remember { mutableStateOf(false) }
    scope.launch {
        delay(100)
        show = true
    }
    val scrollState = rememberScrollState()


    if (show) {
        scope.launch {
           delay(300)
            show2 = true
        }
        if(show2) {
            scope.launch {
                delay(100)
                show3 = true
            }
            if (show3){
                Row(modifier = Modifier.fillMaxSize().padding(bottom = 16.dp)) {
                    Column(
                        modifier = Modifier.fillMaxHeight().fillMaxWidth(0.65f)
                            .padding(horizontal = 8.dp)
                    ) {
                        ChartScreen()
                    }

                }
            }

        }

    }


}


// You can customise the code )
@Composable
fun ChartScreen() {
    Row(modifier = Modifier.fillMaxWidth()) {
        Spacer(modifier = Modifier.width(30.dp))
        Card(
            Modifier.width(750.dp).fillMaxHeight(0.5f).shadow(
                elevation = 20.dp,//12.dp,
                shape = RoundedCornerShape(8.dp),
                clip = true,
                spotColor = Color.LightGray.copy(0.3f),
            ),
            shape = RoundedCornerShape(8.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White)
        ) {
            Box(modifier = Modifier.fillMaxSize()) {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth()
                            .padding(start = 16.dp, top = 8.dp, end = 8.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "Sales Analytics",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.SemiBold,
                            fontFamily = FontFamily.SansSerif,
                            color = ddinColor.copy(0.9f)
                        )
                        Card(
                            shape = RoundedCornerShape(8.dp),
                            border = BorderStroke(
                                width = 0.3.dp, color = primaryColor.copy(0.13f)
                            ),
                            colors = CardDefaults.cardColors(containerColor = Color.White),
                            modifier = Modifier.width(100.dp).height(30.dp)
                        ) {
                            Row(
                                modifier = Modifier.fillMaxSize()
                                    .padding(horizontal = 8.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    "This Month",
                                    fontSize = 12.sp,
                                    color = Color.DarkGray.copy(0.9f)
                                )
                                Spacer(modifier = Modifier.width(2.dp))
                                Icon(
                                    imageVector = Icons.Default.KeyboardArrowDown,
                                    null,
                                    modifier = Modifier.size(24.dp),
                                    tint = Color.DarkGray
                                )
                            }
                        }
                    }

                    QobilChart(
                        Modifier.width(900.dp).height(350.dp).padding(horizontal = 8.dp)
                    )
                }
            }

        }
    }
}











