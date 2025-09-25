package com.unreel.unreel.networks.repository

sealed class IntentKeywords(val name: String) {
    object StartDestination : IntentKeywords("START_DESTINATION")

    object SESSION_EXPIRED : IntentKeywords("SESSION_EXPIRED")

    object PROFILE_PICTURE : IntentKeywords("PROFILE_PICTURE")
}