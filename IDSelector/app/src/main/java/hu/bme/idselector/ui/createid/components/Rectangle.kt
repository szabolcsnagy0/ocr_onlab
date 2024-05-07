package hu.bme.idselector.ui.createid.components

import android.util.Log
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.toOffset
import androidx.compose.ui.zIndex
import hu.bme.idselector.viewmodels.createid.NewDocumentViewModel

@Composable
fun Rectangle(offsets: MutableList<MutableState<IntOffset>>, modifier: Modifier = Modifier) {
//fun Rectangle(viewModel: NewDocumentViewModel, modifier: Modifier = Modifier) {
//    var currentWidth by remember { mutableStateOf(0.dp) }
//    var currentHeight by remember { mutableStateOf(0.dp) }
//
//    var sizeChanged by remember { mutableStateOf(false) }
//
//    if (currentHeight != 0.dp && currentWidth != 0.dp && sizeChanged) {
//        viewModel.initIntOffsets(currentWidth.toPx(), currentHeight.toPx())
//        sizeChanged = false
//    }

    Box(
        modifier = modifier
//        .onGloballyPositioned {
//            currentWidth = it.size.width.dp / 2
//            currentHeight = it.size.height.dp / 2
//            Log.i("recsiz", currentWidth.toString())
//            Log.i("recsiz", currentHeight.toString())
//            sizeChanged = true
//        }
    )
    {
        for (value in offsets) {
            Handle(offset = value, modifier = Modifier.zIndex(1f))
        }
        Canvas(
            modifier = modifier
        ) {
            for (i in offsets.indices) {
                drawLine(
                    start = offsets[i].value.toOffset(),
                    end = offsets[(i + 1) % offsets.size].value.toOffset(),
                    color = Color(0xFFF2BB3E),
                    strokeWidth = 4f
                )
            }
        }
    }
}