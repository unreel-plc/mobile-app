package com.unreel.unreel.core.common.utils

import com.unreel.unreel.core.common.utils.Constants

data class LoadingState(
    val isLoading: Boolean = false,
    val message: String? = null,
)

fun fixedImageUrl(url: Any?): String {
    return if (url is String) {
        if (url.startsWith("http")) url else Constants.BASE_URL_IMAGE + url
    } else {
        Constants.DEFAULT_PROFILE_PICTURE
    }
}