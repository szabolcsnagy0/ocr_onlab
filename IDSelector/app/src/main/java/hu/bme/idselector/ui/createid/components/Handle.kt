package hu.bme.idselector.ui.createid.components

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import kotlin.math.roundToInt

@Composable
fun Handle(modifier: Modifier = Modifier, offset: MutableState<IntOffset>) {
    val inputSize = 150.dp
    val circleSize = 15.dp
    val offsetToCenter = IntOffset(circleSize.value.roundToInt(), circleSize.value.roundToInt())
    Box(
        modifier = modifier
            .size(inputSize)
            .offset { offset.value.minus(offsetToCenter) }
            .pointerInput(Unit) {
                detectDragGestures { change, dragAmount ->
                    change.consume()
                    val offsetChange =
                        IntOffset(dragAmount.x.roundToInt(), dragAmount.y.roundToInt())
                    offset.value = offset.value.plus(offsetChange)
                }
            }
//            .background(
//                    color = Color(Color.Red.red, Color.Red.green, Color.Red.blue, 0.1f),
//                    shape = CircleShape
//                )
    ) {
        Box(
            modifier = modifier
                .size(circleSize)
                .background(
                    color = Color(Color.Red.red, Color.Red.green, Color.Red.blue, 1f),
                    shape = CircleShape
                )
        )
    }
}