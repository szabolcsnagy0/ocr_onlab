package hu.bme.idselector.ui.createid.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.toOffset
import androidx.compose.ui.zIndex

@Composable
fun Rectangle(
    offsets: MutableList<MutableState<IntOffset>>,
    modifier: Modifier = Modifier,
    color: Color = Color(0xFFF2BB3E)
) {
    Box(modifier)
    {
        for (value in offsets) {
            Handle(offset = value, modifier = Modifier.zIndex(1f), color = color)
        }
        Canvas(
            modifier = modifier
        ) {
            for (i in offsets.indices) {
                drawLine(
                    start = offsets[i].value.toOffset(),
                    end = offsets[(i + 1) % offsets.size].value.toOffset(),
                    color = color,
                    strokeWidth = 4f
                )
            }
        }
    }
}
