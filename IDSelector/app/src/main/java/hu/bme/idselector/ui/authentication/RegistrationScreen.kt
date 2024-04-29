package hu.bme.idselector.ui.authentication

import android.widget.Toast
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import hu.bme.idselector.R
import hu.bme.idselector.data.UserRegistration
import hu.bme.idselector.navigation.Routes
import hu.bme.idselector.ui.shared.RegistrationForm
import hu.bme.idselector.viewmodels.RegistrationViewModel

/**
 * Composable function which displays the registration screen.
 * @param navController NavController which handles the navigation between screens.
 * @param viewModel RegistrationViewModel which handles the registration process.
 */
@Composable
fun RegistrationScreen(
    navController: NavController,
    viewModel: RegistrationViewModel = remember {
        RegistrationViewModel()
    }
) {
    val regResult = remember {
        mutableStateOf("")
    }

    val regSuccess = remember {
        mutableStateOf(false)
    }

    val user = remember {
        mutableStateOf(UserRegistration("", ""))
    }

    WelcomeScreen(
        inputForm = {
            RegistrationForm(
                buttonText = stringResource(id = R.string.registration),
                onButtonClicked = { email, pass ->
                    user.value = UserRegistration(
                        email = email,
                        password = pass
                    )
                    viewModel.registerUser(
                        user.value,
                        regResult,
                        regSuccess
                    )
                },
                modifier = Modifier.padding(horizontal = 20.dp)
            )
        },
        onTextButtonClicked = {
            navController.navigate(Routes.Login.route) {
                popUpTo(Routes.Login.route) {
                    inclusive = true
                }
            }
        },
        smallText = stringResource(R.string.has_account),
        textButtonText = stringResource(id = R.string.login_text),
        imageWeight = 0.7f
    )


    if (regResult.value.isNotBlank()) {
        Toast.makeText(LocalContext.current, regResult.value, Toast.LENGTH_SHORT).show()
        regResult.value = ""
    }

    if (regSuccess.value) {
        navController.navigate(Routes.Login.route) {
            popUpTo(Routes.Login.route)
        }
    }
}