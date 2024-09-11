package hu.bme.idselector.ui.idtemplate

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.Save
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedCard
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
import hu.bme.idselector.ui.createid.components.Rectangle
import hu.bme.idselector.ui.idtemplate.states.TemplateCreationState
import hu.bme.idselector.ui.shared.ShowImage
import hu.bme.idselector.ui.shared.TextWithInput
import hu.bme.idselector.viewmodels.DocumentTemplateViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TemplateFieldCreator(
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
                actions = {
                    if (appState == TemplateCreationState.START) {
                        IconButton(onClick = viewModel::onSaveTemplate) {
                            Icon(
                                imageVector = Icons.Filled.Save,
                                contentDescription = null,
                                tint = colorResource(id = R.color.white),
                                modifier = Modifier.size(35.dp)
                            )
                        }
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
                        onClick = viewModel::onAddFieldStarted,
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
                        onClick = viewModel::onAddFieldFinished,
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
                    val templateFieldNames by viewModel.templateFieldNames.collectAsStateWithLifecycle(null)
                    ShowImage(
                        glideUrl = viewModel.getFrontImageUrl(),
                        modifier = Modifier
                            .padding(vertical = 10.dp)
                            .heightIn(max = 200.dp)
                    )
                    templateFieldNames?.let { namesList ->
                        Text(
                            text = "Added fields:",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.ExtraBold,
                            color = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.padding(top = 10.dp)
                        )
                        LazyColumn(
                            verticalArrangement = Arrangement.spacedBy(10.dp),
                            contentPadding = PaddingValues(vertical = 5.dp)
                        ) {
                            items(namesList) { field ->
                                ElevatedCard(
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Text(
                                        text = field,
                                        modifier = Modifier.padding(10.dp)
                                    )
                                }
                            }
                        }
                    }
                }

                TemplateCreationState.ADD_FIELD_VALUE -> {
                    val intOffsets = remember { viewModel.valueIntOffsets }
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier
                            .onGloballyPositioned {
                                viewModel.initializeIntOffsets(intOffsets, it.size.width, it.size.height)
                            }
                    ) {
                        ShowImage(
                            glideUrl = viewModel.getFrontImageUrl(),
                            modifier = Modifier
                                .padding(vertical = 10.dp)
                                .heightIn(max = 200.dp)
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
                    val intOffsets = remember { viewModel.keyIntOffsets }
                    TextWithInput(
                        text = "Please type the text of the key",
                        textChanged = viewModel::onKeyTextChanged,
                        modifier = Modifier.padding(bottom = 30.dp)
                    )
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier
                            .onGloballyPositioned {
                                viewModel.initializeIntOffsets(intOffsets, it.size.width, it.size.height)
                            }
                    ) {
                        ShowImage(
                            glideUrl = viewModel.getFrontImageUrl(),
                            modifier = Modifier
                                .padding(vertical = 10.dp)
                                .heightIn(max = 200.dp)
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

                TemplateCreationState.RESULT -> {
                    val template by viewModel.templateString.collectAsStateWithLifecycle()
                    Text(
                        template, color = MaterialTheme.colorScheme.primary, modifier = Modifier.verticalScroll(
                            rememberScrollState()
                        )
                    )
                }
            }
        }
    }
}
