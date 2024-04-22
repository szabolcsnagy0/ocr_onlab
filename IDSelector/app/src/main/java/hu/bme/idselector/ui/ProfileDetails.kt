package hu.bme.idselector.ui

import android.graphics.Color
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import hu.bme.idselector.R
import hu.bme.idselector.data.NationalId
import hu.bme.idselector.data.Profile
import java.text.DateFormat
import java.time.LocalDate
import java.util.Date

@Composable
fun ProfileDetails(
    profile: Profile = Profile(
        id = 1,
        name = "John Doe",
        nationalId = NationalId(
            id = 1,
            name = "John Doe",
            authority = "UK",
            can = "3125233",
            dateOfBirth = Date("1/5/1977"),
            dateOfExpiry = Date("5/5/2005"),
            documentNr = "312312DS",
            mothersName = "Maria Doe",
            nameAtBirth = "John Doe",
            nationality = "UK",
            placeOfBirth = "London",
            sex = 'M'
        )
    )
) {
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        profile.name?.let {
            Text(text = it, fontWeight = FontWeight.Bold, fontSize = 25.sp)
        }
        profile.nationalId?.let {
            IdComponent(id = it)
        }
    }
}

@Composable
fun IdComponent(id: NationalId, modifier: Modifier = Modifier) {
    var rotated by remember { mutableStateOf(false) }

    val rotation by animateFloatAsState(
        targetValue = if (rotated) 180f else 0f,
        animationSpec = tween(500)
    )

    val animateFront by animateFloatAsState(
        targetValue = if (!rotated) 1f else 0f,
        animationSpec = tween(500)
    )

    val animateBack by animateFloatAsState(
        targetValue = if (rotated) 1f else 0f,
        animationSpec = tween(500)
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
            },
    ) {
        if (!rotated) {
            Column(
                horizontalAlignment = Alignment.Start,
                verticalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier
                    .padding(start = 8.dp, end = 8.dp, bottom = 8.dp)
                    .fillMaxWidth()
                    .graphicsLayer {
                        alpha = animateFront
                        rotationY = rotation
                    },
            ) {
                id.name?.let {
                    Column {
                        Text(
                            text = "Family name and Given name:",
                            fontWeight = FontWeight.Light,
                            fontSize = 15.sp,
                            modifier = Modifier
                                .padding(bottom = 2.dp)
                        )
                        Text(
                            text = it,
                            modifier = Modifier,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
                id.nationality?.let {
                    Column {
                        Text(
                            text = "Nationality:",
                            fontWeight = FontWeight.Light,
                            fontSize = 15.sp,
                            modifier = Modifier
                                .padding(end = 2.dp)
                        )
                        Text(
                            text = it,
                            modifier = Modifier,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
                id.documentNr?.let {
                    Column {
                        Text(
                            text = "Document Number:",
                            fontWeight = FontWeight.Light,
                            fontSize = 15.sp,
                            modifier = Modifier
                                .padding(end = 2.dp)
                        )
                        Text(
                            text = it,
                            modifier = Modifier,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        } else {
            Column(
                horizontalAlignment = Alignment.Start,
                verticalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier
                    .padding(start = 8.dp, end = 8.dp, bottom = 8.dp)
                    .fillMaxWidth()
                    .graphicsLayer {
                        alpha = animateBack
                        rotationY = rotation
                    },
            ) {
                id.placeOfBirth?.let {
                    Column {
                        Text(
                            text = "Place of birth:",
                            fontWeight = FontWeight.Light,
                            fontSize = 15.sp,
                            modifier = Modifier
                                .padding(end = 2.dp)
                        )
                        Text(
                            text = it,
                            modifier = Modifier,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }

                }
            }
        }
    }
}