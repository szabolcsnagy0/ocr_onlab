package hu.bme.idselector.ui.camera

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import hu.bme.idselector.R

@Composable
fun ChooseImage(
    content: @Composable (onClick: () -> Unit) -> Unit,
    onConfirmation: (uri: Uri?, path: String?) -> Unit
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

    content {
        cameraClicked = true
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