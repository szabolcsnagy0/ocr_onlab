package hu.bme.idselector.ui.shared

import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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
        val placeHolderModifier: MutableState<Modifier> = remember { mutableStateOf(Modifier) }
        GlideImage(
            model = glideUrl,
            contentDescription = null,
            loading = placeholder { Loading(placeHolderModifier.value) },
            failure = placeholder { Failure(placeHolderModifier.value) },
            contentScale = ContentScale.FillHeight,
            modifier = modifier
                .clip(shape = RoundedCornerShape(10))
                .onGloballyPositioned {
                    placeHolderModifier.value = Modifier.size(height = it.size.height.dp, width = it.size.width.dp)
                }
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

@Composable
private fun Loading(modifier: Modifier = Modifier) {
    Box(
        modifier
            .background(colorResource(R.color.grey).copy(alpha = 0.5f))
    ) {
        CircularProgressIndicator(
            modifier = Modifier
                .size(30.dp)
                .align(Alignment.Center),
            color = MaterialTheme.colorScheme.secondary,
            trackColor = MaterialTheme.colorScheme.surfaceVariant,
        )
    }
}

@Composable
private fun Failure(modifier: Modifier = Modifier) {
    Box(
        modifier
            .background(colorResource(R.color.grey))
    ) {
        Text(
            text = "The document only has a single side!",
            fontWeight = FontWeight.ExtraBold,
            fontSize = 25.sp,
            textAlign = TextAlign.Center,
            color = colorResource(
                id = R.color.white
            ),
            modifier = Modifier.align(Alignment.Center)
        )
    }
}
