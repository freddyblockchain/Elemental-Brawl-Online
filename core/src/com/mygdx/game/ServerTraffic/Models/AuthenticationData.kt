package com.mygdx.game.ServerTraffic.Models

import com.mygdx.game.Models.VerificationData
import kotlinx.serialization.Serializable

@Serializable
data class AuthenticationData(
    val verificationData: VerificationData,
    val localPort: Int,
)