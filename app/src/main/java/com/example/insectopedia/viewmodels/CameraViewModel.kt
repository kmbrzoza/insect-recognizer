package com.example.insectopedia.viewmodels

import android.app.Application
import android.widget.Toast
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.core.content.ContextCompat
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.example.insectopedia.R
import com.example.insectopedia.helpers.generatePictureFileName
import com.example.insectopedia.services.ConnectionService
import java.io.File

class CameraViewModel(application: Application) : AndroidViewModel(application) {
    var imageCapture: ImageCapture = ImageCapture.Builder().build()

    var newImagePath: MutableLiveData<String?> = MutableLiveData(null)
        private set

    private var outputDirectory: File = getOutputDirectory()

    private fun getOutputDirectory(): File {
        val app = getApplication<Application>()
        val resultDirectory = app.externalMediaDirs?.firstOrNull()?.let {
            File(it, app.getString(R.string.app_name)).apply {
                mkdirs()
            }
        }
        return if (resultDirectory?.exists() == true) resultDirectory else app.filesDir
    }

    fun savePicture() {
        if (!ConnectionService.isNetworkConnected(getApplication())) {
            Toast.makeText(
                getApplication(),
                "You need an internet connection",
                Toast.LENGTH_LONG
            ).show()
        }

        val fileName = generatePictureFileName()
        val pictureFile = File(outputDirectory, fileName)
        val outputOptions = ImageCapture.OutputFileOptions.Builder(pictureFile).build()

        imageCapture.takePicture(
            outputOptions,
            ContextCompat.getMainExecutor(getApplication()),
            object : ImageCapture.OnImageSavedCallback {
                override fun onError(exc: ImageCaptureException) {
                    Toast.makeText(
                        getApplication(),
                        "Picture capture failed",
                        Toast.LENGTH_SHORT
                    ).show()
                }

                override fun onImageSaved(output: ImageCapture.OutputFileResults) {
                    newImagePath.value = pictureFile.absolutePath
                }
            }
        )
    }
}