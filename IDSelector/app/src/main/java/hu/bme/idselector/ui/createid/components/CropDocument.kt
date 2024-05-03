package hu.bme.idselector.ui.createid.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.FloatingActionButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import hu.bme.idselector.ui.shared.ShowImage
import hu.bme.idselector.viewmodels.NewIdViewModel

@Composable
fun CropDocument(viewModel: NewIdViewModel, onCropClick: () -> Unit) {
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