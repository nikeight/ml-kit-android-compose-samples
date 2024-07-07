package com.example.textrecognizer.di

import com.google.mlkit.common.model.RemoteModelManager
import com.google.mlkit.vision.digitalink.DigitalInkRecognition
import com.google.mlkit.vision.digitalink.DigitalInkRecognitionModel
import com.google.mlkit.vision.digitalink.DigitalInkRecognitionModelIdentifier
import com.google.mlkit.vision.digitalink.DigitalInkRecognizer
import com.google.mlkit.vision.digitalink.DigitalInkRecognizerOptions
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object TextRecognizerModule {

    @Provides
    @Singleton
    @Named("US_LANG")
    fun provideUsLangRecognizer(): DigitalInkRecognitionModelIdentifier? =
        try {
            DigitalInkRecognitionModelIdentifier
                .fromLanguageTag("en-US")
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }

    @Provides
    @Singleton
    fun provideDigitalInkRecognitionModel(
        modelIdentifier: DigitalInkRecognitionModelIdentifier?
    ): DigitalInkRecognitionModel? = modelIdentifier?.let {
        DigitalInkRecognitionModel.builder(modelIdentifier).build()
    }

    @Provides
    @Singleton
    fun provideDigitalInkRecognizer(digitalInkModel: DigitalInkRecognitionModel?):
            DigitalInkRecognizer? = digitalInkModel?.let {
        DigitalInkRecognition.getClient(
            DigitalInkRecognizerOptions.builder(digitalInkModel).build()
        )
    }

    @Provides
    @Singleton
    fun provideModelDownloaderManager() = RemoteModelManager.getInstance()
}