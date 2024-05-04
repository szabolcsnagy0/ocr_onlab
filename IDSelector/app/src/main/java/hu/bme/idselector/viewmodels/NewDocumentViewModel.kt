package hu.bme.idselector.viewmodels

import android.graphics.BitmapFactory
import android.net.Uri
import android.util.Log
import android.util.Size
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.unit.IntOffset
import androidx.lifecycle.ViewModel
import com.bumptech.glide.load.model.GlideUrl
import hu.bme.idselector.api.ApiService
import hu.bme.idselector.ui.createid.states.DetectionState
import hu.bme.idselector.ui.createid.states.ImageState
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import kotlin.math.roundToInt

abstract class NewDocumentViewModel(
    val profileId: Int
) : ViewModel() {

    private val offsets = mutableListOf<Pair<Float, Float>>()
    val intOffsets = mutableListOf<MutableState<IntOffset>>()

    val detectionState = mutableStateOf(DetectionState.START)

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

    private val frontImageUri = mutableStateOf<Uri?>(null)
    private val backImageUri = mutableStateOf<Uri?>(null)

    private val frontImagePath = mutableStateOf<String?>(null)
    private val backImagePath = mutableStateOf<String?>(null)

    protected val frontImageId = mutableStateOf<String?>(null)
    protected val backImageId = mutableStateOf<String?>(null)


    abstract fun onResult()

    fun detectCorners() {
        if (detectionState.value == DetectionState.LOADING) {
            return
        } else detectionState.value = DetectionState.LOADING

        val image = selectedImagePath.value?.let { File(it) } ?: return

        val requestMultipartBody =
            MultipartBody.Part.createFormData("image", image.name, image.asRequestBody())
        val call = ApiService.getInstance().detectCorners(requestMultipartBody)
        call?.enqueue(object : Callback<List<List<Float>>> {

            override fun onResponse(
                call: Call<List<List<Float>>>,
                response: Response<List<List<Float>>>
            ) {
                detectionState.value = if (response.isSuccessful) {
                    Log.i("corners", response.body().toString())
                    val list = response.body() ?: return
                    intOffsets.clear()
                    offsets.clear()
                    for (value in list) {
                        offsets.add(Pair(value[0], value[1]))
                    }
                    DetectionState.CROP
                } else {
                    Log.i("corners", response.body().toString())
                    DetectionState.ERROR
                }
            }

            override fun onFailure(call: Call<List<List<Float>>>, t: Throwable) {
                Log.e("corners", t.message.toString())
                detectionState.value = DetectionState.ERROR
            }
        })
    }

    fun cropPicture(
        onResult: (Boolean, String) -> Unit = { _, _ -> }
    ) {
        if (detectionState.value == DetectionState.LOADING) {
            return
        } else detectionState.value = DetectionState.LOADING

        val image = selectedImagePath.value?.let { File(it) }
        if (image == null) {
            onResult(false, "Hiba! Kép nem található!")
            return
        }

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
        call?.enqueue(object : Callback<String> {

            override fun onResponse(call: Call<String>, response: Response<String>) {
                detectionState.value = DetectionState.START
                if (response.isSuccessful) {
                    if (response.body() != null) {
                        selectedImageId.value = response.body()
                    }
                } else {
                    onResult(false, "Hiba! Hibakód: ${response.code()} ${response.message()}")
                }
            }

            override fun onFailure(call: Call<String>, t: Throwable) {
                detectionState.value = DetectionState.START
                onResult(false, "Hiba! ${t.message.toString()}!")
            }
        })
    }

    fun cancelUpload() {
        val call = ApiService.getInstance().clearFolder()
        call?.enqueue(object : Callback<Any> {
            override fun onFailure(call: Call<Any>, t: Throwable) {
                Log.e("clear", t.message.toString())
            }
            override fun onResponse(call: Call<Any>, response: Response<Any>) {
            }
        })
    }

    fun getFrontImageUrl() = getImageUrl(frontImageId)
    fun getBackImageUrl() = getImageUrl(backImageId)

    private fun getImageUrl(imageId: MutableState<String?>): GlideUrl? {
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
        val offsets = mutableListOf<Pair<Float, Float>>()
        intOffsets.forEach { intOffset ->
            val x = (intOffset.value.x.toFloat() / currentSize.first)
            val y = (intOffset.value.y.toFloat() / currentSize.second)
            offsets += Pair(x, y)
        }
        return offsets
    }

    private fun getImageSize(image: File?): Size {
        if (image == null || !image.exists()) return Size(0, 0)
        val options = BitmapFactory.Options()
        options.inJustDecodeBounds = true
        BitmapFactory.decodeFile(image.absolutePath, options)
        return Size(options.outWidth, options.outHeight)
    }
}