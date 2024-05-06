package hu.bme.idselector.ui.createid

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import hu.bme.idselector.ui.createid.states.DetectionState
import hu.bme.idselector.viewmodels.createid.NewNationalViewModel

@Composable
fun NewNationalIdScreen(
    viewModel: NewNationalViewModel,
    onCancelled: () -> Unit,
    onResult: () -> Unit,
) {
    val appState by remember { viewModel.detectionState }
    if (appState != DetectionState.RESULT) {
        NewDocumentScreen(
            viewModel = viewModel,
            onCancelled = onCancelled,
            onResult = {
                viewModel.onResult()
            }
        )
    } else {
        DetectionResult(nationalId = viewModel.identity, onResult = onResult)
    }
}