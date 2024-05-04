package hu.bme.idselector.ui.createid.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.toOffset
import androidx.compose.ui.zIndex
import hu.bme.idselector.viewmodels.NewDocumentViewModel

@Composable
fun Rectangle(viewModel: NewDocumentViewModel) {
    var currentHeight by remember { mutableStateOf(0.dp) }
    var currentWidth by remember { mutableStateOf(0.dp) }

    var filled by remember { mutableStateOf(false) }

    if (currentHeight != 0.dp && currentWidth != 0.dp && !filled) {
        filled = true
        viewModel.initIntOffsets(currentWidth.toPx(), currentHeight.toPx())
    }

    val offsets = remember { viewModel.intOffsets }

    val handleModifier = Modifier
        .zIndex(1f)
    Box(modifier = Modifier
        .onGloballyPositioned {
            currentWidth = it.size.width.dp / 2
            currentHeight = it.size.height.dp / 2
        }) {
        for (value in offsets) {
            Handle(offset = value, modifier = handleModifier)
        }
        Canvas(
            modifier = Modifier
                .fillMaxSize()
        ) {
            for (i in 0..<offsets.size) {
                drawLine(
                    start = offsets[i].value.toOffset(),
                    end = offsets[(i + 1) % offsets.size].value.toOffset(),
                    color = Color.Red,
                    strokeWidth = 3f
                )
            }
        }
    }
}

@Composable
fun Dp.toPx() = with(LocalDensity.current) { this@toPx.toPx() }