package com.mygdx.game.Action

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
sealed interface Action {
    @Serializable
    @SerialName("TouchAction")
    data class TouchAction(val pos: Pair<Float,Float>): Action
}