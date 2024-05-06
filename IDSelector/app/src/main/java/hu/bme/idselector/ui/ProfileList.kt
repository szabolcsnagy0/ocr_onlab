package hu.bme.idselector.ui

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import hu.bme.idselector.R
import hu.bme.idselector.ui.shared.CustomAlertDialog
import hu.bme.idselector.viewmodels.ProfilesViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileList(
    viewModel: ProfilesViewModel,
    onProfileSelected: (Int) -> Unit = {_ -> },
    logoutRequested: () -> Unit = {}
) {
    val profiles = remember { viewModel.profiles }

    val logoutRequest = remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = stringResource(id = R.string.app_name),
                        fontWeight = FontWeight.ExtraBold,
                        fontSize = 30.sp,
                        color = colorResource(
                            id = R.color.white
                        )
                    )
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    titleContentColor = colorResource(id = R.color.white),
                    containerColor = colorResource(id = R.color.grey)
                ),
                navigationIcon = {
                    IconButton(onClick = { logoutRequest.value = true }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.Logout,
                            contentDescription = stringResource(
                                id = R.string.logout
                            ),
                            tint = colorResource(id = R.color.white),
                            modifier = Modifier.size(35.dp)
                        )
                    }
                },
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { /* TODO */ },
                containerColor = colorResource(id = R.color.grey),
                contentColor = colorResource(id = R.color.white),
                elevation = FloatingActionButtonDefaults.bottomAppBarFabElevation()
            ) {
                Icon(Icons.Filled.Add, stringResource(R.string.add_card))
            }
        },
        containerColor = colorResource(id = R.color.orange),
        modifier = Modifier.fillMaxHeight()
    ) { paddingValues ->
        LazyColumn(
            contentPadding = PaddingValues(
                bottom = 30.dp
            ),
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
        ) {
            items(profiles) { profile ->
                ProfileElement(profile) {
                    onProfileSelected(profile.id)
                }
            }
        }
    }

    if (logoutRequest.value) {
        CustomAlertDialog(
            dialogText = stringResource(R.string.logout_confirmation),
            confirmText = stringResource(R.string.yes),
            dismissText = stringResource(
                id = R.string.cancel
            ),
            onConfirmation = logoutRequested,
            onDismissRequest = {
                logoutRequest.value = false
            }
        )
    }
}