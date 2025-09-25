package com.unreel.unreel.ui.components

import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation

class PhoneNumberVisualTransformation : VisualTransformation {
    override fun filter(text: AnnotatedString): TransformedText {
        val prefix = "+251 | "
        val transformedText = prefix + text.text

        return TransformedText(
            text = AnnotatedString(transformedText),
            offsetMapping = object : OffsetMapping {
                override fun originalToTransformed(offset: Int): Int {
                    return offset + prefix.length
                }

                override fun transformedToOriginal(offset: Int): Int {
                    return (offset - prefix.length).coerceAtLeast(0)
                }
            }
        )
    }
}