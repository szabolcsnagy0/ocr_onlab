package hu.bme.idselector.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import hu.bme.idselector.R
import hu.bme.idselector.data.Profile

@Composable
fun ProfileElement(
    profile: Profile = Profile(
        id = 1,
        name = "John Doe",
        nationalId = null
    ),
    onItemSelected: () -> Unit = {}
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 10.dp, vertical = 10.dp)
            .height(100.dp)
            .clickable { onItemSelected() }
    ) {
        ElevatedCard(
            colors = CardDefaults.cardColors(
                containerColor = colorResource(id = R.color.white),
                contentColor = colorResource(id = R.color.grey)
            ),
            elevation = CardDefaults.elevatedCardElevation(10.dp),
            modifier = Modifier
                .fillMaxWidth()
                .height(100.dp)
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
                modifier = Modifier
                    .fillMaxSize()
            ) {
                profile.name?.let {
                    Text(text = it, fontSize = 30.sp, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}