package com.example.cameraview.data

import android.util.Log
import javax.inject.Inject

interface Repository {
    fun saveIdAndShowMessage()
    fun retrieveDataAndCloseConnection(id: String)
}

class RepositoryImpl @Inject constructor(
    private val storageService: StorageService
) : Repository {

    override fun saveIdAndShowMessage() {
        storageService.saveId()
        Log.d("TAG", "Showing Message to user")
    }

    override fun retrieveDataAndCloseConnection(id: String) {
        storageService.retrieveDataFromId(id)
        Log.d("TAG", "Closing Connection")
    }
}