package hu.bme.idselector.ui.idtemplate

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import hu.bme.idselector.R
import hu.bme.idselector.ui.createid.components.ImagePreview
import hu.bme.idselector.ui.createid.components.Rectangle
import hu.bme.idselector.ui.idtemplate.states.TemplateCreationState
import hu.bme.idselector.ui.shared.ShowImage
import hu.bme.idselector.ui.shared.TextWithInput
import hu.bme.idselector.viewmodels.DocumentTemplateViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TemplateCreator(
    viewModel: DocumentTemplateViewModel,
    onCancelled: () -> Unit = {}
) {
    val appState by viewModel.creationState.collectAsStateWithLifecycle()
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = "New template",
                        fontWeight = FontWeight.ExtraBold,
                        fontSize = 30.sp,
                        color = colorResource(
                            id = R.color.white
                        )
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onCancelled) {
                        Icon(
                            imageVector = Icons.Filled.Cancel,
                            contentDescription = stringResource(
                                id = R.string.back
                            ),
                            tint = colorResource(id = R.color.white),
                            modifier = Modifier.size(35.dp)
                        )
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    titleContentColor = colorResource(id = R.color.white),
                    containerColor = colorResource(id = R.color.grey)
                ),
            )
        },
        floatingActionButton = {
            when (appState) {
                TemplateCreationState.START -> {
                    FloatingActionButton(
                        onClick = viewModel::onAddFieldKey,
                        containerColor = colorResource(id = R.color.grey),
                        contentColor = colorResource(id = R.color.white),
                        elevation = FloatingActionButtonDefaults.bottomAppBarFabElevation(),
                    ) {
                        Icon(imageVector = Icons.Filled.Add, contentDescription = null)
                    }
                }

                TemplateCreationState.ADD_FIELD_KEY -> {
                    FloatingActionButton(
                        onClick = viewModel::onAddFieldValue,
                        containerColor = colorResource(id = R.color.grey),
                        contentColor = colorResource(id = R.color.white),
                        elevation = FloatingActionButtonDefaults.bottomAppBarFabElevation(),
                    ) {
                        Icon(imageVector = Icons.Filled.Done, contentDescription = null)
                    }
                }

                TemplateCreationState.ADD_FIELD_VALUE -> {
                    FloatingActionButton(
                        onClick = {},
                        containerColor = colorResource(id = R.color.grey),
                        contentColor = colorResource(id = R.color.white),
                        elevation = FloatingActionButtonDefaults.bottomAppBarFabElevation(),
                    ) {
                        Icon(imageVector = Icons.Filled.Done, contentDescription = null)
                    }
                }

                else -> {}
            }
        },
        containerColor = colorResource(id = R.color.orange)
    ) { paddingValues ->
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .padding(paddingValues)
                .padding(horizontal = 5.dp)
                .fillMaxSize()
        ) {
            when (appState) {
                TemplateCreationState.START -> {
                    ImagePreview(
                        text = stringResource(R.string.front),
                        glideUrl = viewModel.imageUrl,
                        imageMaxHeight = 300.dp
                    )
                }

                TemplateCreationState.ADD_FIELD_VALUE -> {
                    val intOffsets = remember { viewModel.intOffsets }
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier
                            .onGloballyPositioned {
                                viewModel.initializeIntOffsets(it.size.width, it.size.height)
                            }
                    ) {
                        ShowImage(
                            glideUrl = viewModel.imageUrl,
                            modifier = Modifier
                                .fillMaxWidth(0.8f)
                                .wrapContentHeight()
                        )
                        Rectangle(
                            offsets = intOffsets,
                            color = Color.Green,
                            modifier = Modifier
                                .matchParentSize()
                        )
                    }
                }

                TemplateCreationState.ADD_FIELD_KEY -> {
                    val intOffsets = remember { viewModel.intOffsets }
                    TextWithInput(
                        text = "Please type the text of the key",
                        textChanged = viewModel::onKeyTextChanged
                    )
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier
                            .onGloballyPositioned {
                                viewModel.initializeIntOffsets(it.size.width, it.size.height)
                            }
                    ) {
                        ShowImage(
                            glideUrl = viewModel.imageUrl,
                            modifier = Modifier
                                .fillMaxWidth()
                                .wrapContentHeight()
                        )
                        Rectangle(
                            offsets = intOffsets,
                            color = Color.Red,
                            modifier = Modifier
                                .matchParentSize()
                        )
                    }
                }

                TemplateCreationState.LOADING -> {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(20.dp)
                    ) {
                        CircularProgressIndicator(
                            modifier = Modifier
                                .size(30.dp)
                                .align(Alignment.Center),
                            color = MaterialTheme.colorScheme.secondary,
                            trackColor = MaterialTheme.colorScheme.surfaceVariant,
                        )
                    }
                }
            }
        }
    }
}
