package hu.bme.idselector.ui.idlist

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.sp

@Composable
fun IdField(
    title: String,
    value: String?,
    modifier: Modifier = Modifier,
    textModifier: Modifier = Modifier,
    horizontalAlignment: Alignment.Horizontal = Alignment.Start,
    fontSize: TextUnit = 13.sp
) {
    Column(
        horizontalAlignment = horizontalAlignment,
        modifier = modifier
    ) {
        Text(
            text = "$title:",
            fontWeight = FontWeight.Light,
            fontSize = fontSize,
            lineHeight = fontSize * 1.1f,
            modifier = textModifier,
        )
        value?.let {
            Text(
                text = it,
                modifier = textModifier,
                fontSize = fontSize * 1.33,
                lineHeight = fontSize * 1.5,
                fontWeight = FontWeight.Bold
            )
        }
    }
}