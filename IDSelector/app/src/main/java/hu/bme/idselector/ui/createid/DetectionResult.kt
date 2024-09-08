package hu.bme.idselector.ui.createid

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import hu.bme.idselector.R
import hu.bme.idselector.data.NationalId
import hu.bme.idselector.ui.createid.components.ChooseSex
import hu.bme.idselector.ui.shared.DateField
import hu.bme.idselector.ui.shared.EditTextWithTitle

@Preview(showBackground = true)
@Composable
fun DetectionResult(
    nationalId: MutableState<NationalId?> = mutableStateOf(
        NationalId(
            id = 1,
            sex = 'M',
            placeOfBirth = "London",
            nationality = "HUN",
            nameAtBirth = "John Doe",
            mothersName = "Maria Doe",
            documentNr = "312312",
            dateOfExpiry = "2002-05-04",
            dateOfBirth = "1999-03-01",
            can = "41",
            name = "John Doe",
            authority = "HUN"
        )
    ),
    onResult: () -> Unit = {}
) {
    var name by remember {
        mutableStateOf(nationalId.value?.name)
    }
    var sex by remember {
        mutableStateOf(nationalId.value?.sex)
    }
    var placeOfBirth by remember {
        mutableStateOf(nationalId.value?.placeOfBirth)
    }
    var nationality by remember {
        mutableStateOf(nationalId.value?.nationality)
    }
    var nameAtBirth by remember {
        mutableStateOf(nationalId.value?.nameAtBirth)
    }
    var mothersName by remember {
        mutableStateOf(nationalId.value?.mothersName)
    }
    var documentNr by remember {
        mutableStateOf(nationalId.value?.documentNr)
    }
    var authority by remember {
        mutableStateOf(nationalId.value?.authority)
    }
    var can by remember {
        mutableStateOf(nationalId.value?.can)
    }
    var dateOfBirth by remember {
        mutableStateOf(nationalId.value?.dateOfBirth)
    }
    var dateOfExpiry by remember {
        mutableStateOf(nationalId.value?.dateOfExpiry)
    }

    Column(
        modifier = Modifier
            .verticalScroll(rememberScrollState())
            .padding(10.dp)
    ) {
        EditTextWithTitle(
            titleText = stringResource(id = R.string.name),
            prevText = name ?: "",
            onValueChange = {
                name = it
            },
            modifier = Modifier.padding(bottom = 5.dp)
        )

        ChooseSex(
            sex = sex,
            modifier = Modifier.padding(bottom = 5.dp)
        ) {
            sex = if (sex == 'M') 'F'
            else 'M'
        }

        EditTextWithTitle(
            titleText = stringResource(id = R.string.nationality),
            prevText = nationality ?: "",
            onValueChange = {
                nationality = it
            },
            modifier = Modifier.padding(bottom = 5.dp)
        )

        DateField(
            titleText = stringResource(id = R.string.dateOfBirth),
            date = dateOfBirth,
            onValueChange = {
                dateOfBirth = it
            },
            modifier = Modifier.padding(bottom = 5.dp)
        )

        DateField(
            titleText = stringResource(id = R.string.dateOfExpiry),
            date = dateOfExpiry,
            onValueChange = {
                dateOfExpiry = it
            },
            modifier = Modifier.padding(bottom = 5.dp)
        )

        EditTextWithTitle(
            titleText = stringResource(id = R.string.documentNr),
            prevText = documentNr ?: "",
            onValueChange = {
                documentNr = it
            },
            modifier = Modifier.padding(bottom = 5.dp)
        )
        EditTextWithTitle(
            titleText = stringResource(id = R.string.can),
            keyboardType = KeyboardType.Decimal,
            prevText = can ?: "",
            onValueChange = {
                can = it
            },
            modifier = Modifier.padding(bottom = 5.dp)
        )
        EditTextWithTitle(
            titleText = stringResource(id = R.string.placeOfBirth),
            prevText = placeOfBirth ?: "",
            onValueChange = {
                placeOfBirth = it
            },
            modifier = Modifier.padding(bottom = 5.dp)
        )
        EditTextWithTitle(
            titleText = stringResource(id = R.string.mothersName),
            prevText = mothersName ?: "",
            onValueChange = {
                mothersName = it
            },
            modifier = Modifier.padding(bottom = 5.dp)
        )
        EditTextWithTitle(
            titleText = stringResource(id = R.string.nameAtBirth),
            prevText = nameAtBirth ?: "",
            onValueChange = {
                nameAtBirth = it
            },
            modifier = Modifier.padding(bottom = 5.dp)
        )
        EditTextWithTitle(
            titleText = stringResource(id = R.string.authority),
            prevText = authority ?: "",
            onValueChange = {
                authority = it
            },
            modifier = Modifier.padding(bottom = 5.dp)
        )

        ElevatedButton(
            onClick = {
                nationalId.value?.name = name
                nationalId.value?.sex = sex
                nationalId.value?.placeOfBirth = placeOfBirth
                nationalId.value?.nationality = nationality
                nationalId.value?.nameAtBirth = nameAtBirth
                nationalId.value?.mothersName = mothersName
                nationalId.value?.documentNr = documentNr
                nationalId.value?.authority = authority
                nationalId.value?.can = can
                nationalId.value?.nationality = nationality
                nationalId.value?.dateOfBirth = dateOfBirth
                nationalId.value?.dateOfExpiry = dateOfExpiry

                onResult()
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
                text = stringResource(id = R.string.save),
                fontWeight = FontWeight.SemiBold,
                fontSize = 15.sp
            )
        }
    }
}
