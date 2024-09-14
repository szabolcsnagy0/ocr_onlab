package hu.bme.idselector.ui.createid

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import hu.bme.idselector.ui.createid.states.DetectionState
import hu.bme.idselector.viewmodels.createid.NewDocumentFromTemplateViewModel

@Composable
fun NewDocumentFromTemplateScreen(
    viewModel: NewDocumentFromTemplateViewModel,
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
        DetectionResult(viewModel = viewModel, onResult = onResult)
    }
}
