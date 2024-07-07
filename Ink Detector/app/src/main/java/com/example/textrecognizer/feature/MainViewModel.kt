package com.example.textrecognizer.feature

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.google.mlkit.common.MlKitException
import com.google.mlkit.common.model.DownloadConditions
import com.google.mlkit.common.model.RemoteModelManager
import com.google.mlkit.vision.digitalink.DigitalInkRecognition
import com.google.mlkit.vision.digitalink.DigitalInkRecognitionModel
import com.google.mlkit.vision.digitalink.DigitalInkRecognitionModelIdentifier
import com.google.mlkit.vision.digitalink.DigitalInkRecognizer
import com.google.mlkit.vision.digitalink.DigitalInkRecognizerOptions
import com.google.mlkit.vision.digitalink.Ink

class MainViewModel() : ViewModel() {

    private var modelIdentifier: DigitalInkRecognitionModelIdentifier? = null
    private var recognizer: DigitalInkRecognizer? = null
    private var model: DigitalInkRecognitionModel? = null
    private val remoteModelManager = RemoteModelManager.getInstance()

    // State
    var recognizedText by mutableStateOf("NOT DETECTED")

    init {
        try {
            modelIdentifier = DigitalInkRecognitionModelIdentifier.fromLanguageTag("en-US")
        } catch (e: MlKitException) {
            println("FAILED ${e.message}")
        }

        modelIdentifier?.let { saveModelIdentifier ->
            model =
                DigitalInkRecognitionModel.builder(saveModelIdentifier).build()

            model?.let { saveModel ->
                recognizer =
                    DigitalInkRecognition.getClient(
                        DigitalInkRecognizerOptions.builder(saveModel).build()
                    )
            }

        }

        model?.let { saveModel ->
            remoteModelManager.download(saveModel, DownloadConditions.Builder().build())
                .addOnSuccessListener {
                    println("Downloaded the model successfully")
                }
                .addOnFailureListener { e: Exception ->
                    println("Not able to download the Model ${e.message}")

                }

        }
    }

    fun recognizeTheInk(ink: Ink) {
        recognizer?.recognize(ink)
            ?.addOnSuccessListener { result ->
                println("RESULT : ${result.candidates}")
                recognizedText = result.candidates[0].text
            }?.addOnFailureListener {
                println("FAILED RECOGNITION ${it.message}")
            }
    }
}