package com.unreel.unreel

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform