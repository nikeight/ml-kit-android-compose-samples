package com.example.cameraview.data

import android.util.Log
import javax.inject.Inject

interface StorageService {
    fun saveId()
    fun retrieveDataFromId(id: String)
}

class FirebaseStorageService @Inject constructor() : StorageService {
    override fun saveId() {
        Log.d("TAG", "Id Saved")
    }

    override fun retrieveDataFromId(id: String) {
        Log.d("TAG", "Data Retrieved")
    }
}

class LocalDatabaseStorageService @Inject constructor() : StorageService {
    override fun saveId() {
        Log.d("TAG", "Id Saved")
    }

    override fun retrieveDataFromId(id: String) {
        Log.d("TAG", "Data Retrieved")
    }
}