package hu.bme.idselector.ui.createid

import android.net.Uri
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import hu.bme.idselector.R
import hu.bme.idselector.ui.createid.components.CropDocument
import hu.bme.idselector.ui.createid.components.ImagePreview
import hu.bme.idselector.ui.createid.states.DetectionState
import hu.bme.idselector.ui.createid.states.ImageState
import hu.bme.idselector.ui.shared.camera.ChooseImage
import hu.bme.idselector.viewmodels.NewDocumentViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewDocumentScreen(
    viewModel: NewDocumentViewModel,
    onCancelled: () -> Unit,
    onResult: () -> Unit = {}
) {
    val appState by remember { viewModel.detectionState }
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = stringResource(R.string.add_a_new_document),
                        fontWeight = FontWeight.ExtraBold,
                        fontSize = 30.sp,
                        color = colorResource(
                            id = R.color.white
                        )
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onCancelled) {
                        Icon(
                            imageVector = Icons.Filled.Cancel,
                            contentDescription = stringResource(
                                id = R.string.back
                            ),
                            tint = colorResource(id = R.color.white),
                            modifier = Modifier.size(35.dp)
                        )
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    titleContentColor = colorResource(id = R.color.white),
                    containerColor = colorResource(id = R.color.grey)
                ),
            )
        },
        containerColor = colorResource(id = R.color.orange)
    ) { paddingValues ->
        Column(
            modifier = Modifier.padding(paddingValues)
        ) {
            when (appState) {
                DetectionState.START -> {
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.SpaceEvenly,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        ChooseImage(content = { onClick ->
                            ImagePreview(
                                text = "FRONT",
                                onClick = onClick,
                                glideUrl = viewModel.getFrontImageUrl()
                            )
                        }) { uri: Uri?, path: String? ->
                            viewModel.selectedImage.value = ImageState.FRONT
                            viewModel.selectedImageUri.value = uri
                            viewModel.selectedImagePath.value = path
                            viewModel.detectCorners()
                        }
                        ChooseImage(content = { onClick ->
                            ImagePreview(
                                text = "BACK",
                                onClick = onClick,
                                glideUrl = viewModel.getBackImageUrl()
                            )
                        }) { uri: Uri?, path: String? ->
                            viewModel.selectedImage.value = ImageState.BACK
                            viewModel.selectedImageUri.value = uri
                            viewModel.selectedImagePath.value = path
                            viewModel.detectCorners()
                        }
                        Button(onClick = onResult, modifier = Modifier.size(150.dp, 70.dp)) {
                            Text(text = "OK")
                        }
                    }
                }

                DetectionState.LOADING -> {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(20.dp)
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

                DetectionState.CROP -> {
                    CropDocument(viewModel = viewModel) {
                        viewModel.cropPicture()
                    }
                }

                DetectionState.ERROR -> {
                    Text(text = "ERROR", fontSize = 30.sp)
                }
            }
        }
    }
}