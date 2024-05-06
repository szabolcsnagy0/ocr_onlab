package hu.bme.idselector.viewmodels.createid

import android.util.Log
import hu.bme.idselector.api.ApiService
import hu.bme.idselector.data.OtherId
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class NewOtherIdViewModel(
    profileId: Int
) : NewDocumentViewModel(profileId) {

    override fun onResult() {
        if (frontImageId.value == null || backImageId.value == null) return
        val otherId = OtherId(front = frontImageId.value, back = backImageId.value)

        val call = ApiService.getInstance().createNewOtherId(
            profileId = profileId,
            otherId = otherId
        )
        call?.enqueue(object : Callback<ResponseBody> {

            override fun onResponse(
                call: Call<ResponseBody>,
                response: Response<ResponseBody>
            ) {
                Log.i("otherid", response.body().toString())
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                Log.e("otherid", t.message.toString())
            }
        })
    }
}