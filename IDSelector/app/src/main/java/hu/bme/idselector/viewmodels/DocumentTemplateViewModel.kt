package hu.bme.idselector.viewmodels

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.unit.IntOffset
import androidx.lifecycle.viewModelScope
import com.bumptech.glide.load.model.GlideUrl
import hu.bme.idselector.api.ApiService
import hu.bme.idselector.data.Coordinate
import hu.bme.idselector.data.DocumentTemplate
import hu.bme.idselector.data.TemplateField
import hu.bme.idselector.ui.createid.states.DetectionState
import hu.bme.idselector.ui.idtemplate.states.TemplateCreationState
import hu.bme.idselector.viewmodels.createid.NewDocumentViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.math.roundToInt

class DocumentTemplateViewModel : NewDocumentViewModel() {

    private val _creationState = MutableStateFlow(TemplateCreationState.START)
    val creationState = _creationState.asStateFlow()

    private val frontFields: MutableStateFlow<List<TemplateField>> = MutableStateFlow(emptyList())
    private val backFields: MutableStateFlow<List<TemplateField>> = MutableStateFlow(emptyList())

    private val currentFields: MutableStateFlow<List<TemplateField>>
        get() = when (currentFace.value) {
            Faces.FRONT -> frontFields
            Faces.BACK -> backFields
        }

    val templateFieldNames: Flow<List<String>> =
        frontFields.mapToText().combine(backFields.mapToText()) { front, back -> front + back }

    val keyIntOffsets = mutableStateListOf<MutableState<IntOffset>>()
    val valueIntOffsets = mutableStateListOf<MutableState<IntOffset>>()

    private val currentFace = MutableStateFlow(Faces.FRONT)

    private val _imageUrl = MutableStateFlow<GlideUrl?>(null)
    val imageUrl = _imageUrl.asStateFlow()

    fun onAddFrontFieldStarted() = onAddFieldStarted(Faces.FRONT)
    fun onAddBackFieldStarted() = onAddFieldStarted(Faces.BACK)

    private fun onAddFieldStarted(face: Faces) {
        keyIntOffsets.clear()
        valueIntOffsets.clear()
        keyText.tryEmit("")

        currentFace.tryEmit(face)
        currentSizeInPx = Pair(1, 1)
        when (currentFace.value) {
            Faces.FRONT -> getFrontImageUrl()
            Faces.BACK -> getBackImageUrl()
        }.let { _imageUrl.tryEmit(it) }

        _creationState.tryEmit(TemplateCreationState.ADD_FIELD_KEY)
    }

    fun onAddFieldValue() {
        if (keyText.value.isNotBlank()) {
            _creationState.tryEmit(TemplateCreationState.ADD_FIELD_VALUE)
        }
    }

    fun onAddFieldFinished() {
        viewModelScope.launch {
            val newTemplateField = TemplateField(
                text = keyText.value,
                textCoords = keyIntOffsets.getConvertedOffsets(),
                valueCoords = valueIntOffsets.getConvertedOffsets()
            )
            currentFields.value += newTemplateField
            _creationState.tryEmit(TemplateCreationState.START)
        }
    }

    private val keyText = MutableStateFlow("")

    fun onKeyTextChanged(newValue: String) {
        keyText.tryEmit(newValue)
    }

    private val _templateName = MutableStateFlow("")
    val templateName = _templateName.asStateFlow()

    fun onTemplateNameChanged(newValue: String) {
        _templateName.tryEmit(newValue)
    }

    private val json by lazy {
        Json { prettyPrint = true }
    }

    fun onSaveTemplate() {
        viewModelScope.launch {
            _creationState.tryEmit(TemplateCreationState.LOADING)
            val resultMap = mapOf(
                "front" to frontFields.value,
                "back" to backFields.value
            )
            val templateString = json.encodeToString(resultMap)
            val newDocumentTemplate = DocumentTemplate(
                name = templateName.value,
                jsonTemplate = templateString
            )
            onSendTemplateToBackend(newDocumentTemplate)
        }
    }

    private val _creationResult = MutableStateFlow<String?>(null)
    val creationResult = _creationResult.asStateFlow()

    private fun onSendTemplateToBackend(documentTemplate: DocumentTemplate) {
        val call = ApiService.getInstance().createNewDocumentTemplate(documentTemplate)
        call?.enqueue(object : Callback<ResponseBody> {

            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                if (response.isSuccessful) {
                    _creationResult.tryEmit("Template was created successfully!")
                } else {
                    _creationResult.tryEmit("Error! Please try again! ${response.message()}")
                }
                _creationState.tryEmit(TemplateCreationState.RESULT)
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                _creationResult.tryEmit("Error! Please try again. ${t.message}")
                _creationState.tryEmit(TemplateCreationState.RESULT)
            }
        })
    }

    override fun onResult() {
        detectionState.value = DetectionState.RESULT
    }

    private var currentSizeInPx: Pair<Int, Int> = Pair(1, 1)

    fun initializeIntOffsets(intOffsets: SnapshotStateList<MutableState<IntOffset>>, width: Int, height: Int) {
        if (currentSizeInPx.first == width && currentSizeInPx.second == height && intOffsets.isNotEmpty()) {
            return
        }
        currentSizeInPx = Pair(width, height)
        if (intOffsets.isEmpty()) {
            defaultOffsets.forEach { offset ->
                intOffsets.add(
                    mutableStateOf(
                        IntOffset(
                            (width * offset.first).roundToInt(), (height * offset.second).roundToInt()
                        )
                    )
                )
            }
        } else defaultOffsets.forEachIndexed { index, offset ->
            intOffsets[index].value = IntOffset(
                (width * offset.first).roundToInt(), (height * offset.second).roundToInt()
            )
        }
    }

    private fun List<State<IntOffset>>.getConvertedOffsets(): List<Coordinate> {
        val offsets = mutableListOf<Coordinate>()
        forEach { intOffset ->
            val x = (intOffset.value.x.toFloat() / currentSizeInPx.first)
            val y = (intOffset.value.y.toFloat() / currentSizeInPx.second)
            offsets += Coordinate(x, y)
        }
        return offsets
    }

    private fun Flow<List<TemplateField>>.mapToText(): Flow<List<String>> = map { it.map { it.text } }

    companion object {
        private val defaultOffsets = listOf(
            0.40f to 0.45f, 0.40f to 0.55f, 0.60f to 0.55f, 0.60f to 0.45f
        )

        private enum class Faces {
            FRONT, BACK
        }
    }
}
