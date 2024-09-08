package hu.bme.idselector.ui.createid.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bumptech.glide.load.model.GlideUrl
import hu.bme.idselector.R
import hu.bme.idselector.ui.shared.ShowImage

@Preview()
@Composable
fun ImagePreview(
    text: String = "Front image",
    glideUrl: GlideUrl? = null,
    imageMaxHeight: Dp = 200.dp,
    onClick: () -> Unit = {}
) {
    Box(
        modifier = Modifier
            .padding(horizontal = 10.dp),
        contentAlignment = Alignment.Center
    ) {
        if (glideUrl != null) {
            ShowImage(
                glideUrl = glideUrl, modifier = Modifier
                    .clickable { onClick() }
                    .fillMaxWidth(0.9f)
                    .heightIn(max = imageMaxHeight)
            )
        } else {
            ElevatedButton(
                onClick = onClick,
                colors = ButtonDefaults.buttonColors(
                    containerColor = colorResource(id = R.color.grey),
                    contentColor = colorResource(id = R.color.white),
                ),
                elevation = ButtonDefaults.buttonElevation(
                    defaultElevation = 5.dp,
                    pressedElevation = 5.dp,
                    focusedElevation = 5.dp,
                    hoveredElevation = 5.dp,
                    disabledElevation = 0.dp
                ),
                shape = RoundedCornerShape(10),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 20.dp, bottom = 10.dp)
                    .heightIn(150.dp)
            ) {
                Text(text = text, fontSize = 30.sp, fontWeight = FontWeight.SemiBold)
            }
        }
    }
}
