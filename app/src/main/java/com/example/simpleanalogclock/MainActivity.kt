package com.example.simpleanalogclock

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.*
import com.example.simpleanalogclock.ui.theme.SimpleAnalogClockTheme
import java.text.SimpleDateFormat
import java.util.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.drawscope.rotate
import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.TimeUnit

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SimpleAnalogClockTheme {
                // A surface container using the 'background' color from the theme
                Surface(color = MaterialTheme.colors.background) {
                    Greeting()
                }
            }
        }
    }
}

@Composable
fun Greeting() {
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxHeight()
            .fillMaxWidth()
    ) {
        AnalogClock()
        DigitalClock()
    }

}

@Composable
fun DigitalClock() {
    var time by remember {
        mutableStateOf("")
    }

    DisposableEffect(key1 = 0) {

        var calendar = Calendar.getInstance()

        val observable =
            Observable.interval(1, TimeUnit.SECONDS)
                .subscribeOn(Schedulers.io())
                .subscribe {
                    calendar = Calendar.getInstance()

                    time = SimpleDateFormat("hh:mm:ss a").format(calendar.time)
                }
        onDispose {
            observable.dispose()
        }


    }

    Text(
        text = "$time",
        style = TextStyle(
            fontWeight = FontWeight.Bold,
            fontFamily = FontFamily.Default,
            fontSize = 30.sp
        )
    )
}

@Preview(showBackground = true)
@Composable
fun AnalogClock() {
    Box(
        modifier = Modifier.size(350.dp),
        contentAlignment = Alignment.Center
    ) {
        var time by remember {
            mutableStateOf(
                Time(
                    hour = 0f,
                    minute = 0f,
                    second = 0f
                )
            )
        }

        DisposableEffect(key1 = 0) {

            var calendar = Calendar.getInstance()
            val observable = Observable.interval(1, TimeUnit.SECONDS)
                .subscribeOn(Schedulers.io())
                .subscribe {
                    calendar = Calendar.getInstance()

                    val hour = calendar.get(Calendar.HOUR)
                    val minute = calendar.get(Calendar.MINUTE)
                    val second = calendar.get(Calendar.SECOND)

                    time = Time(
                        hour = ((hour + (minute / 60f)) * 6f * 5),
                        minute = minute * 6f,
                        second = second * 6f
                    )
                }

            onDispose {
                observable.dispose()
            }
        }

        Canvas(modifier = Modifier.size(250.dp)) {

            for (angle in 0..60) {
                rotate(angle * 6f) {
                    if (angle % 5 == 0) {
                        drawLine(
                            color = Color.Black,
                            start = Offset(size.width / 2, 0f),
                            end = Offset(size.width / 2, 40f),
                            strokeWidth = 4f
                        )
                    } else {
                        drawLine(
                            color = Color.Black,
                            start = Offset(size.width / 2, 15f),
                            end = Offset(size.width / 2, 25f),
                            strokeWidth = 4f
                        )
                    }
                }
            }

            rotate(time.hour) {
                drawLine(
                    color = Color.Black ,
                    start = Offset(size.width / 2 , size.width / 2),
                    end = Offset(size.width / 2 , 200f),
                    strokeWidth = 14f
                )
            }

            rotate(time.minute) {
                drawLine(
                    color = Color.Black ,
                    start = Offset(size.width / 2 , size.width / 2 + 40),
                    end = Offset(size.width / 2 , 75f),
                    strokeWidth = 10f
                )
            }

            rotate(time.second) {
                drawLine(
                    color = Color.Red,
                    start = Offset(size.width / 2 , size.width / 2),
                    end = Offset(size.width / 2 , 75f),
                    strokeWidth = 6f
                )
            }

            drawCircle(
                color = Color.Black ,
                radius = 20f
            )
        }
    }
}

data class Time(
    var hour: Float,
    var minute: Float,
    var second: Float
)
