package hu.bme.idselector.ui.createid

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import hu.bme.idselector.ui.createid.states.DetectionState
import hu.bme.idselector.viewmodels.createid.NewNationalViewModel

@Composable
fun NewNationalIdScreen(
    viewModel: NewNationalViewModel,
    onCancelled: () -> Unit,
    onResult: () -> Unit,
    onError: () -> Unit
) {
    val appState by remember { viewModel.detectionState }
    if (appState != DetectionState.RESULT) {
        NewDocumentScreen(
            viewModel = viewModel,
            onCancelled = onCancelled,
            onResult = {
                viewModel.onResult()
            },
            onError = onError
        )
    } else {
        DetectionResultNationalId(nationalId = viewModel.identity, onResult = onResult)
    }
}
