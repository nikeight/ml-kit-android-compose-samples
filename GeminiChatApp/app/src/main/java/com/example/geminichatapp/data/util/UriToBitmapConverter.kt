package com.example.geminichatapp.data.util

import android.graphics.Bitmap

interface UriToBitmapConverter {
    suspend fun convert(uriList : List<String>) : List<Bitmap?>?
}