package hu.bme.idselector.viewmodels

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.unit.IntOffset
import androidx.lifecycle.ViewModel
import hu.bme.idselector.api.ApiService
import hu.bme.idselector.ui.idtemplate.states.TemplateCreationState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlin.math.roundToInt

class DocumentTemplateViewModel : ViewModel() {

    private val _creationState = MutableStateFlow(TemplateCreationState.START)
    val creationState = _creationState.asStateFlow()

    val imageUrl by lazy {
        ApiService.getImageUrl("front.jpg")
    }

    private val offsets = mutableListOf<Pair<Float, Float>>(
        0.40f to 0.45f,
        0.40f to 0.55f,
        0.60f to 0.55f,
        0.60f to 0.45f
    )
    val intOffsets = mutableStateListOf<MutableState<IntOffset>>()

    fun onAddFieldValue() {
        _creationState.tryEmit(TemplateCreationState.ADD_FIELD_VALUE)
    }

    fun onAddFieldKey() {
        _creationState.tryEmit(TemplateCreationState.ADD_FIELD_KEY)
    }

    private val keyText = MutableStateFlow("")

    fun onKeyTextChanged(newValue: String) {
        keyText.tryEmit(newValue)
    }

    private var currentSizeInPx: Pair<Int, Int> = Pair(1, 1)

    fun initializeIntOffsets(width: Int, height: Int) {
        if (currentSizeInPx.first == width && currentSizeInPx.second == height) {
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
}
