package hu.bme.idselector.ui.shared

import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
import androidx.compose.ui.window.Dialog
import hu.bme.idselector.R

@Composable
fun NewProfileDialog(
    onDismiss: () -> Unit = {},
    onSuccess: (String) -> Unit = { _ -> }
) {
    var name by remember {
        mutableStateOf("")
    }

    Dialog(onDismissRequest = onDismiss) {
        ElevatedCard(
            modifier = Modifier
                .fillMaxWidth()
                .height(250.dp)
                .padding(16.dp),
            shape = RoundedCornerShape(16.dp),
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 10.dp, vertical = 10.dp),
                verticalArrangement = Arrangement.SpaceBetween,
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 5.dp),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    EditTextWithTitle(
                        titleText = stringResource(id = R.string.profile_name),
                        prevText = name,
                        onValueChange = { newText ->
                            name = newText
                        },
                        modifier = Modifier
                            .defaultMinSize(minHeight = 35.dp)
                            .background(color = Color.Transparent, shape = CircleShape)
                            .padding(vertical = 10.dp)
                            .fillMaxWidth(),
                    )
                }
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    TextButton(
                        modifier = Modifier.padding(8.dp),
                        onClick = onDismiss,
                    ) {
                        Text(
                            text = stringResource(R.string.cancel),
                            fontWeight = FontWeight.Normal,
                            color = colorResource(id = R.color.black),
                            fontSize = 18.sp
                        )
                    }
                    TextButton(
                        modifier = Modifier.padding(8.dp),
                        onClick = { onSuccess(name) },
                        enabled = name.isNotBlank()
                    ) {
                        Text(
                            text = stringResource(R.string.save),
                            fontWeight = FontWeight.ExtraBold,
                            fontSize = 18.sp
                        )
                    }
                }
            }
        }
    }
}