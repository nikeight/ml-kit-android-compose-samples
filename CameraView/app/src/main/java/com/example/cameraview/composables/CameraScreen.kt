package com.example.cameraview.composables

import android.content.Context
import android.graphics.Rect
import android.net.Uri
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.view.CameraController
import androidx.camera.view.LifecycleCameraController
import androidx.camera.view.PreviewView
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.TextRecognizer
import com.google.mlkit.vision.text.latin.TextRecognizerOptions

@Composable
fun CameraScreen(
    modifier: Modifier = Modifier,
    applicationContext: Context,
    onResultFound: (String) -> Unit,
) {
    val cameraController = remember {
        LifecycleCameraController(applicationContext).apply {
            setEnabledUseCases(
                CameraController.IMAGE_ANALYSIS
            )
            cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA
            imageAnalysisBackpressureStrategy = ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST
        }
    }

    val textRecognizer: TextRecognizer = TextRecognition.getClient(
        TextRecognizerOptions.Builder().build()
    )

    var selectedImageUri by rememberSaveable { mutableStateOf<Uri?>(null) }
    var inputImage by remember { mutableStateOf<InputImage?>(null) }

    // Launcher to get Image Uri from Storage
    val photoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia(),
        onResult = { uri ->
            selectedImageUri = uri
        }
    )

    val lifecycleOwner = LocalLifecycleOwner.current
    var recognizedText by rememberSaveable { mutableStateOf<String?>(null) }
    var blockFrame by remember { mutableStateOf<Rect?>(null) }

    Scaffold(modifier = modifier.fillMaxSize()) { innerPadding ->
        Box(
            modifier = modifier
                .fillMaxSize()
                .padding(innerPadding),
            contentAlignment = Alignment.BottomCenter
        ) {
            AndroidView(
                factory = { context ->
                    PreviewView(context).apply {
                        layoutParams = LinearLayout.LayoutParams(
                            ViewGroup.LayoutParams.MATCH_PARENT,
                            ViewGroup.LayoutParams.MATCH_PARENT
                        )
                        implementationMode = PreviewView.ImplementationMode.COMPATIBLE
                        scaleType = PreviewView.ScaleType.FILL_START
                    }.also { previewView ->
                        cameraController.setImageAnalysisAnalyzer(
                            ContextCompat.getMainExecutor(context),
                            MyTextRecognizer(
                                textRecognizer = textRecognizer,
                                onResultFound = {
                                    it.textBlocks.forEach { block ->
                                        blockFrame = block.boundingBox
                                    }
                                    recognizedText = it.formatRecognizedText()
                                },
                            )
                        )
                        cameraController.bindToLifecycle(lifecycleOwner)
                        previewView.controller = cameraController
                    }
                },
                modifier = modifier
                    .fillMaxSize(),
            )

            Canvas(modifier = modifier.fillMaxSize()) {
                blockFrame?.let { rect ->
                    val topLeft = Offset(rect.left.toFloat(), rect.top.toFloat())
                    val size = Size(rect.width().toFloat(), rect.height().toFloat())

                    // Draw the rectangle
                    drawRect(
                        color = androidx.compose.ui.graphics.Color.Red,
                        topLeft = topLeft,
                        size = size
                    )
                }
            }

            AnimatedVisibility(visible = recognizedText.isNullOrEmpty().not()) {
                Column(
                    modifier
                        .fillMaxWidth()
                        .background(color = androidx.compose.ui.graphics.Color.White)
                        .padding(24.dp),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(text = "OutPut Text : $recognizedText")
                    Row(
                        modifier
                            .fillMaxWidth(0.9f)
                            .padding(8.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Button(onClick = {
                            photoPickerLauncher.launch(
                                PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                            )
                        }) {
                            Text(text = "Pick a Photo")
                        }

                        Button(onClick = {
                            photoPickerLauncher.launch(
                                PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                            )
                        }) {
                            Text(text = "Save to DB")
                        }
                    }
                }
            }
        }
    }
}

fun Text.formatRecognizedText(): String {
    val stringBuilder = buildString {
        textBlocks.forEach { blockText ->
            append("\n")
            append("Block : ${blockText.text}")
            blockText.lines.forEach { lineText ->
                append("\n")
                append("Line : ${lineText.text}")
                lineText.elements.forEach {
                    append("\n")
                    append("Element : ${it.text}")
                }
            }
        }
    }
    return stringBuilder
}