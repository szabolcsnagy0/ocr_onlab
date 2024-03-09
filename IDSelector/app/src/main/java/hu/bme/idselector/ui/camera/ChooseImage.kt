package hu.bme.idselector.ui.camera

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import hu.bme.idselector.R

/**
 * Composable function to choose an image from the gallery or take a photo with the camera.
 * @param onConfirmation: (uri: Uri?,  path: String?) -> Unit - callback function to be called when an image is selected
 * @param onDismiss: () -> Unit - callback function to be called when the dialog is dismissed
 */
@Composable
fun ChooseImage(
    onConfirmation: (uri: Uri?, path: String?) -> Unit,
) {
    var hasImage by remember {
        mutableStateOf(false)
    }

    var cameraClicked by remember {
        mutableStateOf(false)
    }
    var imageUri by remember {
        mutableStateOf<Uri?>(null)
    }

    var imagePath by remember {
        mutableStateOf<String?>(null)
    }

    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture(),
        onResult = { success ->
            hasImage = success
        }
    )

    val context = LocalContext.current
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxSize()
    ) {
        Button(onClick = { cameraClicked = true }, modifier = Modifier.size(150.dp, 80.dp)) {
            Text(text = "CAMERA")
        }
    }

    if (cameraClicked && cameraAccess()) {
        cameraClicked = false
        val (uri, path) = CameraFileProvider.getImageUriAndPath(context)
        imageUri = uri
        imagePath = path
        cameraLauncher.launch(uri)
    }

    if (hasImage && imageUri != null) {
        onConfirmation(imageUri, imagePath)
    }
}