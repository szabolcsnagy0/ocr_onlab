package hu.bme.idselector

import android.graphics.BitmapFactory
import android.net.Uri
import android.util.Log
import android.util.Size
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import hu.android.qtyadoki.api.ApiService
import hu.bme.idselector.data.Person
import okhttp3.MultipartBody
import okhttp3.Request
import okhttp3.RequestBody.Companion.asRequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import kotlin.math.roundToInt


class MainViewModel : ViewModel() {

    val person: MutableState<Person?> = mutableStateOf(null)

    private val offsets = mutableListOf<Pair<Float, Float>>()
    val intOffsets = mutableListOf<MutableState<IntOffset>>()

    val appState = mutableStateOf(AppState.START)

    val selectedImage: MutableState<ImageState> = mutableStateOf(ImageState.FRONT)

    val selectedImagePath: MutableState<String?>
        get() = when (selectedImage.value) {
            ImageState.FRONT -> frontImagePath
            ImageState.BACK -> backImagePath
        }

    val selectedImageUri: MutableState<Uri?>
        get() = when (selectedImage.value) {
            ImageState.FRONT -> frontImageUri
            ImageState.BACK -> backImageUri
        }

    val selectedImageId: MutableState<String?>
        get() = when (selectedImage.value) {
            ImageState.FRONT -> frontImageId
            ImageState.BACK -> backImageId
        }

    val selectedImageSize: Size
        get() = when (selectedImage.value) {
            ImageState.FRONT -> frontImageSize
            ImageState.BACK -> backImageSize
        }

    private val frontImageUri = mutableStateOf<Uri?>(null)
    private val backImageUri = mutableStateOf<Uri?>(null)
    private val frontImagePath = mutableStateOf<String?>(null)
    private val backImagePath = mutableStateOf<String?>(null)
    private val frontImageId = mutableStateOf<String?>("front.jpg")
//    private val frontImageId = mutableStateOf<String?>(null)
    private val backImageId = mutableStateOf<String?>("back.jpg")
//    private val backImageId = mutableStateOf<String?>(null)
    private val frontImageSize: Size = getImageSize(frontImagePath.value?.let { File(it) })
    private val backImageSize: Size = getImageSize(backImagePath.value?.let { File(it) })

    private fun getImageSize(image: File?): Size {
        if (image == null || !image.exists()) return Size(0, 0)
        val options = BitmapFactory.Options()
        options.inJustDecodeBounds = true
        BitmapFactory.decodeFile(image.absolutePath, options)
        return Size(options.outWidth, options.outHeight)
    }

    fun detectCorners(
        onResult: (Boolean, String) -> Unit = { _, _ -> }
    ) {
        val image = selectedImagePath.value?.let { File(it) }
        if (image == null) {
            onResult(false, "Hiba! Kép nem található!")
            return
        }
        if (appState.value == AppState.LOADING) {
            return
        } else appState.value = AppState.LOADING

        val requestMultipartBody =
            MultipartBody.Part.createFormData("image", image.name, image.asRequestBody())
        val call = ApiService.getInstance().detectCorners(requestMultipartBody)
        call!!.enqueue(object : Callback<List<List<Float>>> {

            override fun onResponse(
                call: Call<List<List<Float>>>,
                response: Response<List<List<Float>>>
            ) {
                appState.value = if (response.isSuccessful) {
                    Log.i("corners", response.body().toString())
                    val list = response.body() ?: return
                    intOffsets.clear()
                    offsets.clear()
                    for (value in list) {
                        offsets.add(Pair(value[0], value[1]))
                    }
                    onResult(true, "Siker!")
                    AppState.CROP
                } else {
                    onResult(false, "Hiba! Hibakód: ${response.code()} ${response.message()}")
                    AppState.START
                }
            }

            override fun onFailure(call: Call<List<List<Float>>>, t: Throwable) {
                Log.e("corners", t.message.toString())
                appState.value = AppState.START
                onResult(false, "Hiba! ${t.message.toString()}!")
            }
        })
    }

    fun cropPicture(
        onResult: (Boolean, String) -> Unit = { _, _ -> }
    ) {
        val image = selectedImagePath.value?.let { File(it) }
        if (image == null) {
            onResult(false, "Hiba! Kép nem található!")
            return
        }
        if (appState.value == AppState.LOADING) {
            return
        } else appState.value = AppState.LOADING

        val imageData =
            MultipartBody.Part.createFormData("image", image.name, image.asRequestBody())

        var corners = ""
        for (offset in getConvertedOffsets()) {
            corners += offset.first.toString() + " " + offset.second.toString() + " "
        }
        corners = corners.dropLast(1)

        Log.i("crop", corners)

        val cornersData = MultipartBody.Part.createFormData("corners", corners)
        val call = ApiService.getInstance().cropPicture(imageData, cornersData)
        call!!.enqueue(object : Callback<String> {

            override fun onResponse(call: Call<String>, response: Response<String>) {
                appState.value = AppState.START
                if (response.isSuccessful) {
                    if (response.body() != null) {
                        selectedImageId.value = response.body()
                    }
                } else {
                    onResult(false, "Hiba! Hibakód: ${response.code()} ${response.message()}")
                }
            }

            override fun onFailure(call: Call<String>, t: Throwable) {
                appState.value = AppState.START
                onResult(false, "Hiba! ${t.message.toString()}!")
            }
        })
    }

    fun detectText(
        onResult: (Boolean, String) -> Unit = { _, _ -> }
    ) {
        val call = ApiService.getInstance().detectText(frontImageId.value, backImageId.value)

        if (appState.value == AppState.LOADING) {
            return
        } else appState.value = AppState.LOADING

        call.enqueue(object : Callback<Person?> {

            override fun onResponse(call: Call<Person?>, response: Response<Person?>) {
                appState.value = if (response.isSuccessful) {
                    Log.i("detect", response.body().toString())
                    person.value = response.body()
                    onResult(true, "Siker!")
                    AppState.RESULT
                } else {
                    Log.i("detect", "Hiba!")
                    onResult(false, "Hiba! Hibakód: ${response.code()} ${response.message()}")
                    AppState.START
                }
            }

            override fun onFailure(call: Call<Person?>, t: Throwable) {
                appState.value = AppState.START
                Log.i("detect", t.message.toString())
                onResult(false, "Hiba! ${t.message.toString()}!")
            }
        })
    }

    fun getFrontImageUrl() = getImageUrl(frontImageId)
    fun getBackImageUrl() = getImageUrl(backImageId)

    private fun getImageUrl(imageId: MutableState<String?>): String? {
        return if (imageId.value == null) {
            null
        } else {
            ApiService.getImageUrl(imageId.value!!)
        }
    }

    private var currentSize: Pair<Float, Float> = Pair(1f, 1f)

    fun initIntOffsets(width: Float, height: Float) {
        currentSize = Pair(width, height)
        offsets.forEach { offset ->
            intOffsets += mutableStateOf(
                IntOffset(
                    (width * offset.first).roundToInt(),
                    (height * offset.second).roundToInt()
                )
            )
        }
    }

    private fun getConvertedOffsets(): List<Pair<Float, Float>> {
        Log.i("crop", currentSize.toString())
        for (o in intOffsets) {
            Log.i("crop", o.value.toString())
        }
        val offsets = mutableListOf<Pair<Float, Float>>()
        intOffsets.forEach { intOffset ->
            val x = (intOffset.value.x.toFloat() / currentSize.first)
            val y = (intOffset.value.y.toFloat() / currentSize.second)
            offsets += Pair(x, y)
        }
        return offsets
    }
}