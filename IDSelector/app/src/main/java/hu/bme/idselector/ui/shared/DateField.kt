package hu.bme.idselector.ui.shared

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDefaults
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import hu.bme.idselector.R
import java.text.SimpleDateFormat
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DateField(titleText: String, date: String?, onValueChange: (String) -> Unit, modifier: Modifier = Modifier) {
    var showDatePicker by remember {
        mutableStateOf(false)
    }
    Column(modifier) {
        Text(
            text = titleText,
            fontWeight = FontWeight.SemiBold,
            color = colorResource(R.color.black),
            fontSize = 18.sp,
            modifier = Modifier.fillMaxWidth()
        )
        ElevatedButton(
            onClick = {
                showDatePicker = true
            },
            colors = ButtonDefaults.buttonColors(
                containerColor = colorResource(id = R.color.black),
                contentColor = colorResource(id = R.color.white),
                disabledContainerColor = colorResource(id = R.color.grey)
            ),
            elevation = ButtonDefaults.buttonElevation(
                defaultElevation = 5.dp,
                pressedElevation = 5.dp,
                focusedElevation = 5.dp,
                hoveredElevation = 5.dp,
                disabledElevation = 0.dp
            ),
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 20.dp, bottom = 10.dp)
                .heightIn(50.dp, 100.dp)
        ) {
            Text(
                text = date ?: stringResource(id = R.string.select_date),
                fontWeight = FontWeight.SemiBold,
                fontSize = 15.sp
            )
        }
    }

    if (showDatePicker) {
        val formatter = SimpleDateFormat("yyyy-MM-dd", Locale.ROOT)
        val datePickerState = rememberDatePickerState()
        DatePickerDialog(onDismissRequest = {
            showDatePicker = false
        }, confirmButton = {
            TextButton(onClick = {
                showDatePicker = false
                onValueChange(formatter.format(datePickerState.selectedDateMillis))
            }) {
                Text(text = stringResource(id = R.string.save))
            }
        }, dismissButton = {
            TextButton(onClick = {
                showDatePicker = false
            }) {
                Text(text = stringResource(id = R.string.cancel))
            }
        },
            colors = DatePickerDefaults.colors(
                containerColor = Color.White
            )
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                DatePicker(
                    state = datePickerState,
                    showModeToggle = false
                )
            }
        }
    }
}