package hu.bme.idselector.ui.createid.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bumptech.glide.load.model.GlideUrl
import hu.bme.idselector.ui.shared.ShowImage

@Composable
fun ImagePreview(text: String, glideUrl: GlideUrl? = null, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .padding(horizontal = 10.dp),
        contentAlignment = Alignment.Center
    ) {
        if (glideUrl != null) {
            ShowImage(glideUrl = glideUrl, modifier = Modifier.clickable { onClick() })
        } else {
            Button(onClick = onClick) {
                Text(text = text, fontSize = 30.sp)
            }
        }
    }
}