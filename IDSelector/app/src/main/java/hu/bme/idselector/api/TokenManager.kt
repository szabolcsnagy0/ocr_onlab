package hu.bme.idselector.api

import android.content.Context
import android.content.SharedPreferences
import hu.bme.idselector.R

/**
 * Class to manage the authentication token
 */
class TokenManager(context: Context) {
    private var prefs: SharedPreferences =
        context.getSharedPreferences(context.getString(R.string.app_name), Context.MODE_PRIVATE)

    companion object {
        const val USER_TOKEN = "user_token"
    }

    /**
     * Function to save auth token
     */
    fun saveAuthToken(token: String) {
        val editor = prefs.edit()
        editor.putString(USER_TOKEN, token)
        editor.apply()
    }

    /**
     * Function to delete auth token
     */
    fun deleteAuthToken() {
        if(!prefs.contains(USER_TOKEN)) return
        val editor = prefs.edit()
        editor.remove(USER_TOKEN)
        editor.apply()
    }

    /**
     * Function to fetch auth token
     */
    fun fetchAuthToken() : String? {
        return prefs.getString(USER_TOKEN, null)
    }
}