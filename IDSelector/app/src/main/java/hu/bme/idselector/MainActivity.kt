package hu.bme.idselector

import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.unit.toOffset
import androidx.compose.ui.zIndex
import coil.compose.rememberAsyncImagePainter
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import hu.bme.idselector.data.Person
import hu.bme.idselector.navigation.Navigation
import hu.bme.idselector.ui.ProfileDetails
import hu.bme.idselector.ui.ProfileElement
import hu.bme.idselector.ui.ProfileList
import hu.bme.idselector.ui.camera.ChooseImage
import hu.bme.idselector.ui.theme.IDSelectorTheme
import kotlin.math.roundToInt

class MainActivity : ComponentActivity() {
    private val viewModel = MainViewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            IDSelectorTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = colorResource(R.color.orange)
                ) {
                    Navigation()

//                    val appState by remember { viewModel.appState }
//                    when (appState) {
//                        AppState.START -> {
//                            Column(
//                                modifier = Modifier.fillMaxSize(),
//                                verticalArrangement = Arrangement.SpaceEvenly,
//                                horizontalAlignment = Alignment.CenterHorizontally
//                            ) {
//                                ChooseImage(content = { onClick ->
//                                    ImagePreview(
//                                        text = "FRONT",
//                                        onClick = onClick,
//                                        glideUrl = viewModel.getFrontImageUrl()
//                                    )
//                                }) { uri: Uri?, path: String? ->
//                                    viewModel.selectedImage.value = ImageState.FRONT
//                                    viewModel.selectedImageUri.value = uri
//                                    viewModel.selectedImagePath.value = path
//                                    viewModel.detectCorners()
//                                }
//                                ChooseImage(content = { onClick ->
//                                    ImagePreview(
//                                        text = "BACK",
//                                        onClick = onClick,
//                                        glideUrl = viewModel.getBackImageUrl()
//                                    )
//                                }) { uri: Uri?, path: String? ->
//                                    viewModel.selectedImage.value = ImageState.BACK
//                                    viewModel.selectedImageUri.value = uri
//                                    viewModel.selectedImagePath.value = path
//                                    viewModel.detectCorners()
//                                }
//                                Button(onClick = {
//                                    viewModel.detectText()
//                                }, modifier = Modifier.size(150.dp, 70.dp)) {
//                                    Text(text = "OK")
//                                }
//                            }
//                        }
//
//                        AppState.LOADING -> {
//                            Box(
//                                modifier = Modifier
//                                    .fillMaxSize()
//                                    .padding(20.dp)
//                            ) {
//                                CircularProgressIndicator(
//                                    modifier = Modifier
//                                        .size(30.dp)
//                                        .align(Alignment.Center),
//                                    color = MaterialTheme.colorScheme.secondary,
//                                    trackColor = MaterialTheme.colorScheme.surfaceVariant,
//                                )
//                            }
//                        }
//
//                        AppState.CROP -> {
//                            CropID(viewModel = viewModel) {
//                                viewModel.cropPicture()
//                            }
//                        }
//
//                        AppState.RESULT -> {
//                            val person = viewModel.person.value
//                            if (person != null)
//                                PersonData(person = person)
//                        }
//                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PersonData(
    person: Person = Person(
        "Test",
        "Test",
        "Test",
        "Test",
        "Test",
        "Test",
        "Test",
        "test",
        "test",
        "Test",
        "test"
    )
) {
    Column(modifier = Modifier
        .verticalScroll(rememberScrollState())
        .padding(10.dp)) {
        DataFieldWithTitle(resourceId = R.string.name, text = person.name)
        DataFieldWithTitle(resourceId = R.string.sex, text = person.sex)
        DataFieldWithTitle(resourceId = R.string.nationality, text = person.nationality)
        DataFieldWithTitle(resourceId = R.string.dateOfBirth, text = person.dateOfBirth)
        DataFieldWithTitle(resourceId = R.string.dateOfExpiry, text = person.dateOfExpiry)
        DataFieldWithTitle(resourceId = R.string.documentNr, text = person.documentNr)
        DataFieldWithTitle(resourceId = R.string.can, text = person.can)
        DataFieldWithTitle(resourceId = R.string.placeOfBirth, text = person.placeOfBirth)
        DataFieldWithTitle(resourceId = R.string.mothersName, text = person.mothersName)
        DataFieldWithTitle(resourceId = R.string.nameAtBirth, text = person.nameAtBirth)
        DataFieldWithTitle(resourceId = R.string.authority, text = person.authority)
    }
}

