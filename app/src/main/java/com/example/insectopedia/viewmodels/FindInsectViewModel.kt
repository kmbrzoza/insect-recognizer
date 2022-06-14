package com.example.insectopedia.viewmodels
import android.app.Application
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Base64
import androidx.lifecycle.AndroidViewModel
import com.google.android.gms.tasks.Task
import com.google.firebase.functions.FirebaseFunctions
import com.google.firebase.functions.ktx.functions
import com.google.firebase.ktx.Firebase
import com.google.gson.*
import java.io.ByteArrayOutputStream
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

class FindInsectViewModel(application: Application, val imagePath: String) :
    AndroidViewModel(application) {
    var processFinished: Boolean = false
        private set

    private lateinit var functions: FirebaseFunctions
    private val resultsNumber: Int = 15

    fun processImage(): Task<JsonElement>? {
        if (!File(imagePath).exists()) {
            return null
        }

        var bitmap: Bitmap = convertImageToBitmap(imagePath)
        bitmap = scaleBitmap(bitmap)

        val base64 = convertBitmapToBase64(bitmap)

        functions = Firebase.functions
        val request = createRequest(base64)

        return sendRequest(request.toString())
    }

    private fun convertImageToBitmap(path: String): Bitmap {
        return BitmapFactory.decodeFile(path)
        //return BitmapFactory.decodeResource(getApplication<Application>().applicationContext.resources, R.drawable.biedronka)
    }

    private fun scaleBitmap(bitmap: Bitmap, maxDimension: Int = 1200): Bitmap {
        val originalWidth = bitmap.width
        val originalHeight = bitmap.height
        var resizedWidth = maxDimension
        var resizedHeight = maxDimension

        if (originalHeight > originalWidth) {
            resizedWidth = (resizedHeight * originalWidth.toFloat() /
                    originalHeight.toFloat()).toInt()
        } else if (originalWidth > originalHeight) {
            resizedHeight = (resizedWidth * originalHeight.toFloat() /
                    originalWidth.toFloat()).toInt()
        }

        return Bitmap.createScaledBitmap(bitmap, resizedWidth, resizedHeight, false)
    }

    private fun convertBitmapToBase64(bitmap: Bitmap): String {
        val byteArrayOutputStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream)
        val imageBytes: ByteArray = byteArrayOutputStream.toByteArray()
        return Base64.encodeToString(imageBytes, Base64.NO_WRAP)
    }

    private fun createRequest(base64encoded: String): JsonObject {
        val request = JsonObject()

        val image = JsonObject()
        image.add("content", JsonPrimitive(base64encoded))
        request.add("image", image)

        val feature = JsonObject()
        feature.add("maxResults", JsonPrimitive(resultsNumber))
        feature.add("type", JsonPrimitive("LABEL_DETECTION"))

        val features = JsonArray()
        features.add(feature)
        request.add("features", features)

        return request
    }

    private fun sendRequest(requestJson: String): Task<JsonElement> {
        return functions
            .getHttpsCallable("annotateImage")
            .call(requestJson)
            .continueWith { task ->
                val result = task.result?.data
                JsonParser.parseString(Gson().toJson(result))
            }
    }

    fun imageExists(): Boolean = File(imagePath).exists()

    fun deleteImage() {
        val imageFile = File(imagePath)
        if (imageFile.exists()) {
            imageFile.delete()
        }
    }
}