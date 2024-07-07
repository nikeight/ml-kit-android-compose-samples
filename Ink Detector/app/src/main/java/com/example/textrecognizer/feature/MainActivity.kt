package com.example.textrecognizer.feature

import android.os.Bundle
import android.view.MotionEvent
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInteropFilter
import androidx.compose.ui.unit.dp
import com.example.textrecognizer.ui.theme.TextRecognizerTheme
import com.google.mlkit.vision.digitalink.Ink

class MainActivity : ComponentActivity() {

    @OptIn(ExperimentalComposeUiApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            TextRecognizerTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Column(
                        Modifier
                            .fillMaxSize()
                            .padding(innerPadding)
                    ) {
                        // viewModel
                        val viewModel: MainViewModel by viewModels<MainViewModel>()

                        // Ink- Detection Support
                        val inkBuilder = Ink.builder()
                        var strokeBuilder = Ink.Stroke.builder()

                        // Draw on Canvas variables
                        var motionEvent by remember { mutableStateOf(CurrentMotionEvent.IDLE) }
                        var currentPosition by remember { mutableStateOf(Offset.Unspecified) }
                        var previousPosition by remember { mutableStateOf(Offset.Unspecified) }
                        val currentPath by remember { mutableStateOf(Path()) }
                        val paint = remember {
                            Paint().apply {
                                color = Color.Black
                            }
                        }

                        Canvas(
                            modifier = Modifier
                                .fillMaxSize()
                                .weight(1f)
                                .padding(innerPadding)
                                .pointerInteropFilter { event ->
                                    val x = event.x
                                    val y = event.y
                                    val t = System.currentTimeMillis()

                                    when (event.action) {
                                        MotionEvent.ACTION_DOWN -> {
                                            // set values
                                            motionEvent = CurrentMotionEvent.DOWN
                                            currentPosition = Offset(x, y)

                                            // Set Ink Values
                                            strokeBuilder = Ink.Stroke.builder()
                                            strokeBuilder.addPoint(Ink.Point.create(x, y, t))
                                            event.printCoordinates("MOTION_DOWN")
                                        }

                                        MotionEvent.ACTION_UP -> {
                                            // set values
                                            motionEvent = CurrentMotionEvent.UP

                                            // Set INK Value
                                            strokeBuilder.addPoint(Ink.Point.create(x, y, t))
                                            event.printCoordinates("MOTION_UP")
                                        }

                                        MotionEvent.ACTION_MOVE -> {
                                            // SET Values
                                            motionEvent = CurrentMotionEvent.MOVE
                                            currentPosition = Offset(x, y)

                                            // INK SETUP
                                            strokeBuilder.addPoint(Ink.Point.create(x, y, t))
                                            inkBuilder.addStroke(strokeBuilder.build())
                                            event.printCoordinates("MOTION_MOVE")
                                        }

                                        else -> {
                                            event.printCoordinates("Else")
                                        }
                                    }
                                    true
                                }
                        ) {
                            // Draw Lines here
                            when (motionEvent) {
                                CurrentMotionEvent.IDLE -> {
                                    // do Nothing
                                }

                                CurrentMotionEvent.DOWN -> {
                                    // Take the Cursor to the tap area
                                    currentPath.moveTo(currentPosition.x, currentPosition.y)
                                    previousPosition = currentPosition
                                }

                                CurrentMotionEvent.MOVE -> {
                                    if (previousPosition != Offset.Unspecified) {
                                        currentPath.quadraticBezierTo(
                                            previousPosition.x,
                                            previousPosition.y,
                                            (previousPosition.x + currentPosition.x) / 2,
                                            (previousPosition.y + currentPosition.y) / 2

                                        )
                                        previousPosition = currentPosition
                                    }
                                }

                                CurrentMotionEvent.UP -> {
                                    currentPath.lineTo(currentPosition.x, currentPosition.y)
                                    currentPosition = Offset.Unspecified
                                    previousPosition = currentPosition
                                    motionEvent = CurrentMotionEvent.IDLE
                                }
                            }

                            drawPath(
                                currentPath, color = paint.color, style = Stroke(
                                    width = 4f,
                                    cap = Stroke.DefaultCap,
                                    join = StrokeJoin.Round
                                )
                            )
                        }

                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .align(Alignment.CenterHorizontally),
                            shape = RoundedCornerShape(corner = CornerSize(24.dp)),
                        ) {
                            Row(
                                modifier =
                                Modifier
                                    .fillMaxWidth()
                                    .padding(12.dp),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Button(onClick = { viewModel.recognizeTheInk(inkBuilder.build()) }) {
                                    Text(text = "Recognize")
                                }

                                Button(onClick = { /*TODO*/ }) {
                                    Text(text = "Save to DB")
                                }

                                Button(onClick = { /*TODO*/ }) {
                                    Text(text = "Clear Board")
                                }
                            }

                            Text(
                                modifier = Modifier.padding(8.dp),
                                text = "Recognized Text"
                            )
                            Text(
                                modifier = Modifier.padding(8.dp),
                                text = viewModel.recognizedText
                            )
                        }
                    }
                }
            }
        }
    }
}

fun MotionEvent.printCoordinates(motionType: String) {
    println("MOTION-EVENT : $motionType block called with X : $x, Y : $y, T: ${System.currentTimeMillis()}")
}

enum class CurrentMotionEvent {
    IDLE,
    UP,
    DOWN,
    MOVE
}