package hu.bme.idselector.ui.createid.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import hu.bme.idselector.R
import hu.bme.idselector.ui.shared.MultiFloatingActionButton
import hu.bme.idselector.ui.shared.ShowImage
import hu.bme.idselector.viewmodels.createid.NewDocumentViewModel

@Composable
fun CropDocument(viewModel: NewDocumentViewModel, onCropClick: () -> Unit) {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Box(modifier = Modifier.size(320.dp), contentAlignment = Alignment.TopStart) {
            ShowImage(uri = viewModel.selectedImageUri.value)
            Rectangle(viewModel = viewModel)
        }
        FloatingActionButton(
            onClick = {
                onCropClick()
            },
            containerColor = colorResource(id = R.color.grey),
            contentColor = colorResource(id = R.color.white),
            elevation = FloatingActionButtonDefaults.bottomAppBarFabElevation(),
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(12.dp)
        ) {
            Icon(imageVector = Icons.Filled.Check, contentDescription = null)
        }
    }
}