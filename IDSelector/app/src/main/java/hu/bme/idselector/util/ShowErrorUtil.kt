package hu.bme.idselector.util

import android.util.Log
import androidx.compose.material3.SnackbarHostState
import hu.bme.idselector.error.MessageEvent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

internal fun SnackbarHostState.showSnackbarForError(
    event: MessageEvent,
    coroutineScope: CoroutineScope
) {
    val msg = when (event) {
        is MessageEvent.Error -> {
            Log.e("GenericError", event.throwable.toString())
            "An error occurred: " + event.throwable.toString()
        }

        is MessageEvent.Message -> {
            event.message
        }
    }

    coroutineScope.launch {
        this@showSnackbarForError.showSnackbar(
            message = msg,
            withDismissAction = true
        )
    }
}
