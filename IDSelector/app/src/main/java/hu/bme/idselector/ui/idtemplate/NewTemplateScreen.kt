package hu.bme.idselector.ui.idtemplate

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import hu.bme.idselector.ui.createid.NewDocumentScreen
import hu.bme.idselector.ui.createid.states.DetectionState
import hu.bme.idselector.viewmodels.DocumentTemplateViewModel

@Composable
fun NewTemplateScreen(
    viewModel: DocumentTemplateViewModel,
    onCancelled: () -> Unit,
    onResult: () -> Unit
) {
    val appState by remember { viewModel.detectionState }
    if (appState != DetectionState.RESULT) {
        NewDocumentScreen(
            viewModel = viewModel,
            onCancelled = onCancelled,
            onResult = viewModel::onResult
        )
    } else {
        TemplateFieldCreator(
            viewModel = viewModel,
            onCancelled = {}
        )
    }
}
