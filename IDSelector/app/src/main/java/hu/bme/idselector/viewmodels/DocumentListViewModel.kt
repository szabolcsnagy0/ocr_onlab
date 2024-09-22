package hu.bme.idselector.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DocumentListViewModel(
    val profile: Profile,
    private val navController: NavController
) : ViewModel() {
    private val _nationalIds = MutableStateFlow<List<NationalId>>(emptyList())
    val nationalIds = _nationalIds.asStateFlow()

    private val _documents = MutableStateFlow<List<Document>>(emptyList())
    val documents = _documents.asStateFlow()

    private val _otherIds = MutableStateFlow<List<OtherId>>(emptyList())
    val otherIds = _otherIds.asStateFlow()

    private val isRefreshingNational = MutableStateFlow(false)
    private val isRefreshingDocument = MutableStateFlow(false)
    private val isRefreshingOther = MutableStateFlow(false)
    val isRefreshing =
        combine(isRefreshingNational, isRefreshingOther, isRefreshingDocument) { national, other, document ->
            national || other || document
        }

    private val _documentTemplates = MutableStateFlow<List<DocumentTemplate>>(emptyList())
    val documentTemplates = _documentTemplates.asStateFlow()

    init {
        if (_nationalIds.value.isEmpty() && _otherIds.value.isEmpty()) {
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
        viewModelScope.launch {
            getNationalIdList()
            getDocumentList()
            getOtherIdList()
        }
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
                    if (nationalIds.value != responseData) {
                        responseData?.let {
                            _nationalIds.tryEmit(it)
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
                    if (documents.value != responseData) {
                        responseData?.let {
                            _documents.tryEmit(it)
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
                    if (otherIds.value != responseData) {
                        responseData?.let {
                            _otherIds.tryEmit(it)
                        }
                        Log.i("ListViewModel", otherIds.value.toList().toString())
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
