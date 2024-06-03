package com.mygdx.game.Action

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
sealed interface PlayerAction {
    @Serializable
    @SerialName("Move")
    data class Move(val pos: Pair<Float,Float>): PlayerAction
    @Serializable
    @SerialName("FireAbility")
    data class FireAbility(val pos: Pair<Float,Float>): PlayerAction
    @Serializable
    @SerialName("UpdatePlayerState")
    class UpdatePlayerState: PlayerAction
}