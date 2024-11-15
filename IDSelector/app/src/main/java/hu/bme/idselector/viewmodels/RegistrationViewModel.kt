package hu.bme.idselector.viewmodels

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import hu.bme.idselector.api.ApiService
import hu.bme.idselector.data.UserRegistration
import hu.bme.idselector.error.MessageEvent
import hu.bme.idselector.error.MessageHandler
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject

/**
 * ViewModel for the Registration screen.
 */
@HiltViewModel
class RegistrationViewModel @Inject constructor(
    private val messageHandler: MessageHandler
) : ViewModel() {

    /**
     * Register a new user.
     * @param user The new user's data.
     * @param success The success of the registration.
     */
    fun registerUser(
        user: UserRegistration,
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
                    messageHandler.handleMessage(MessageEvent.Message("Successful registration!"))
                } else {
                    Log.e("RegistrationError", "Error! Error code: ${response.code()} ${response.message()}")
                    messageHandler.handleMessage(MessageEvent.Message("Error! Try again!"))
                }
            }

            override fun onFailure(call: Call<ResponseBody?>, t: Throwable) {
                success.value = false
                Log.e("RegistrationError", "Error! Error code: ${t.message}")
                messageHandler.handleMessage(MessageEvent.Message("Error! Try again!"))
            }
        })
    }
}
