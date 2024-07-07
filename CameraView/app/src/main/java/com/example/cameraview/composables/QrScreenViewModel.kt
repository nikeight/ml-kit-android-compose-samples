package com.example.cameraview.composables

import androidx.annotation.OptIn
import androidx.camera.core.ExperimentalGetImage
import androidx.camera.core.ImageProxy
import androidx.camera.mlkit.vision.MlKitAnalyzer
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.cameraview.data.Repository
import com.google.mlkit.vision.barcode.BarcodeScanner
import com.google.mlkit.vision.barcode.common.Barcode
import com.google.mlkit.vision.common.InputImage
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class QrScreenViewModel @Inject constructor(
    private val repository: Repository,
    private val barcodeScanner: BarcodeScanner
) : ViewModel() {

    val fetchedId = mutableStateOf("ID_FROM_QR_CODE")

    init {
        repository.saveIdAndShowMessage()
        repository.retrieveDataAndCloseConnection(fetchedId.value)
    }

    fun getBarCodeClient(): BarcodeScanner {
        return barcodeScanner
    }

    @OptIn(ExperimentalGetImage::class)
    fun processAnalysisImageProxy(
        imageProxy: ImageProxy,
        onSuccess: (List<Barcode>) -> Unit
    ) {
        // Convert from ImageProxy to MediaImage for Analysis
        val inputMediaImage = imageProxy.image?.let { imageCaptured ->
            InputImage.fromMediaImage(
                imageCaptured,
                imageProxy.imageInfo.rotationDegrees,
            )
        }

        try {
            inputMediaImage?.let { safeMediaImage ->
                barcodeScanner.process(safeMediaImage)
                    .addOnSuccessListener {
                        onSuccess(it)
                    }.addOnFailureListener {
                        it.printStackTrace()
                    }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            imageProxy.close()
        }
    }

    fun processPhotoImageProxy(
        mediaImage: InputImage?,
        onSuccess: (List<Barcode>) -> Unit
    ) {
        try {
            mediaImage?.let { safeMediaImage ->
                barcodeScanner.process(safeMediaImage)
                    .addOnSuccessListener { barcodeList ->
                        onSuccess(barcodeList)
                    }.addOnFailureListener {
                        it.printStackTrace()
                    }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun processBarCodeImgAnalysisResult(
        result: MlKitAnalyzer.Result?,
        noResultFound: () -> Unit,
        successResult: (String) -> Unit,
    ) {
        val barcodeResults = result?.getValue(barcodeScanner)
        if ((barcodeResults == null) ||
            (barcodeResults.size == 0) ||
            (barcodeResults.first() == null)
        ) {
            noResultFound()
        } else {
            successResult(barcodeResults.first().rawValue.toString())
        }
    }
}