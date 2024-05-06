package hu.bme.idselector.ui.createid.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import hu.bme.idselector.R

@Composable
fun ChooseSex(sex: Char?, modifier: Modifier = Modifier, onClick: () -> Unit) {
    fun getBorderStroke(value: Char?): BorderStroke =
        if (sex == value) BorderStroke(3.dp, Color.Black) else BorderStroke(0.dp, Color.Black)

    Column(modifier) {
        Text(
            text = stringResource(id = R.string.sex),
            fontWeight = FontWeight.SemiBold,
            color = colorResource(R.color.black),
            fontSize = 18.sp,
            modifier = Modifier.fillMaxWidth()
        )
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 5.dp, vertical = 10.dp)
        ) {
            ElevatedButton(
                onClick = onClick,
                colors = ButtonDefaults.buttonColors(
                    contentColor = colorResource(id = R.color.black),
                    containerColor = Color.Transparent,
                    disabledContentColor = colorResource(id = R.color.white),
                    disabledContainerColor = colorResource(id = R.color.black)
                ),
                enabled = sex != 'M',
                border = getBorderStroke('M'),
                elevation = ButtonDefaults.buttonElevation(
                    defaultElevation = 5.dp,
                    pressedElevation = 5.dp,
                    focusedElevation = 5.dp,
                    hoveredElevation = 5.dp,
                    disabledElevation = 5.dp
                ),
                modifier = Modifier
                    .weight(1f)
                    .padding(end = 3.dp)
                    .height(50.dp)
            ) {
                Text(
                    text = stringResource(id = R.string.male),
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 15.sp
                )
            }
            ElevatedButton(
                onClick = onClick,
                colors = ButtonDefaults.buttonColors(
                    contentColor = colorResource(id = R.color.black),
                    containerColor = Color.Transparent,
                    disabledContentColor = colorResource(id = R.color.white),
                    disabledContainerColor = colorResource(id = R.color.black)
                ),
                enabled = sex != 'F',
                border = getBorderStroke('F'),
                elevation = ButtonDefaults.buttonElevation(
                    defaultElevation = 5.dp,
                    pressedElevation = 5.dp,
                    focusedElevation = 5.dp,
                    hoveredElevation = 5.dp,
                    disabledElevation = 5.dp
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
                    .weight(1f)
            ) {
                Text(
                    text = stringResource(id = R.string.female),
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 15.sp
                )
            }
        }
    }
}