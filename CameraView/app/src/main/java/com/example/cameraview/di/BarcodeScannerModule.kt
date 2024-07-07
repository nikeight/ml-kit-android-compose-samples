package com.example.cameraview.di

import com.google.mlkit.vision.barcode.BarcodeScannerOptions
import com.google.mlkit.vision.barcode.BarcodeScanning
import com.google.mlkit.vision.barcode.common.Barcode
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object BarcodeScannerModule {

    @Provides
    fun provideBarCodeScannerOption() = BarcodeScannerOptions.Builder()
        .setBarcodeFormats(Barcode.FORMAT_QR_CODE)
        .build()

    @Provides
    fun provideBarCodeClient(
        barCodeScannerOptions: BarcodeScannerOptions
    ) = BarcodeScanning.getClient(barCodeScannerOptions)
}