package hu.bme.idselector

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import dagger.hilt.android.AndroidEntryPoint
import hu.bme.idselector.api.ApiService
import hu.bme.idselector.api.TokenManager
import hu.bme.idselector.error.MessageHandler
import hu.bme.idselector.navigation.Navigation
import hu.bme.idselector.ui.theme.IDSelectorTheme
import hu.bme.idselector.util.collectWithLifecycle
import hu.bme.idselector.util.showSnackbarForError
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @Inject
    internal lateinit var messageHandler: MessageHandler

    @Inject
    internal lateinit var tokenManager: TokenManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ApiService.tokenManager = tokenManager
        setContent {
            IDSelectorTheme {
                val snackBarHostState = remember { SnackbarHostState() }
                val coroutineScope = rememberCoroutineScope()

                messageHandler.dispatchMessage().collectWithLifecycle { event ->
                    snackBarHostState.showSnackbarForError(
                        event = event,
                        coroutineScope = coroutineScope
                    )
                }

                Scaffold(
                    snackbarHost = {
                        SnackbarHost(snackBarHostState)
                    },
                    modifier = Modifier.fillMaxSize()
                ) { innerPadding ->
                    Surface(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(innerPadding),
                        color = colorResource(R.color.orange)
                    ) {
                        Navigation(messageHandler = messageHandler)
                    }
                }
            }
        }
    }
}
