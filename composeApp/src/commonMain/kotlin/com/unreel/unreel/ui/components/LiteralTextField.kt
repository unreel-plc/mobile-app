package com.unreel.unreel.ui.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import com.unreel.unreel.ui.components.PhoneNumberVisualTransformation


@Composable
fun LiteralTextField(
    modifier: Modifier = Modifier,
    value: String,
    label: String? = null,
    onValueChanged: (String) -> Unit,
) {
    var text by remember(value) { mutableStateOf(value) }
    var isTextFieldTouched by remember { mutableStateOf(false) }

    OutlinedTextField(
        value = text,
        onValueChange = { newValue ->
            val filteredText = newValue.filter { it.isDigit() }.take(9)
            if (filteredText != text) {
                text = filteredText
                onValueChanged(filteredText)
            }
            isTextFieldTouched = true
        },
        label = { Text(label ?: "Phone Number") },
        isError = isTextFieldTouched && (text.startsWith("0") || !text.startsWith("9") || text.length < 9),
        supportingText = if (isTextFieldTouched) {
            when {
                text.startsWith("0") -> {
                    {
                        Text(
                            text = "Phone Number must start with 9 not 0",
                            color = MaterialTheme.colorScheme.error
                        )
                    }
                }
                !text.startsWith("9") -> {
                    {
                        Text(
                            text = "Phone Number must start with 9",
                            color = MaterialTheme.colorScheme.error
                        )
                    }
                }
                text.length < 9 -> {
                    {
                        Text(
                            text = "Phone Number must be 9 digits",
                            color = MaterialTheme.colorScheme.error
                        )
                    }
                }
                else -> null
            }
        } else {
            null
        },
        modifier =modifier,
        visualTransformation = PhoneNumberVisualTransformation(),
        textStyle = LocalTextStyle.current,
        shape = MaterialTheme.shapes.extraSmall,
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = MaterialTheme.colorScheme.primary,
            unfocusedBorderColor = MaterialTheme.colorScheme.primary,
            cursorColor = MaterialTheme.colorScheme.primary,
            errorBorderColor = MaterialTheme.colorScheme.error,
            errorCursorColor = MaterialTheme.colorScheme.error
        ),
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Phone,
            imeAction = ImeAction.Next
        ),
    )
}

@Composable
 fun PasswordTextField(
    value: String,
    onValueChanged: (String) -> Unit,
    label: String,
    isOldPassword: Boolean = false
) {
    var text by remember(value) { mutableStateOf(value) }
    var isPasswordVisible by remember { mutableStateOf(false) }
    var isTextFieldTouched by remember { mutableStateOf(false) }

    OutlinedTextField(
        value = text,
        onValueChange = { newText ->
            val filteredText = newText
            if (filteredText != text) {
                text = filteredText
                onValueChanged(filteredText)
            }
            isTextFieldTouched = true
        },
        label = { Text(label) },
        isError = isTextFieldTouched && text.length < 6 && !isOldPassword,
        supportingText = if (isTextFieldTouched && text.length < 6 && !isOldPassword) {
            {
                Text(
                    text = "Password must be strong",
                    color = MaterialTheme.colorScheme.error
                )
            }
        } else {
            null
        },
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 30.dp),
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Password,
            imeAction = ImeAction.Next
        ),
        visualTransformation = if (isPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
        textStyle = LocalTextStyle.current,
        shape = MaterialTheme.shapes.extraSmall,
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = MaterialTheme.colorScheme.primary,
            unfocusedBorderColor = MaterialTheme.colorScheme.primary,
            cursorColor = MaterialTheme.colorScheme.primary,
            errorBorderColor = MaterialTheme.colorScheme.error,
            errorCursorColor = MaterialTheme.colorScheme.error
        ),
        trailingIcon = {
            IconButton(onClick = { isPasswordVisible = !isPasswordVisible }) {
                Icon(
                    imageVector = if (isPasswordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                    contentDescription = if (isPasswordVisible) "Hide Password" else "Show Password",
                    tint = MaterialTheme.colorScheme.outline
                )
            }
        }
    )
}