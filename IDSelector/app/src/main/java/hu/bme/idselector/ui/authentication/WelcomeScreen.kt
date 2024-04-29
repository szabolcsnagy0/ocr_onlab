package hu.bme.idselector.ui.authentication

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImagePainter
import coil.compose.rememberAsyncImagePainter
import hu.bme.idselector.R

/**
 * Composable function for the welcome screen.
 * @param inputForm The input form composable function.
 * @param onTextButtonClicked The function to be called when the text button is clicked.
 * @param smallText The text to be displayed next to the text button.
 * @param textButtonText The text to be displayed on the text button.
 * @param modifier The modifier to be applied to the composable.
 * @param imageWeight The weight of the image.
 */
@Composable
fun WelcomeScreen(
    modifier: Modifier = Modifier,
    inputForm: @Composable () -> Unit = {},
    onTextButtonClicked: () -> Unit = {},
    smallText: String = "",
    textButtonText: String = "",
    imageWeight: Float = 0.7f
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center, modifier = modifier
            .fillMaxWidth()
            .fillMaxHeight()
    ) {
        Image(
            painter = painterResource(id = R.drawable.identity_text),
            contentDescription = stringResource(R.string.identity_text),
            modifier = modifier
                .fillMaxSize()
                .padding(horizontal = 50.dp)
                .weight(imageWeight)
        )
        Column(
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .weight(2f)
                .verticalScroll(rememberScrollState()),
        ) {
            Spacer(Modifier.size(16.dp))
            inputForm()
            Row(
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = smallText,
                    color = colorResource(id = R.color.grey)
                )
                TextButton(onClick = {
                    onTextButtonClicked()
                }) {
                    Text(
                        text = textButtonText,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        color = colorResource(id = R.color.black)
                    )
                }
            }
        }
    }
}