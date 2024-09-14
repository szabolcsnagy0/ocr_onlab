package hu.bme.idselector.ui.idlist

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.bumptech.glide.load.model.GlideUrl
import hu.bme.idselector.ui.shared.ShowImage

@Composable
fun ImageIdCard(frontUrl: GlideUrl?, backUrl: GlideUrl?, modifier: Modifier = Modifier) {
    var rotated by remember { mutableStateOf(false) }

    if (frontUrl == null) {
        rotated = true
    }

    if (!rotated) {
        ShowImage(glideUrl = frontUrl,
            modifier = modifier
                .fillMaxWidth(0.9f)
                .heightIn(max = 200.dp)
                .clickable {
                    if (backUrl != null) {
                        rotated = !rotated
                    }
                }
        )
    } else {
        ShowImage(
            glideUrl = backUrl,
            modifier = modifier
                .fillMaxWidth(0.9f)
                .heightIn(max = 200.dp)
                .clickable {
                    if (frontUrl != null) {
                        rotated = !rotated
                    }
                }
        )
    }
}
