package com.example.geminichatapp.data.util

import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import coil.ImageLoader
import coil.request.ImageRequest
import coil.request.SuccessResult
import coil.size.Precision
import javax.inject.Inject

/**
 * Todo: Handle Error and propagate it to upstream
 */
class UriToBitmapConverterImpl @Inject constructor(
    private val imageRequestBuilder: ImageRequest.Builder,
    private val imageLoader: ImageLoader
) : UriToBitmapConverter {
    override suspend fun convert(uriList: List<String>): List<Bitmap?>? {
        val bitmaps = uriList.map {
            val imageRequest = imageRequestBuilder
                .data(Uri.parse(it))
                .size(size = 768)
                .precision(Precision.EXACT)
                .build()
            try {
                val result = imageLoader.execute(imageRequest)
                if (result is SuccessResult) {
                    return@map (result.drawable as BitmapDrawable).bitmap
                } else {
                    return@map null
                }
            } catch (e: Exception) {
                return@map null
            }
        }
        return bitmaps
    }
}