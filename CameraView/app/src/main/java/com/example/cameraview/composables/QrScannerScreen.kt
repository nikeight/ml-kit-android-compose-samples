package com.example.cameraview.composables

import android.app.Application
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.OptIn
import androidx.camera.core.CameraSelector
import androidx.camera.core.ExperimentalGetImage
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageAnalysis.COORDINATE_SYSTEM_VIEW_REFERENCED
import androidx.camera.core.Preview
import androidx.camera.mlkit.vision.MlKitAnalyzer
import androidx.camera.view.CameraController
import androidx.camera.view.LifecycleCameraController
import androidx.camera.view.PreviewView
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.mlkit.vision.common.InputImage
import java.io.IOException


@OptIn(ExperimentalGetImage::class)
@Composable
fun QRScannerScreen(
    modifier: Modifier = Modifier,
    qrScreenViewModel: QrScreenViewModel = hiltViewModel<QrScreenViewModel>(),
    onSuccessBarCodeAnalysis: (String) -> Unit,
    applicationContext: Application
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

    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    var selectedImageUri by rememberSaveable { mutableStateOf<Uri?>(null) }
    var inputImage by remember { mutableStateOf<InputImage?>(null) }

    // Launcher to get Image Uri from Storage
    val photoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia(),
        onResult = { uri ->
            selectedImageUri = uri
        }
    )

    LaunchedEffect(selectedImageUri) {
        selectedImageUri?.let { uri ->
            try {
                inputImage = InputImage.fromFilePath(context, uri)
            } catch (e: IOException) {
                e.printStackTrace()
            }

            qrScreenViewModel.processPhotoImageProxy(
                mediaImage = inputImage,
                onSuccess = { barcodes ->
                    onSuccessBarCodeAnalysis(barcodes.first().rawValue.toString())
                }
            )
        }

        selectedImageUri = null
    }

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
                        // Preview : Set Surface for Process
                        val preview: Preview = Preview.Builder().build()
                        preview.setSurfaceProvider(this.surfaceProvider)
                        this.controller = cameraController
                        cameraController.bindToLifecycle(lifecycleOwner)

                        cameraController.setImageAnalysisAnalyzer(
                            ContextCompat.getMainExecutor(context),
                            MlKitAnalyzer(
                                listOf(qrScreenViewModel.getBarCodeClient()),
                                COORDINATE_SYSTEM_VIEW_REFERENCED,
                                ContextCompat.getMainExecutor(context)
                            ) { result: MlKitAnalyzer.Result? ->
                                qrScreenViewModel.processBarCodeImgAnalysisResult(
                                    result = result,
                                    noResultFound = {

                                    },
                                    successResult = { uniqueId ->
                                        onSuccessBarCodeAnalysis(uniqueId)
                                    }
                                )
                            }
                        )
                    }
                },
                modifier = modifier
                    .fillMaxSize(),
            )

            Row(
                modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Absolute.SpaceBetween
            ) {
                Button(onClick = {
                    photoPickerLauncher.launch(
                        PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                    )
                }) {
                    Text(text = "Gallery")
                }
            }
        }
    }
}