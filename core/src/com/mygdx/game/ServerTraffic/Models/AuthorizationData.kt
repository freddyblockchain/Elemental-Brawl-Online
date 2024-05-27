package com.mygdx.game.ServerTraffic.Models

import kotlinx.serialization.Serializable

@Serializable
data class AuthorizationData(
    val signedMessage: String, // The digital signature
    val roundNrSigned: Long,   // The blockchain round number
    val algorandAddress: String, // The Algorand address of the signer
    val roundSeed: String,
    val localPort: Int,
    val ipAddress: String
)