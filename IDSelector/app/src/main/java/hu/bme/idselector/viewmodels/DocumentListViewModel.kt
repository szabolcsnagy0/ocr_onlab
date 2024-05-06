package hu.bme.idselector.viewmodels

import android.util.Log
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import hu.bme.idselector.api.ApiService
import hu.bme.idselector.data.NationalId
import hu.bme.idselector.data.OtherId
import hu.bme.idselector.data.Profile
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DocumentListViewModel(
    val profile: Profile
) : ViewModel() {
    val nationalIds = mutableStateListOf<NationalId>()
    val otherIds = mutableStateListOf<OtherId>()

    private val isRefreshingNational = mutableStateOf(false)
    private val isRefreshingOther = mutableStateOf(false)
    private val isRefreshing = isRefreshingNational.value && isRefreshingOther.value

    init {
        if (nationalIds.isEmpty() && otherIds.isEmpty())
            refreshDocumentsList()
    }

    fun refreshDocumentsList() {
        if (isRefreshing) return
        getNationalIdList()
        getOtherIdList()
    }

    private fun getNationalIdList() {
        if (isRefreshingNational.value) return
        isRefreshingNational.value = true
        Log.i("ListViewModel", "national id")
        val call = ApiService.getInstance().getNationalIds(profile.id)
        call?.enqueue(object : Callback<List<NationalId>?> {

            override fun onResponse(
                call: Call<List<NationalId>?>,
                response: Response<List<NationalId>?>
            ) {
                if (response.isSuccessful) {
                    val responseData = response.body()
                    if (nationalIds != responseData) {
                        nationalIds.clear()
                        responseData?.let {
                            nationalIds.addAll(it)
                        }
                        Log.i("ListViewModel", nationalIds.toString())
                    }
                }
                isRefreshingNational.value = false
            }

            override fun onFailure(call: Call<List<NationalId>?>, t: Throwable) {
                Log.e("ListViewModel", "Error while getting national id list: ${t.message}")
                isRefreshingNational.value = false
            }
        })
    }

    private fun getOtherIdList() {
        if (isRefreshingOther.value) return
        isRefreshingOther.value = true
        Log.i("ListViewModel", "other id")
        val call = ApiService.getInstance().getOtherIds(profile.id)
        call?.enqueue(object : Callback<List<OtherId>?> {

            override fun onResponse(
                call: Call<List<OtherId>?>,
                response: Response<List<OtherId>?>
            ) {
                if (response.isSuccessful) {
                    val responseData = response.body()
                    if (otherIds != responseData) {
                        otherIds.clear()
                        responseData?.let {
                            otherIds.addAll(it)
                        }
                        Log.i("ListViewModel", otherIds.toList().toString())
                    }
                }
                isRefreshingOther.value = false
            }

            override fun onFailure(call: Call<List<OtherId>?>, t: Throwable) {
                Log.e("ListViewModel", "Error while getting other id list: ${t.message}")
                isRefreshingOther.value = false
            }
        })
    }
}