package hu.bme.idselector.viewmodels.createid

import android.util.Log
import androidx.lifecycle.viewModelScope
import hu.bme.idselector.api.ApiService
import hu.bme.idselector.data.Document
import hu.bme.idselector.data.DocumentField
import hu.bme.idselector.ui.createid.states.DetectionState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class NewDocumentFromTemplateViewModel(
    private val profileId: Int,
    private val templateId: Int
) : NewDocumentViewModel() {
    private val document = MutableStateFlow<Document?>(null)
    val fieldList = document.map { it?.fieldsList ?: emptyList() }

    fun changeFieldValue(index: Int, field: DocumentField) = viewModelScope.launch {
        val currentList = fieldList.firstOrNull()?.toMutableList()
        val currentDocument = document.value
        if (currentList != null && currentDocument != null) {
            currentList[index] = field
            document.tryEmit(
                currentDocument.copy(
                    fieldsList = currentList
                )
            )
        }
    }

    override fun onResult() {
        if (detectionState.value == DetectionState.LOADING) {
            return
        } else detectionState.value = DetectionState.LOADING

        val call =
            ApiService.getInstance().detectDocumentText(
                frontImageId.value, backImageId.value,
                templateId = templateId
            )

        call?.enqueue(object : Callback<Document?> {

            override fun onResponse(call: Call<Document?>, response: Response<Document?>) {
                detectionState.value = if (response.isSuccessful) {
                    Log.i("detect", response.body().toString())
                    response.body()?.let { document.tryEmit(it) }
                    DetectionState.RESULT
                } else {
                    Log.i("detect", "Error!")
                    DetectionState.ERROR
                }
            }

            override fun onFailure(call: Call<Document?>, t: Throwable) {
                detectionState.value = DetectionState.ERROR
                Log.i("detect", t.message.toString())
            }
        })
    }

    fun createId() {
        if (frontImageId.value == null && backImageId.value == null) return

        val newDocument = document.value?.copy(
            front = frontImageId.value,
            back = backImageId.value
        ) ?: return

        val call = ApiService.getInstance().createNewDocument(
            profileId = profileId,
            document = newDocument
        )
        call?.enqueue(object : Callback<ResponseBody> {

            override fun onResponse(
                call: Call<ResponseBody>,
                response: Response<ResponseBody>
            ) {
                Log.i("document", response.body().toString())
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                Log.e("document", t.message.toString())
                detectionState.value = DetectionState.ERROR
            }
        })
    }
}
