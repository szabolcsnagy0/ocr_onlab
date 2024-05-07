package hu.bme.idselector.ui.shared

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import hu.bme.idselector.R

/**
 * Custom input field
 * @param modifier: Modifier of the input field
 * @param text: Text of the input field
 * @param textChanged: Function to call when the text is changed
 * @param keyboardType: Type of the keyboard
 * @param isPassword: Is the input field a password field
 * @param passwordVisibility: State of the password visibility
 */
@Composable
fun TextWithInput(
    modifier: Modifier = Modifier,
    text: String = "",
    textChanged: (String) -> Unit,
    keyboardType: KeyboardType = KeyboardType.Text,
    isPassword: Boolean = false,
    passwordVisibility: MutableState<Boolean> = remember { mutableStateOf(false) }
) {
    var input by remember {
        mutableStateOf("")
    }
    if (isPassword) {
        OutlinedTextField(
            value = input,
            onValueChange = {
                input = it
                textChanged(input)
            },
            placeholder = {
                Text(
                    text = text,
                    color = colorResource(id = R.color.grey),
                )
            },
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = colorResource(id = R.color.white),
                unfocusedContainerColor = colorResource(id = R.color.white),
                disabledContainerColor = colorResource(id = R.color.white),
            ),
            modifier = modifier
                .defaultMinSize(minHeight = 35.dp)
                .fillMaxWidth()
                .padding(vertical = 10.dp),
            singleLine = true,
            shape = CircleShape,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            visualTransformation = if (passwordVisibility.value) VisualTransformation.None else PasswordVisualTransformation(),
            trailingIcon = {
                val image = if (passwordVisibility.value) Icons.Filled.Visibility
                else Icons.Filled.VisibilityOff
                IconButton(onClick = { passwordVisibility.value = !passwordVisibility.value }) {
                    Icon(imageVector = image, "")
                }
            },
        )
    } else {
        OutlinedTextField(
            value = input,
            onValueChange = {
                input = it
                textChanged(input)
            },
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = colorResource(id = R.color.white),
                unfocusedContainerColor = colorResource(id = R.color.white),
                disabledContainerColor = colorResource(id = R.color.white),
            ),
            modifier = modifier
                .defaultMinSize(minHeight = 35.dp)
                .fillMaxWidth()
                .padding(vertical = 10.dp),
            placeholder = {
                Text(
                    text = text,
                    color = colorResource(id = R.color.grey),
                )
            },
            singleLine = true,
            shape = CircleShape,
            keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
        )
    }
}

/**
 * Custom input field with title
 * @param titleText: Title of the input field
 * @param prevText: Previous text of the input field
 * @param onValueChange: Function to call when the text is changed
 * @param modifier: Modifier of the input field
 */
@Composable
fun EditTextWithTitle(
    titleText: String,
    prevText: String,
    keyboardType: KeyboardType = KeyboardType.Text,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {

    Column(modifier = modifier){
        Text(
            text = titleText,
            fontWeight = FontWeight.SemiBold,
            color = colorResource(R.color.black),
            fontSize = 18.sp,
            modifier = Modifier.fillMaxWidth()
        )
        OutlinedTextField(
            value = prevText, onValueChange = {
                onValueChange(it)
            },
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = colorResource(id = R.color.white),
                unfocusedContainerColor = colorResource(id = R.color.white),
                disabledContainerColor = colorResource(id = R.color.white),
            ),
            modifier = Modifier
                .defaultMinSize(minHeight = 35.dp)
                .padding(vertical = 10.dp)
                .fillMaxWidth(),
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            shape = CircleShape
        )
    }
}