package hu.bme.idselector.ui.shared

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import hu.bme.idselector.R

/**
 * Component for a login form.
 * @param modifier The modifier of the component.
 * @param buttonText The text of the button.
 * @param onButtonClicked The function to be called when the button is clicked.
 * @param errorText The text to be displayed if there is an error.
 */
@Composable
fun LoginForm(
    modifier: Modifier = Modifier,
    buttonText: String = "",
    onButtonClicked: (String, String) -> Unit = { _, _ -> }
) {
    var email by remember {
        mutableStateOf("")
    }
    var pass by remember {
        mutableStateOf("")
    }
    Column(modifier) {
        Column {
            Text(
                text = buttonText,
                fontSize = 30.sp,
                color = colorResource(id = R.color.black),
                fontWeight = FontWeight.Bold,
            )
            Spacer(Modifier.size(16.dp))
            TextWithInput(
                text = stringResource(R.string.email), textChanged = {
                    email = it
                },
                keyboardType = KeyboardType.Email
            )
            TextWithInput(text = stringResource(R.string.password), textChanged = {
                pass = it
            }, isPassword = true)
        }
        ElevatedButton(
            onClick = {
                onButtonClicked(email, pass)
            }, colors = ButtonDefaults.buttonColors(
                containerColor = colorResource(id = R.color.black),
                contentColor = colorResource(id = R.color.white),
                disabledContainerColor = colorResource(id = R.color.grey)
            ),
            elevation = ButtonDefaults.buttonElevation(
                defaultElevation = 5.dp,
                pressedElevation = 5.dp,
                focusedElevation = 5.dp,
                hoveredElevation = 5.dp,
                disabledElevation = 0.dp
            ),
            modifier = Modifier
                .fillMaxWidth()
                .height(70.dp)
                .padding(top = 20.dp)
        ) {
            Text(
                text = buttonText,
                fontWeight = FontWeight.SemiBold,
                fontSize = 15.sp
            )
        }
    }
}

/**
 * Component for a registration form.
 * @param modifier The modifier of the component.
 * @param buttonText The text of the button.
 * @param onButtonClicked The function to be called when the button is clicked.
 */
@Composable
fun RegistrationForm(
    modifier: Modifier = Modifier,
    buttonText: String = "",
    onButtonClicked: (email: String, pass: String) -> Unit = { _, _ -> }
) {
    var email by remember {
        mutableStateOf("")
    }
    var pass by remember {
        mutableStateOf("")
    }
    var passRep by remember {
        mutableStateOf("")
    }
    val showPass = remember {
        mutableStateOf(false)
    }

    Column(modifier = modifier)
    {
        Column {
            Text(
                text = buttonText,
                fontSize = 24.sp,
                color = colorResource(id = R.color.black),
                fontWeight = FontWeight.SemiBold
            )
            Spacer(Modifier.size(16.dp))
            TextWithInput(text = stringResource(R.string.email), textChanged = {
                email = it
            }, keyboardType = KeyboardType.Email)
            TextWithInput(
                text = stringResource(R.string.password), textChanged = {
                    pass = it
                }, isPassword = true,
                passwordVisibility = showPass
            )
            TextWithInput(
                text = stringResource(R.string.repeat_pass),
                textChanged = {
                    passRep = it
                },
                isPassword = true,
                passwordVisibility = showPass
            )
            if (pass != passRep) {
                Text(
                    text = stringResource(R.string.not_matching_passwords),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color.Red
                )
            }
            ElevatedButton(
                onClick = {
                    onButtonClicked(email, pass)
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = colorResource(id = R.color.black),
                    contentColor = colorResource(id = R.color.white),
                    disabledContainerColor = colorResource(id = R.color.grey)
                ),
                enabled = (email.isNotBlank() && pass.isNotBlank() && pass == passRep),
                elevation = ButtonDefaults.buttonElevation(
                    defaultElevation = 5.dp,
                    pressedElevation = 5.dp,
                    focusedElevation = 5.dp,
                    hoveredElevation = 5.dp,
                    disabledElevation = 0.dp
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 20.dp, bottom = 10.dp)
                    .heightIn(50.dp, 100.dp)
            ) {
                Text(
                    text = buttonText,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 15.sp
                )
            }
        }
    }
}
