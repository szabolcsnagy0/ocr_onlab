package hu.bme.idselector.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.DocumentScanner
import androidx.compose.material.icons.filled.Image
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import hu.bme.idselector.R
import hu.bme.idselector.api.ApiService
import hu.bme.idselector.ui.idlist.DocumentCard
import hu.bme.idselector.ui.idlist.ImageIdCard
import hu.bme.idselector.ui.idlist.NationalIdCard
import hu.bme.idselector.ui.shared.FabItem
import hu.bme.idselector.ui.shared.MultiFloatingActionButton
import hu.bme.idselector.viewmodels.DocumentListViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileDetails(
    viewModel: DocumentListViewModel,
    onBackPressed: () -> Unit = {}
) {
    val profile = remember { viewModel.profile }
    val nationalIds by viewModel.nationalIds.collectAsStateWithLifecycle()
    val otherIds by viewModel.otherIds.collectAsStateWithLifecycle()
    val documents by viewModel.documents.collectAsStateWithLifecycle()
    val isLoading by viewModel.isRefreshing.collectAsStateWithLifecycle(true)

    val showImages = remember { mutableStateOf(false) }
    val selectTemplate = remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    profile.name?.let {
                        Text(
                            text = it,
                            fontWeight = FontWeight.ExtraBold,
                            fontSize = 30.sp,
                            color = colorResource(
                                id = R.color.white
                            )
                        )
                    }
                },
                navigationIcon = {
                    IconButton(onClick = onBackPressed) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.KeyboardArrowLeft,
                            contentDescription = stringResource(
                                id = R.string.back
                            ),
                            tint = colorResource(id = R.color.white),
                            modifier = Modifier.size(35.dp)
                        )
                    }
                },
                actions = {
                    IconButton(onClick = {
                        showImages.value = !showImages.value
                    }) {
                        if (showImages.value) {
                            Icon(
                                Icons.Filled.DocumentScanner,
                                contentDescription = stringResource(R.string.show_scan),
                                tint = colorResource(id = R.color.white),
                            )
                        } else {
                            Icon(
                                Icons.Filled.Image,
                                contentDescription = stringResource(R.string.show_images),
                                tint = colorResource(id = R.color.white),
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
            MultiFloatingActionButton(
                fabIcon = Icons.Filled.Add,
                items = arrayListOf(
                    FabItem(
                        icon = Icons.Filled.DocumentScanner,
                        label = "Select template",
                        onFabItemClicked = {
                            selectTemplate.value = true
                        }
                    ),
                    FabItem(
                        icon = Icons.Filled.DocumentScanner,
                        label = "Hungarian national ID",
                        onFabItemClicked = viewModel::onAddNewNationalId
                    ),
                    FabItem(
                        icon = Icons.Filled.Image,
                        label = "Other document",
                        onFabItemClicked = viewModel::onAddNewOtherId
                    )
                )
            )
        },
        containerColor = colorResource(id = R.color.orange)
    ) {
        if (isLoading) {
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
        } else {
            LazyColumn(
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.CenterHorizontally,
                contentPadding = PaddingValues(
                    top = 20.dp,
                    bottom = 100.dp
                ),
                modifier = Modifier
                    .padding(it)
                    .fillMaxSize()
            ) {
                items(nationalIds) { nationalId ->
                    if (showImages.value) {
                        val frontUrl = ApiService.getNationalIdFront(
                            profileId = profile.id,
                            nationalId = nationalId.id
                        )
                        val backUrl = ApiService.getNationalIdBack(
                            profileId = profile.id,
                            nationalId = nationalId.id
                        )
                        ImageIdCard(frontUrl = frontUrl, backUrl = backUrl)
                    } else {
                        NationalIdCard(id = nationalId)
                    }
                    Spacer(modifier = Modifier.padding(vertical = 10.dp))
                }

                items(documents) { document ->
                    if (showImages.value) {
                        val frontUrl = ApiService.getDocumentFront(
                            profileId = profile.id,
                            documentId = document.id
                        )
                        val backUrl = ApiService.getDocumentBack(
                            profileId = profile.id,
                            documentId = document.id
                        )
                        ImageIdCard(frontUrl = frontUrl, backUrl = backUrl)
                    } else {
                        DocumentCard(document = document)
                    }
                    Spacer(modifier = Modifier.padding(vertical = 10.dp))
                }

                items(otherIds) { otherId ->
                    if (showImages.value) {
                        val frontUrl = otherId.id?.let { it1 ->
                            ApiService.getOtherIdFront(
                                profileId = profile.id,
                                otherId = it1
                            )
                        }
                        val backUrl = otherId.id?.let { it1 ->
                            ApiService.getOtherIdBack(
                                profileId = profile.id,
                                otherId = it1
                            )
                        }
                        ImageIdCard(frontUrl = frontUrl, backUrl = backUrl)
                        Spacer(modifier = Modifier.padding(vertical = 10.dp))
                    }
                }
            }
        }
    }
    if (selectTemplate.value) {
        val documentTemplates by viewModel.documentTemplates.collectAsStateWithLifecycle(emptyList())
        Dialog(
            onDismissRequest = {
                selectTemplate.value = false
            }
        ) {
            ElevatedCard(
                modifier = Modifier
                    .padding(10.dp)
                    .heightIn(max = 300.dp)
            ) {
                LazyColumn(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center,
                    contentPadding = PaddingValues(bottom = 5.dp),
                    modifier = Modifier.padding(10.dp)
                ) {
                    item {
                        if (documentTemplates.isEmpty()) {
                            Text("No templates were found!")
                        }
                    }
                    itemsIndexed(documentTemplates) { index, template ->
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center,
                            modifier = Modifier
                                .fillMaxWidth()
                                .wrapContentHeight()
                                .clickable {
                                    viewModel.onSelectDocumentTemplate(template)
                                    selectTemplate.value = false
                                }
                        ) {
                            if (index != 0) {
                                Divider(
                                    thickness = 1.dp,
                                    color = colorResource(R.color.grey),
                                    modifier = Modifier.padding(vertical = 10.dp)
                                )
                            }
                            Text(
                                text = template.name,
                                fontWeight = FontWeight.Bold,
                            )
                        }
                    }
                }
            }
        }
    }
}
