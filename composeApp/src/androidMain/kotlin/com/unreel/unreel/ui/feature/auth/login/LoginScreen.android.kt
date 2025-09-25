package com.unreel.unreel.ui.feature.auth.login

actual fun platformBase64Decode(input: String): ByteArray =
    java.util.Base64.getUrlDecoder().decode(input)