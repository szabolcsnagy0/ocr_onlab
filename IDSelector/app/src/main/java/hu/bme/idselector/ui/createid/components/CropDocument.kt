package hu.bme.idselector.ui.createid.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.onGloballyPositioned
import hu.bme.idselector.ui.shared.ShowImage
import hu.bme.idselector.viewmodels.createid.NewDocumentViewModel

@Composable
fun CropDocument(viewModel: NewDocumentViewModel) {
    val offsets = remember { viewModel.intOffsets }

    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxSize()
    ) {
        Box(
            modifier = Modifier
                .onGloballyPositioned {
                    viewModel.refreshIntOffsets(it.size.width, it.size.height)
                },
            contentAlignment = Alignment.Center
        ) {
            ShowImage(
                uri = viewModel.selectedImageUri.value,
                modifier = Modifier
                    .fillMaxWidth(0.8f)
                    .wrapContentHeight()
            )
            Rectangle(
                offsets = offsets,
                modifier = Modifier.matchParentSize()
            )
        }
    }
}
