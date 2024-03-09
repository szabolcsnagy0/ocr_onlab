package hu.bme.idselector

import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.unit.IntOffset
import androidx.lifecycle.ViewModel
import hu.android.qtyadoki.api.ApiService
import hu.android.qtyadoki.api.CornerResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File

class MainViewModel: ViewModel() {

    val offsetHandle1 = mutableStateOf(IntOffset.Zero)
    val offsetHandle2 = mutableStateOf(IntOffset.Zero)
    val offsetHandle3 = mutableStateOf(IntOffset.Zero)
    val offsetHandle4 = mutableStateOf(IntOffset.Zero)

    fun uploadPicture(
        image: File?,
        onResult: (Boolean, String) -> Unit = { _, _ -> }
    ) {
        if (image == null) {
            onResult(false, "Hiba! Kép nem található!")
            return
        }

//        val requestFile = image.asRequestBody()
        val requestMultipartBody = MultipartBody.Part.createFormData("image", image.name, image.asRequestBody())
        val call = ApiService.getInstance().detectCorners(requestMultipartBody)
        initCall(call, onResult)
    }

    private fun initCall(
        call: Call<List<List<Int>>>?,
        onResult: (Boolean, String) -> Unit = { _, _ -> }
    ) {
        call!!.enqueue(object : Callback<List<List<Int>>> {

            override fun onResponse(call: Call<List<List<Int>>>, response: Response<List<List<Int>>>) {
                if (response.isSuccessful) {
                    onResult(true, "Siker!")
                    val list = response.body() ?: return
                    offsetHandle1.value = IntOffset(list[0][0], list[0][1])
                    offsetHandle2.value = IntOffset(list[1][0], list[1][1])
                    offsetHandle3.value = IntOffset(list[2][0], list[2][1])
                    offsetHandle4.value = IntOffset(list[3][0], list[3][1])
                    Log.i("corners", response.body().toString())
                } else onResult(false, "Hiba! Hibakód: ${response.code()} ${response.message()}")
            }

            override fun onFailure(call: Call<List<List<Int>>>, t: Throwable) {
                Log.e("ImageHelper", t.message.toString())
                onResult(false, "Hiba! ${t.message.toString()}!")
            }
        })
    }
}