package com.mygdx.game.Action

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
sealed interface TouchAction {
    @Serializable
    @SerialName("Move")
    data class Move(val pos: Pair<Float,Float>): TouchAction
    @Serializable
    @SerialName("FireAbility")
    data class FireAbility(val pos: Pair<Float,Float>): TouchAction
}