package hu.bme.idselector.ui

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
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
import hu.bme.idselector.data.NationalId
import hu.bme.idselector.data.Profile
import hu.bme.idselector.viewmodels.ProfilesViewModel
import java.text.DateFormat
import java.time.LocalDate
import java.util.Date

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileDetails(
    viewModel: ProfilesViewModel,
    addNewNationalIdDocument: () -> Unit = {},
    addNewOtherIdDocument: () -> Unit = {},
    onBackPressed: () -> Unit = {}
) {
    val profile = remember { viewModel.selectedProfile.value }
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    profile?.name?.let {
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
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.KeyboardArrowLeft,
                        contentDescription = stringResource(
                            id = R.string.back
                        ),
                        tint = colorResource(id = R.color.white),
                        modifier = Modifier.size(35.dp)
                    )
                },
                actions = {
                    IconButton(onClick = onBackPressed) {
                        Icon(
                            Icons.Filled.Edit,
                            contentDescription = stringResource(R.string.edit_profile),
                            tint = colorResource(id = R.color.white),
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
        Column(
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .padding(it)
                .padding(top = 20.dp)
                .fillMaxSize()
        ) {
//            profile.nationalId?.let {
//                IdComponent(id = it)
//            }
        }
    }
}

@Composable
fun IdComponent(id: NationalId, modifier: Modifier = Modifier) {
    var rotated by remember { mutableStateOf(false) }

    val rotation by animateFloatAsState(
        targetValue = if (rotated) 180f else 0f,
        animationSpec = tween(500), label = ""
    )

    val animateFront by animateFloatAsState(
        targetValue = if (!rotated) 1f else 0f,
        animationSpec = tween(500), label = ""
    )

    val animateBack by animateFloatAsState(
        targetValue = if (rotated) 1f else 0f,
        animationSpec = tween(500), label = ""
    )
    ElevatedCard(
        colors = CardDefaults.cardColors(
            containerColor = colorResource(id = R.color.white),
            contentColor = colorResource(id = R.color.grey)
        ),
        elevation = CardDefaults.elevatedCardElevation(10.dp),
        modifier = modifier
            .graphicsLayer {
                rotationY = rotation
                cameraDistance = 8 * density
            }
            .height(200.dp)
            .fillMaxWidth()
            .padding(10.dp)
            .clickable {
                rotated = !rotated
            }
    ) {
        if (!rotated) {
            Column(
                horizontalAlignment = Alignment.Start,
                verticalArrangement = Arrangement.SpaceEvenly,
                modifier = modifier
                    .padding(vertical = 5.dp, horizontal = 10.dp)
                    .fillMaxWidth()
                    .graphicsLayer {
                        alpha = animateFront
                    },
            ) {
                Text(
                    text = stringResource(id = R.string.id_card),
                    fontSize = 30.sp,
                    fontWeight = FontWeight.ExtraBold,
                    modifier = Modifier.padding(bottom = 10.dp)
                )

                Row {
                    Column(
                        modifier = Modifier.weight(1f)
                    ) {
                        IdField(
                            title = stringResource(id = R.string.name),
                            value = id.name,
                            modifier = Modifier.padding(bottom = 5.dp)
                        )
                        IdField(
                            title = stringResource(id = R.string.documentNr),
                            value = id.documentNr
                        )
                    }

                    Column(
                        modifier = Modifier.weight(1f)
                    ) {
                        IdField(
                            title = stringResource(id = R.string.dateOfBirth),
                            value = id.dateOfBirth?.toGMTString()?.take(10),
                            modifier = Modifier.padding(bottom = 5.dp)
                        )
                        IdField(
                            title = stringResource(id = R.string.dateOfExpiry),
                            value = id.dateOfExpiry?.toGMTString()?.take(10)
                        )
                    }
                }
            }
        } else {
            Row(
                verticalAlignment = Alignment.Top,
                horizontalArrangement = Arrangement.SpaceEvenly,
                modifier = Modifier
                    .padding(10.dp)
                    .fillMaxWidth(),
            ) {
                val rotationModifier: Modifier = Modifier.graphicsLayer {
                    alpha = animateBack
                    rotationY = rotation
                }
                Column(
                    horizontalAlignment = Alignment.End,
                    modifier = Modifier.weight(0.5f)
                ) {
                    IdField(
                        title = stringResource(id = R.string.can),
                        value = id.can,
                        fontSize = 13.sp,
                        textModifier = rotationModifier,
                        horizontalAlignment = Alignment.End,
                        modifier = Modifier.padding(bottom = 10.dp)
                    )
                    IdField(
                        title = stringResource(id = R.string.authority),
                        value = id.authority,
                        fontSize = 13.sp,
                        textModifier = rotationModifier,
                        horizontalAlignment = Alignment.End,
                        modifier = Modifier.padding(bottom = 10.dp)
                    )
                    Row {
                        IdField(
                            title = stringResource(id = R.string.nationality),
                            value = id.nationality,
                            fontSize = 13.sp,
                            textModifier = rotationModifier,
                            horizontalAlignment = Alignment.End,
                            modifier = Modifier.weight(1f)
                        )
                        IdField(
                            title = stringResource(id = R.string.sex),
                            value = id.sex.toString(),
                            fontSize = 13.sp,
                            textModifier = rotationModifier,
                            horizontalAlignment = Alignment.End,
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
                Column(
                    horizontalAlignment = Alignment.End,
                    modifier = Modifier.weight(0.5f)
                ) {
                    IdField(
                        title = stringResource(id = R.string.placeOfBirth),
                        value = id.placeOfBirth,
                        fontSize = 13.sp,
                        textModifier = rotationModifier,
                        horizontalAlignment = Alignment.End,
                        modifier = Modifier.padding(bottom = 10.dp)
                    )
                    IdField(
                        title = stringResource(id = R.string.nameAtBirth),
                        value = id.nameAtBirth,
                        fontSize = 13.sp,
                        textModifier = rotationModifier,
                        horizontalAlignment = Alignment.End,
                        modifier = Modifier.padding(bottom = 10.dp)
                    )
                    IdField(
                        title = stringResource(id = R.string.mothersName),
                        value = id.mothersName,
                        fontSize = 13.sp,
                        textModifier = rotationModifier,
                        horizontalAlignment = Alignment.End
                    )
                }
            }
        }
    }
}

@Composable
fun IdField(
    title: String,
    value: String?,
    modifier: Modifier = Modifier,
    textModifier: Modifier = Modifier,
    horizontalAlignment: Alignment.Horizontal = Alignment.Start,
    fontSize: TextUnit = 15.sp
) {
    Column(
        horizontalAlignment = horizontalAlignment,
        modifier = modifier
    ) {
        Text(
            text = "$title:",
            fontWeight = FontWeight.Light,
            fontSize = fontSize,
            lineHeight = fontSize.times(1.1f),
            modifier = textModifier,
        )
        value?.let {
            Text(
                text = it,
                modifier = textModifier,
                fontSize = fontSize * 1.33,
                fontWeight = FontWeight.Bold
            )
        }
    }
}