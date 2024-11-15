package hu.bme.idselector.viewmodels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import hu.bme.idselector.api.ApiService
import hu.bme.idselector.api.TokenManager
import hu.bme.idselector.data.LoginData
import hu.bme.idselector.error.MessageEvent
import hu.bme.idselector.error.MessageHandler
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject

/**
 * ViewModel for the authentication process.
 * @param sessionManager: TokenManager
 */
@HiltViewModel
class AuthenticationViewModel @Inject constructor(
    private val messageHandler: MessageHandler,
    private val sessionManager: TokenManager
) : ViewModel() {

    private val _isLoggedIn = MutableLiveData(false)
    val isLoggedIn: LiveData<Boolean> = _isLoggedIn

    /**
     * Login user with the given email and password.
     */
    fun loginUser(
        email: String, password: String
    ) {
        val loginData = LoginData(email = email, password = password)
        val call = ApiService.getInstance().loginUser(loginData)
        call.enqueue(object : Callback<String?> {

            override fun onResponse(call: Call<String?>, response: Response<String?>) {
                if (response.isSuccessful) {
                    _isLoggedIn.value = true
                    messageHandler.handleMessage(MessageEvent.Message("Successful login!"))
                    saveToken(response.body())
                } else {
                    messageHandler.handleMessage(MessageEvent.Message("Login failed. Try again!"))
                }
            }

            override fun onFailure(call: Call<String?>, t: Throwable) {
                Log.e("Login", "Error: ${t.message}")
                messageHandler.handleMessage(MessageEvent.Message("Login failed. Try again!"))
            }
        })
    }

    /**
     * Test the currently stored token.
     */
    fun testToken() {
        if (sessionManager.fetchAuthToken().isNullOrEmpty()) return
        val call = ApiService.getInstance().testToken()
        call.enqueue(object : Callback<ResponseBody?> {

            override fun onResponse(call: Call<ResponseBody?>, response: Response<ResponseBody?>) {
                if (response.isSuccessful) {
                    _isLoggedIn.value = true
                    messageHandler.handleMessage(MessageEvent.Message(response.body().toString()))
                }
            }

            override fun onFailure(call: Call<ResponseBody?>, t: Throwable) {
                Log.e("TokenTest", "Error: ${t.message}")
            }
        })
    }

    /**
     * Save the token to the TokenManager.
     */
    fun saveToken(token: String?) {
        if (token?.isNotBlank() == true) {
            sessionManager.saveAuthToken(token)
        }
    }

    /**
     * Log out the user.
     */
    fun logOut() {
        sessionManager.deleteAuthToken()
        _isLoggedIn.value = false
    }
}
