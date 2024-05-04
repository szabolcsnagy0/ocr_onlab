package hu.bme.idselector.ui.createid

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import hu.bme.idselector.navigation.Routes
import hu.bme.idselector.viewmodels.NewNationalViewModel

@Composable
fun NewNationalIdScreen(
    viewModel: NewNationalViewModel,
    onCancelled: () -> Unit,
    onResult: () -> Unit,
) {
    var showResult by remember { mutableStateOf(false) }
    if (!showResult) {
        NewDocumentScreen(
            viewModel = viewModel,
            onCancelled = onCancelled,
            onResult = {
                viewModel.onResult()
                showResult = true
            }
        )
    } else {
        DetectionResult(nationalId = viewModel.identity, onResult = onResult)
    }
}