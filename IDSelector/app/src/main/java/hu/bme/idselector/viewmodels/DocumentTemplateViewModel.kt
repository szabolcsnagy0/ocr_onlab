package hu.bme.idselector.viewmodels

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.unit.IntOffset
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import hu.bme.idselector.api.ApiService
import hu.bme.idselector.data.Coordinate
import hu.bme.idselector.data.TemplateField
import hu.bme.idselector.ui.idtemplate.states.TemplateCreationState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import kotlin.math.roundToInt

class DocumentTemplateViewModel : ViewModel() {

    private val _creationState = MutableStateFlow(TemplateCreationState.START)
    val creationState = _creationState.asStateFlow()

    private val templateFields: MutableStateFlow<List<TemplateField>> = MutableStateFlow(emptyList())

    val templateFieldNames = templateFields.map { it.map { it.text } }

    val imageUrl by lazy {
        ApiService.getImageUrl("front.jpg")
    }

    private val offsets = listOf(
        0.40f to 0.45f,
        0.40f to 0.55f,
        0.60f to 0.55f,
        0.60f to 0.45f
    )
    val keyIntOffsets = mutableStateListOf<MutableState<IntOffset>>()
    val valueIntOffsets = mutableStateListOf<MutableState<IntOffset>>()

    fun onAddFieldStarted() {
        _creationState.tryEmit(TemplateCreationState.ADD_FIELD_KEY)
    }

    fun onAddFieldValue() {
        _creationState.tryEmit(TemplateCreationState.ADD_FIELD_VALUE)
    }

    fun onAddFieldFinished() {
        viewModelScope.launch {
            val newTemplateField = TemplateField(
                text = keyText.value,
                textCoords = keyIntOffsets.getConvertedOffsets(),
                valueCoords = valueIntOffsets.getConvertedOffsets()
            )
            templateFields.value += newTemplateField
            _creationState.tryEmit(TemplateCreationState.START)
        }
    }

    private val keyText = MutableStateFlow("")

    fun onKeyTextChanged(newValue: String) {
        keyText.tryEmit(newValue)
    }

    private val _templateString = MutableStateFlow("")
    val templateString = _templateString.asStateFlow()

    private val json by lazy {
        Json { prettyPrint = true }
    }

    fun onSaveTemplate() {
        json.encodeToString(templateFields.value).let { _templateString.tryEmit(it) }
        _creationState.tryEmit(TemplateCreationState.RESULT)
    }

    private var currentSizeInPx: Pair<Int, Int> = Pair(1, 1)

    fun initializeIntOffsets(intOffsets: SnapshotStateList<MutableState<IntOffset>>, width: Int, height: Int) {
        if (currentSizeInPx.first == width && currentSizeInPx.second == height && intOffsets.isNotEmpty()) {
            return
        }
        currentSizeInPx = Pair(width, height)
        if (intOffsets.isEmpty()) {
            offsets.forEach { offset ->
                intOffsets.add(
                    mutableStateOf(
                        IntOffset(
                            (width * offset.first).roundToInt(),
                            (height * offset.second).roundToInt()
                        )
                    )
                )
            }
        } else offsets.forEachIndexed { index, offset ->
            intOffsets[index].value =
                IntOffset(
                    (width * offset.first).roundToInt(),
                    (height * offset.second).roundToInt()
                )
        }
        Log.i("rec", intOffsets.toList().toString())
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
}