@Composable
fun DataFieldWithTitle(resourceId: Int, text: String?) {
    if (text != null) {
        Text(
            text = stringResource(id = resourceId),
            fontWeight = FontWeight.Bold,
            fontSize = 20.sp
        )
        Text(text, fontSize = 15.sp, modifier = Modifier.padding(bottom = 15.dp))
    }
}

@Composable
fun CropID(viewModel: MainViewModel, onCropClick: () -> Unit) {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Box(modifier = Modifier.size(320.dp), contentAlignment = Alignment.TopStart) {
            ShowImage(uri = viewModel.selectedImageUri.value)
            Rectangle(viewModel = viewModel)
        }
        FloatingActionButton(
            onClick = {
                onCropClick()
            },
            Modifier
                .align(Alignment.BottomEnd)
                .padding(12.dp)
        ) {
            Image(imageVector = Icons.Filled.Check, contentDescription = null)
        }
    }
}

@Composable
fun Rectangle(viewModel: MainViewModel) {
    var currentHeight by remember { mutableStateOf(0.dp) }
    var currentWidth by remember { mutableStateOf(0.dp) }

    var filled by remember { mutableStateOf(false) }

    if (currentHeight != 0.dp && currentWidth != 0.dp && !filled) {
        filled = true
        viewModel.initIntOffsets(currentWidth.toPx(), currentHeight.toPx())
    }

    val offsets = remember { viewModel.intOffsets }

    val handleModifier = Modifier
        .zIndex(1f)
    Box(modifier = Modifier
        .onGloballyPositioned {
            currentWidth = it.size.width.dp / 2
            currentHeight = it.size.height.dp / 2
        }) {
        for (value in offsets) {
            Handle(offset = value, modifier = handleModifier)
        }
        Canvas(
            modifier = Modifier
                .fillMaxSize()
        ) {
            for (i in 0..<offsets.size) {
                drawLine(
                    start = offsets[i].value.toOffset(),
                    end = offsets[(i + 1) % offsets.size].value.toOffset(),
                    color = Color.Red,
                    strokeWidth = 3f
                )
            }
        }
    }
}

@Composable
fun Handle(modifier: Modifier = Modifier, offset: MutableState<IntOffset>) {
    val inputSize = 150.dp
    val circleSize = 15.dp
    val offsetToCenter = IntOffset(circleSize.value.roundToInt(), circleSize.value.roundToInt())
    Box(
        modifier = modifier
            .size(inputSize)
            .offset { offset.value.minus(offsetToCenter) }
            .pointerInput(Unit) {
                detectDragGestures { change, dragAmount ->
                    change.consume()
                    val offsetChange =
                        IntOffset(dragAmount.x.roundToInt(), dragAmount.y.roundToInt())
                    offset.value = offset.value.plus(offsetChange)
                }
            }
//            .background(
//                    color = Color(Color.Red.red, Color.Red.green, Color.Red.blue, 0.1f),
//                    shape = CircleShape
//                )
    ) {
        Box(
            modifier = modifier
                .size(circleSize)
                .background(
                    color = Color(Color.Red.red, Color.Red.green, Color.Red.blue, 1f),
                    shape = CircleShape
                )
        )
    }
}

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun ShowImage(uri: Uri? = null, glideUrl: String? = null, modifier: Modifier = Modifier) {
    if (glideUrl != null) {
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

@Composable
fun ImagePreview(text: String, glideUrl: String? = null, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .padding(horizontal = 10.dp),
        contentAlignment = Alignment.Center
    ) {
        if (glideUrl != null) {
            ShowImage(glideUrl = glideUrl, modifier = Modifier.clickable { onClick() })
        } else {
            Button(onClick = onClick) {
                Text(text = text, fontSize = 30.sp)
            }
        }
    }
}

@Composable
fun Dp.toPx() = with(LocalDensity.current) { this@toPx.toPx() }

@Composable
fun Int.toDp() = with(LocalDensity.current) { this@toDp.toDp() }