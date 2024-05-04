package hu.bme.idselector.ui.createid

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import hu.bme.idselector.R
import hu.bme.idselector.data.NationalId

@Composable
fun DetectionResult(
    nationalId: MutableState<NationalId?>,
    onResult: () -> Unit
) {
    Column(modifier = Modifier
        .verticalScroll(rememberScrollState())
        .padding(10.dp)) {
        DataFieldWithTitle(resourceId = R.string.name, text = nationalId.value?.name)
        DataFieldWithTitle(resourceId = R.string.sex, text = nationalId.value?.sex.toString())
        DataFieldWithTitle(resourceId = R.string.nationality, text = nationalId.value?.nationality)
        DataFieldWithTitle(resourceId = R.string.dateOfBirth, text = nationalId.value?.dateOfBirth.toString())
        DataFieldWithTitle(resourceId = R.string.dateOfExpiry, text = nationalId.value?.dateOfExpiry.toString())
        DataFieldWithTitle(resourceId = R.string.documentNr, text = nationalId.value?.documentNr)
        DataFieldWithTitle(resourceId = R.string.can, text = nationalId.value?.can)
        DataFieldWithTitle(resourceId = R.string.placeOfBirth, text = nationalId.value?.placeOfBirth)
        DataFieldWithTitle(resourceId = R.string.mothersName, text = nationalId.value?.mothersName)
        DataFieldWithTitle(resourceId = R.string.nameAtBirth, text = nationalId.value?.nameAtBirth)
        DataFieldWithTitle(resourceId = R.string.authority, text = nationalId.value?.authority)
    }
}

@Composable
fun DataFieldWithTitle(resourceId: Int, text: String?) {
    if (text != null) {
        Text(
            text = stringResource(id = resourceId),
            fontWeight = FontWeight.Bold,
            fontSize = 20.sp
        )
        Text(text, fontSize = 15.sp, modifier = Modifier.padding(bottom = 15.dp))
    }
}