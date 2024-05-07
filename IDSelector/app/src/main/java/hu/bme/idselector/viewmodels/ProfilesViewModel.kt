package hu.bme.idselector.viewmodels

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import hu.bme.idselector.api.ApiService
import hu.bme.idselector.data.Profile
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ProfilesViewModel : ViewModel() {

    val profiles = mutableStateListOf<Profile>()
    val selectedProfile: MutableState<Profile?> = mutableStateOf(null)

    fun refreshProfilesList() {
        getProfilesList()
    }

    fun selectProfile(id: Int) {
        selectedProfile.value = profiles.first { it.id == id }
    }

    private fun getProfilesList() {
        val call = ApiService.getInstance().getProfiles()
        call?.enqueue(object : Callback<List<Profile>?> {

            override fun onResponse(
                call: Call<List<Profile>?>,
                response: Response<List<Profile>?>
            ) {
                if (response.isSuccessful) {
                    val responseData = response.body()
                    if (profiles != responseData) {
                        profiles.clear()
                        responseData?.let {
                            profiles.addAll(it)
                        }
                        Log.i("ListViewModel", profiles.toString())
                    }
                }
            }

            override fun onFailure(call: Call<List<Profile>?>, t: Throwable) {
                Log.e("ListViewModel", "Error while getting profiles list: ${t.message}")
            }
        })
    }

    fun createProfile(profileName: String) {
        val profile = Profile(name = profileName)
        val call = ApiService.getInstance().createNewProfile(profile)
        call?.enqueue(object : Callback<Profile?> {

            override fun onResponse(
                call: Call<Profile?>,
                response: Response<Profile?>
            ) {
                if (response.isSuccessful) {
                    response.body()?.let {
                        profiles.add(it)
                    }
                }
                Log.i("ProfilesViewModel", response.body().toString())
            }

            override fun onFailure(call: Call<Profile?>, t: Throwable) {
                Log.e("ProfilesViewModel", "Error while creating new profile: ${t.message}")
            }
        })
    }
}