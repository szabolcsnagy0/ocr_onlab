package hu.bme.idselector

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import hu.bme.idselector.api.ApiService
import hu.bme.idselector.api.TokenManager
import hu.bme.idselector.navigation.Navigation
import hu.bme.idselector.ui.theme.IDSelectorTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val tokenManager = TokenManager(applicationContext)
        ApiService.tokenManager = tokenManager
        setContent {
            IDSelectorTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = colorResource(R.color.orange)
                ) {
                    Navigation(tokenManager)
                }
            }
        }
    }
}