package hu.bme.idselector.ui.createid

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import hu.bme.idselector.R
import hu.bme.idselector.data.Person

@Preview(showBackground = true)
@Composable
fun DetectionResult(
    person: Person = Person(
        "Test",
        "Test",
        "Test",
        "Test",
        "Test",
        "Test",
        "Test",
        "test",
        "test",
        "Test",
        "test"
    )
) {
    Column(modifier = Modifier
        .verticalScroll(rememberScrollState())
        .padding(10.dp)) {
        DataFieldWithTitle(resourceId = R.string.name, text = person.name)
        DataFieldWithTitle(resourceId = R.string.sex, text = person.sex)
        DataFieldWithTitle(resourceId = R.string.nationality, text = person.nationality)
        DataFieldWithTitle(resourceId = R.string.dateOfBirth, text = person.dateOfBirth)
        DataFieldWithTitle(resourceId = R.string.dateOfExpiry, text = person.dateOfExpiry)
        DataFieldWithTitle(resourceId = R.string.documentNr, text = person.documentNr)
        DataFieldWithTitle(resourceId = R.string.can, text = person.can)
        DataFieldWithTitle(resourceId = R.string.placeOfBirth, text = person.placeOfBirth)
        DataFieldWithTitle(resourceId = R.string.mothersName, text = person.mothersName)
        DataFieldWithTitle(resourceId = R.string.nameAtBirth, text = person.nameAtBirth)
        DataFieldWithTitle(resourceId = R.string.authority, text = person.authority)
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