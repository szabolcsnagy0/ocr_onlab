package hu.bme.idselector.ui.shared

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import hu.bme.idselector.R

/**
 * Component for custom alert dialog.
 *
 * @param onDismissRequest: () -> Unit = {} - called when the dialog is dismissed
 * @param onConfirmation: () -> Unit = {} - called when the confirm button is clicked
 * @param dialogTitle: String = "" - if it is blank, the title will not be displayed
 * @param dialogText: String = "" - if it is blank, the text will not be displayed
 * @param icon: ImageVector? = Icons.Filled.Warning - if it is null, the icon will not be displayed
 * @param confirmText: String = "" - if it is blank, the confirm button will not be displayed
 * @param dismissText: String = "" - if it is blank, the dismiss button will not be displayed
 */
@Composable
fun CustomAlertDialog(
    onDismissRequest: () -> Unit = {},
    onConfirmation: () -> Unit = {},
    dialogTitle: String = "",
    dialogText: String = "",
    icon: ImageVector? = Icons.Filled.Warning,
    confirmText: String = "",
    dismissText: String = ""
) {
    AlertDialog(
        icon = {
            if (icon != null)
                Icon(imageVector = icon, contentDescription = null, tint = colorResource(id = R.color.orange))
        },
        title = {
            if (dialogTitle.isNotBlank())
                Text(text = dialogTitle)
        },
        text = {
            if (dialogText.isNotBlank())
                Text(text = dialogText)
        },
        onDismissRequest = {
            onDismissRequest()
        },
        confirmButton = {
            if (confirmText.isNotBlank())
                TextButton(
                    onClick = {
                        onConfirmation()
                    }
                ) {
                    Text(
                        confirmText,
                        color = colorResource(R.color.black),
                        fontWeight = FontWeight.SemiBold
                    )
                }
        },
        dismissButton = {
            if (dismissText.isNotBlank())
                TextButton(
                    onClick = {
                        onDismissRequest()
                    }
                ) {
                    Text(
                        dismissText,
                        color = colorResource(R.color.black),
                        fontWeight = FontWeight.SemiBold
                    )
                }
        }
    )
}