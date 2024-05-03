package hu.bme.idselector.ui.shared

import android.net.Uri
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import coil.compose.rememberAsyncImagePainter
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.bumptech.glide.load.model.GlideUrl

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun ShowImage(modifier: Modifier = Modifier, uri: Uri? = null, glideUrl: GlideUrl? = null) {
    if (glideUrl != null) {
        Log.i("img", glideUrl.toStringUrl())
        Log.i("img", glideUrl.toURL().toString())
        Log.i("img", glideUrl.headers.toString())
        GlideImage(
            model = glideUrl,
            contentDescription = null,
            modifier = modifier
                .clip(shape = RoundedCornerShape(10))
        )
    } else if (uri != null) {
        Image(
            modifier = modifier
                .fillMaxSize()
                .clip(RoundedCornerShape(10)),
            painter = rememberAsyncImagePainter(uri),
            contentDescription = null,
            contentScale = ContentScale.Inside
        )
    }
}