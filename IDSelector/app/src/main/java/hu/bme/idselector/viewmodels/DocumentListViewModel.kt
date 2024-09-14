package hu.bme.idselector.viewmodels

import android.util.Log
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import hu.bme.idselector.api.ApiService
import hu.bme.idselector.data.Document
import hu.bme.idselector.data.DocumentTemplate
import hu.bme.idselector.data.NationalId
import hu.bme.idselector.data.OtherId
import hu.bme.idselector.data.Profile
import hu.bme.idselector.navigation.Routes
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DocumentListViewModel(
    val profile: Profile,
    val navController: NavController
) : ViewModel() {
    val nationalIds = mutableStateListOf<NationalId>()
    val documents = mutableStateListOf<Document>()
    val otherIds = mutableStateListOf<OtherId>()

    private val isRefreshingNational = mutableStateOf(false)
    private val isRefreshingDocument = mutableStateOf(false)
    private val isRefreshingOther = mutableStateOf(false)
    private val isRefreshing = isRefreshingNational.value && isRefreshingOther.value && isRefreshingDocument.value

    private val _documentTemplates = MutableStateFlow<List<DocumentTemplate>>(emptyList())
    val documentTemplates = _documentTemplates.asStateFlow()

    init {
        if (nationalIds.isEmpty() && otherIds.isEmpty()) {
            refreshDocumentsList()
        }
        if (documentTemplates.value.isEmpty()) {
            getDocumentTemplates()
        }
    }

    fun onSelectDocumentTemplate(selectedTemplate: DocumentTemplate) =
        navController.navigate(Routes.NewDocumentFromTemplate.route.replace("{template_id}", "${selectedTemplate.id}"))

    fun onAddNewOtherId() = navController.navigate(Routes.NewOtherIdDocument.route)

    fun onAddNewNationalId() = navController.navigate(Routes.NewNationalIdDocument.route)

    fun refreshDocumentsList() {
        if (isRefreshing) return
        getNationalIdList()
        getDocumentList()
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

    private fun getDocumentList() {
        if (isRefreshingDocument.value) return
        isRefreshingDocument.value = true
        Log.i("ListViewModel", "documents")
        val call = ApiService.getInstance().getDocuments(profile.id)
        call?.enqueue(object : Callback<List<Document>?> {

            override fun onResponse(
                call: Call<List<Document>?>,
                response: Response<List<Document>?>
            ) {
                if (response.isSuccessful) {
                    val responseData = response.body()
                    if (documents != responseData) {
                        documents.clear()
                        responseData?.let {
                            documents.addAll(it)
                        }
                        Log.i("ListViewModel", documents.toString())
                    }
                }
                isRefreshingDocument.value = false
            }

            override fun onFailure(call: Call<List<Document>?>, t: Throwable) {
                Log.e("ListViewModel", "Error while getting national id list: ${t.message}")
                isRefreshingDocument.value = false
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

    private fun getDocumentTemplates() {
        val call = ApiService.getInstance().getDocumentTemplates()
        call?.enqueue(object : Callback<List<DocumentTemplate>?> {

            override fun onResponse(
                call: Call<List<DocumentTemplate>?>,
                response: Response<List<DocumentTemplate>?>
            ) {
                if (response.isSuccessful) {
                    val responseData = response.body()
                    responseData?.let {
                        _documentTemplates.tryEmit(it)
                    }
                }
            }

            override fun onFailure(call: Call<List<DocumentTemplate>?>, t: Throwable) {
                Log.e("ListViewModel", "Error while getting document template list: ${t.message}")
            }
        })
    }
}
