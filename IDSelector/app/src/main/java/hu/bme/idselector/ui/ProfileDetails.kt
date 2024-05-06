package hu.bme.idselector.ui

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.DocumentScanner
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import com.bumptech.glide.integration.compose.GlideImage
import hu.bme.idselector.R
import hu.bme.idselector.api.ApiService
import hu.bme.idselector.data.NationalId
import hu.bme.idselector.data.OtherId
import hu.bme.idselector.data.Profile
import hu.bme.idselector.ui.idlist.ImageIdCard
import hu.bme.idselector.ui.idlist.NationalIdCard
import hu.bme.idselector.ui.shared.ShowImage
import hu.bme.idselector.viewmodels.DocumentListViewModel
import hu.bme.idselector.viewmodels.ProfilesViewModel
import java.text.DateFormat
import java.time.LocalDate
import java.util.Date

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileDetails(
    viewModel: DocumentListViewModel,
    addNewNationalIdDocument: () -> Unit = {},
    addNewOtherIdDocument: () -> Unit = {},
    onBackPressed: () -> Unit = {}
) {
    val profile = remember { viewModel.profile }
    val nationalIds = remember { viewModel.nationalIds }
    val otherIds = remember { viewModel.otherIds }

    val showImages = remember { mutableStateOf(false) }

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
            FloatingActionButton(
                onClick = addNewOtherIdDocument,
                containerColor = colorResource(id = R.color.grey),
                contentColor = colorResource(id = R.color.white),
                elevation = FloatingActionButtonDefaults.bottomAppBarFabElevation()
            ) {
                Icon(Icons.Filled.Add, stringResource(R.string.add_card))
            }
        },
        containerColor = colorResource(id = R.color.orange)
    ) {
        LazyColumn(
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally,
            contentPadding = PaddingValues(
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
                    if (frontUrl != null && backUrl != null) {
                        ImageIdCard(frontUrl = frontUrl, backUrl = backUrl)
                    }
                } else {
                    NationalIdCard(id = nationalId)
                }
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
                    if (frontUrl != null && backUrl != null) {
                        ImageIdCard(frontUrl = frontUrl, backUrl = backUrl)
                    }
                }
            }
        }
    }
}