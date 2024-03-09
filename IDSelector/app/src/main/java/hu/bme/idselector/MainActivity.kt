package hu.bme.idselector

import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.semantics.Role.Companion.Image
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.toOffset
import androidx.compose.ui.zIndex
import coil.compose.rememberAsyncImagePainter
import hu.bme.idselector.ui.camera.CameraFileProvider
import hu.bme.idselector.ui.camera.ChooseImage
import hu.bme.idselector.ui.camera.cameraAccess
import hu.bme.idselector.ui.theme.IDSelectorTheme
import java.io.File
import kotlin.math.roundToInt

class MainActivity : ComponentActivity() {
    val viewModel = MainViewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            IDSelectorTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    var showRect by remember {
                        mutableStateOf(false)
                    }
                    
                    var imageUri by remember {
                        mutableStateOf<Uri?>(null)
                    }

                    var imagePath by remember {
                        mutableStateOf<String?>(null)
                    }
                    if(imageUri != null && imagePath != null) {
                        Log.i("corners", imagePath.toString())
                        ShowImage(imageUri!!)
                        viewModel.uploadPicture(File(imagePath!!)) { success,_ ->
                            showRect = success
                        }
                    }
                    else {
                        ChooseImage { uri: Uri?, path: String? ->
                            imageUri = uri
                            imagePath = path
                        }
                    }
                    
                    if(showRect) {
                        Rectangle(viewModel = viewModel)
                    }
                }
            }
        }
    }
}

@Composable
fun Rectangle(viewModel: MainViewModel) {
    val CIRCLE_SIZE = 35.dp
    val lineOffset = IntOffset(CIRCLE_SIZE.value.roundToInt(), CIRCLE_SIZE.value.roundToInt())
    val offsetHandle1 = viewModel.offsetHandle1
    val offsetHandle2 = viewModel.offsetHandle2
    val offsetHandle3 = viewModel.offsetHandle3
    val offsetHandle4 = viewModel.offsetHandle4
    val handleModifier = Modifier
        .size(CIRCLE_SIZE)
        .zIndex(1f)
    Box {
        Handle(offset = offsetHandle1, modifier = handleModifier)
        Handle(offset = offsetHandle2, modifier = handleModifier)
        Handle(offset = offsetHandle3, modifier = handleModifier)
        Handle(offset = offsetHandle4, modifier = handleModifier)
        Canvas(
            modifier = Modifier
                .fillMaxSize()
        ) {
            drawLine(
                start = offsetHandle1.value.plus(lineOffset).toOffset(),
                end = offsetHandle2.value.plus(lineOffset).toOffset(),
                color = Color.Gray,
                strokeWidth = 3f
            )
            drawLine(
                start = offsetHandle2.value.plus(lineOffset).toOffset(),
                end = offsetHandle3.value.plus(lineOffset).toOffset(),
                color = Color.Gray,
                strokeWidth = 3f
            )
            drawLine(
                start = offsetHandle3.value.plus(lineOffset).toOffset(),
                end = offsetHandle4.value.plus(lineOffset).toOffset(),
                color = Color.Gray,
                strokeWidth = 3f
            )
            drawLine(
                start = offsetHandle4.value.plus(lineOffset).toOffset(),
                end = offsetHandle1.value.plus(lineOffset).toOffset(),
                color = Color.Gray,
                strokeWidth = 3f
            )
        }
    }
}

@Composable
fun Handle(modifier: Modifier = Modifier, offset: MutableState<IntOffset>) {
    Box(
        modifier = modifier
            .offset { offset.value }
            .pointerInput(Unit) {
                detectDragGestures { change, dragAmount ->
                    change.consume()
                    val offsetChange =
                        IntOffset(dragAmount.x.roundToInt(), dragAmount.y.roundToInt())
                    offset.value = offset.value.plus(offsetChange)
                }
            }
            .background(
                color = Color(Color.Gray.red, Color.Gray.green, Color.Gray.blue, 1f),
                shape = CircleShape
            )
    )
}

@Composable
fun ShowImage(uri: Uri) {
    Image(
        modifier = Modifier.fillMaxSize(),
        painter = rememberAsyncImagePainter(uri),
        contentDescription = null,
        contentScale = ContentScale.Crop
    )
}