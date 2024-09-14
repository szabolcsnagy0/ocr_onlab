package hu.bme.idselector.ui.createid

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import hu.bme.idselector.R
import hu.bme.idselector.ui.shared.EditTextWithTitle
import hu.bme.idselector.viewmodels.createid.NewDocumentFromTemplateViewModel

@Composable
fun DetectionResult(
    viewModel: NewDocumentFromTemplateViewModel,
    onResult: () -> Unit = {}
) {
    val fieldList by viewModel.fieldList.collectAsStateWithLifecycle(emptyList())

    LazyColumn(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(20.dp),
        modifier = Modifier.padding(10.dp)
    ) {
        itemsIndexed(fieldList) { index, field ->
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(5.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                var fieldValue by remember { mutableStateOf(field.value) }
                EditTextWithTitle(
                    titleText = field.title,
                    prevText = fieldValue,
                    onValueChange = {
                        fieldValue = it
                        viewModel.changeFieldValue(
                            index,
                            field.copy(value = it)
                        )
                    }
                )
            }
        }

        item {
            ElevatedButton(
                onClick = onResult,
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
                    text = stringResource(id = R.string.save),
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 15.sp
                )
            }
        }
    }
}
