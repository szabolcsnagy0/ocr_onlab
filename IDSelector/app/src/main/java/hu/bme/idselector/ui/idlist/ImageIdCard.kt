package hu.bme.idselector.ui.idlist

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.bumptech.glide.load.model.GlideUrl
import hu.bme.idselector.api.ApiService
import hu.bme.idselector.data.OtherId
import hu.bme.idselector.ui.shared.ShowImage

@Composable
fun ImageIdCard(frontUrl: GlideUrl, backUrl: GlideUrl, modifier: Modifier = Modifier) {
    var rotated by remember { mutableStateOf(false) }

    ElevatedCard(
        colors = CardDefaults.cardColors(
            containerColor = Color.Transparent
        ),
        elevation = CardDefaults.elevatedCardElevation(10.dp),
        shape = RoundedCornerShape(10),
        modifier = modifier
            .clickable {
                rotated = !rotated
            }
    ) {
        val url = if (!rotated) {
            frontUrl
        } else {
            backUrl
        }
        ShowImage(glideUrl = url, modifier = Modifier.fillMaxWidth(0.9f))
    }
}