package com.unreel.unreel.ui.components
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Error
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.ui.text.font.FontWeight



@Composable
fun ErrorDialog(
    title: String = "Error",
    message: String,
    onConfirm: () -> Unit = {},
) {
    BaseDialog(
        title = title,
        icon = Icons.Filled.Error,
        message = message,
        onDismiss = onConfirm,
        onConfirm = onConfirm,
    )
}

@Composable
fun SuccessDialog(
    message: String,
    onConfirm: () -> Unit = {},
    title: String = "Success Message",
) {
    BaseDialog(
        title = title,
        //icon = Icons.Filled.Lightbulb,
        message = message,
        onDismiss = onConfirm,
        onConfirm = onConfirm
    )
}

@Composable
fun BaseDialog(
    title: String,
    icon: ImageVector? = null,
    message: String,
    onDismiss: () -> Unit = {},
    onConfirm: () -> Unit = {},
    showCancelButton: Boolean = false,
) {
    AlertDialog(
        modifier = Modifier.fillMaxWidth(),
        icon = {
            /*if (icon != null) {
                Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(icon, null)
                }
            }*/
        },
        shape = RectangleShape,
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(onClick = onConfirm) {
                Text("OK")
            }
        },
        dismissButton = {
            if (showCancelButton) {
                TextButton(onClick = onDismiss) {
                    Text("Cancel")
                }
            }
        },

        title = {
            Text(
                text = title,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth(),
            )
        },
        text = { Text(text = message, style = TextStyle(fontSize = 15.sp)) },
        containerColor = Color.White,
    )
}

@Composable
fun LoadingDialog(
    message: String,
    onDismiss: () -> Unit = {},
) {
    AlertDialog(
        modifier = Modifier
            .height(120.dp)
            .fillMaxWidth(),
        containerColor = Color.White,
        shape = RectangleShape,
        onDismissRequest = onDismiss,
        text = {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                CircularProgressIndicator()
                Text(message)
            }
        },
        title = {},
        confirmButton = {},
        dismissButton = {},
    )
}




@Composable
fun ConfirmationDialog(
    title: String,
    message: String,
    confirmButtonText: String = "Confirm",
    cancelButtonText: String = "Cancel",
    onConfirm: () -> Unit,
    onCancel: () -> Unit,
    modifier: Modifier = Modifier
) {
    Dialog(onDismissRequest = onCancel) {
        Card(
            modifier = modifier
                .fillMaxWidth()
                .padding(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text(
                    text = title,
                    style = TextStyle(
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp
                    )
                )
                Text(
                    text = message,
                    style = TextStyle(fontSize = 16.sp),
                    textAlign = TextAlign.Center
                )
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    TextButton(onClick = onCancel) {
                        Text(cancelButtonText)
                    }
                    Button(onClick = onConfirm) {
                        Text(confirmButtonText)
                    }
                }
            }
        }
    }
}