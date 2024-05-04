package hu.bme.idselector.viewmodels

import androidx.compose.runtime.MutableState
import androidx.lifecycle.ViewModel
import hu.bme.idselector.api.ApiService
import hu.bme.idselector.data.UserRegistration
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**
 * ViewModel for the Registration screen.
 */
class RegistrationViewModel : ViewModel() {

    /**
     * Register a new user.
     * @param user The new user's data.
     * @param result The result of the registration.
     * @param success The success of the registration.
     */
    fun registerUser(
        user: UserRegistration,
        result: MutableState<String>,
        success: MutableState<Boolean>
    ) {
        val call = ApiService.getInstance().registerUser(user)
        call.enqueue(object : Callback<ResponseBody?> {

            override fun onResponse(
                call: Call<ResponseBody?>,
                response: Response<ResponseBody?>
            ) {
                if (response.isSuccessful) {
                    success.value = true
                    result.value = "Successful registration!"
                } else result.value =
                    "Error! Try again! Error code: ${response.code()} ${response.message()}"
            }

            override fun onFailure(call: Call<ResponseBody?>, t: Throwable) {
                success.value = false
                result.value = "Error! ${t.message.toString()}!"
            }
        })
    }
}