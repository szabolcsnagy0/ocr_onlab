package hu.bme.idselector.ui.shared

import android.net.Uri
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.ColorPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import coil.compose.rememberAsyncImagePainter
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.bumptech.glide.integration.compose.placeholder
import com.bumptech.glide.load.model.GlideUrl
import hu.bme.idselector.R

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun ShowImage(modifier: Modifier = Modifier, uri: Uri? = null, glideUrl: GlideUrl? = null) {
    if (glideUrl != null) {
        GlideImage(
            model = glideUrl,
            contentDescription = null,
            loading = placeholder(ColorPainter(Color.Transparent)),
            contentScale = ContentScale.FillWidth,
            modifier = modifier
                .clip(shape = RoundedCornerShape(10))
        )
    } else if (uri != null) {
        Image(
            modifier = modifier
                .clip(RoundedCornerShape(10)),
            painter = rememberAsyncImagePainter(uri),
            contentDescription = null,
            contentScale = ContentScale.Inside
        )
    }
}