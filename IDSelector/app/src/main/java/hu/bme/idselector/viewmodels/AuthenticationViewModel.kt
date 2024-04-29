package hu.bme.idselector.viewmodels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import hu.android.qtyadoki.api.ApiService
import hu.bme.idselector.api.TokenManager
import hu.bme.idselector.data.LoginData
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**
 * ViewModel for the authentication process.
 * @param sessionManager: TokenManager
 */
class AuthenticationViewModel(private val sessionManager: TokenManager) : ViewModel() {

    private val _isLoggedIn = MutableLiveData(false)
    val isLoggedIn: LiveData<Boolean> = _isLoggedIn

    val loginText: MutableLiveData<String?> = MutableLiveData(null)

    val loginError: MutableLiveData<Boolean> = MutableLiveData(false)

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
                    loginText.value = null
                    saveToken(response.body())
                } else loginError.value = true
            }

            override fun onFailure(call: Call<String?>, t: Throwable) {
                Log.e("Login", "Error: ${t.message}")
                loginError.value = true
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
//                    loginText.value = response.body()?.string()
                } else loginText.value = null
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
        loginText.value = ""
    }
}