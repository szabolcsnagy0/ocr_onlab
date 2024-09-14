package hu.bme.idselector.viewmodels.createid

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import hu.bme.idselector.api.ApiService
import hu.bme.idselector.data.NationalId
import hu.bme.idselector.ui.createid.states.DetectionState
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class NewNationalViewModel(
    private val profileId: Int
) : NewDocumentViewModel() {
    val identity: MutableState<NationalId?> = mutableStateOf(null)

    override fun onResult() {
        if (detectionState.value == DetectionState.LOADING) {
            return
        } else detectionState.value = DetectionState.LOADING

        val call =
            ApiService.getInstance().detectNationalIdText(
                frontImageId.value, backImageId.value
            )

        call?.enqueue(object : Callback<NationalId?> {

            override fun onResponse(call: Call<NationalId?>, response: Response<NationalId?>) {
                detectionState.value = if (response.isSuccessful) {
                    Log.i("detect", response.body().toString())
                    identity.value = response.body()
                    DetectionState.RESULT
                } else {
                    Log.i("detect", "Hiba!")
                    DetectionState.ERROR
                }
            }

            override fun onFailure(call: Call<NationalId?>, t: Throwable) {
                detectionState.value = DetectionState.ERROR
                Log.i("detect", t.message.toString())
            }
        })
    }

    fun createId() {
        if (frontImageId.value == null || backImageId.value == null || identity.value == null) return

        identity.value?.front = frontImageId.value
        identity.value?.back = backImageId.value

        val call = ApiService.getInstance().createNewNationalId(
            profileId = profileId,
            nationalId = identity.value!!
        )
        call?.enqueue(object : Callback<ResponseBody> {

            override fun onResponse(
                call: Call<ResponseBody>,
                response: Response<ResponseBody>
            ) {
                Log.i("nationalid", response.body().toString())
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                Log.e("nationalid", t.message.toString())
                detectionState.value = DetectionState.ERROR
            }
        })
    }
}
