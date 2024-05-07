package hu.bme.idselector.ui.idlist

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import hu.bme.idselector.R
import hu.bme.idselector.api.ApiService
import hu.bme.idselector.data.NationalId
import hu.bme.idselector.data.OtherId
import hu.bme.idselector.ui.shared.ShowImage

@Composable
fun NationalIdCard(id: NationalId, modifier: Modifier = Modifier) {
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
                            value = id.dateOfBirth,
                            modifier = Modifier.padding(bottom = 5.dp)
                        )
                        IdField(
                            title = stringResource(id = R.string.dateOfExpiry),
                            value = id.dateOfExpiry
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
                    .fillMaxHeight()
                    .fillMaxWidth(),
            ) {
                val rotationModifier: Modifier = Modifier.graphicsLayer {
                    alpha = animateBack
                    rotationY = rotation
                }
                Column(
                    verticalArrangement = Arrangement.SpaceEvenly,
                    horizontalAlignment = Alignment.End,
                    modifier = Modifier.weight(0.5f)
                        .padding(end = 7.dp)
                ) {
                    IdField(
                        title = stringResource(id = R.string.can),
                        value = id.can,
                        fontSize = 10.sp,
                        textModifier = rotationModifier,
                        horizontalAlignment = Alignment.End,
                        modifier = Modifier.padding(bottom = 10.dp)
                    )
                    IdField(
                        title = stringResource(id = R.string.authority),
                        value = id.authority,
                        fontSize = 10.sp,
                        textModifier = rotationModifier,
                        horizontalAlignment = Alignment.End,
                        modifier = Modifier.padding(bottom = 10.dp)
                    )
                    Row {
                        IdField(
                            title = stringResource(id = R.string.nationality),
                            value = id.nationality,
                            fontSize = 10.sp,
                            textModifier = rotationModifier,
                            horizontalAlignment = Alignment.End,
                            modifier = Modifier.weight(1f)
                        )
                        IdField(
                            title = stringResource(id = R.string.sex),
                            value = id.sex.toString(),
                            fontSize = 10.sp,
                            textModifier = rotationModifier,
                            horizontalAlignment = Alignment.End,
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
                Column(
                    verticalArrangement = Arrangement.SpaceEvenly,
                    horizontalAlignment = Alignment.End,
                    modifier = Modifier.weight(0.5f)
                ) {
                    IdField(
                        title = stringResource(id = R.string.placeOfBirth),
                        value = id.placeOfBirth,
                        fontSize = 10.sp,
                        textModifier = rotationModifier,
                        horizontalAlignment = Alignment.End,
                        modifier = Modifier.padding(bottom = 10.dp)
                    )
                    IdField(
                        title = stringResource(id = R.string.nameAtBirth),
                        value = id.nameAtBirth,
                        fontSize = 10.sp,
                        textModifier = rotationModifier,
                        horizontalAlignment = Alignment.End,
                        modifier = Modifier.padding(bottom = 10.dp)
                    )
                    IdField(
                        title = stringResource(id = R.string.mothersName),
                        value = id.mothersName,
                        fontSize = 10.sp,
                        textModifier = rotationModifier,
                        horizontalAlignment = Alignment.End
                    )
                }
            }
        }
    }
}