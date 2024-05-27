package com.mygdx.game.Models

import com.mygdx.game.GameObjects.MoveableEntities.Characters.PLAYER_STATUS
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

enum class GameObjectType{PLAYER, FIREBALL}

@Serializable
data class ServerGameObjectData(val position: Pair<Float, Float>, val unitVectorDirection: Pair<Float,Float>, val speed: Float, val gameObjectNum: Int, val gameObjectType: GameObjectType)

@Serializable
open class ServerGameObject(val serverGameObjectData: ServerGameObjectData, val customFields: CustomFields = CustomFields.EmptyCustomFields)
@Serializable
sealed interface CustomFields{
    @Serializable
    @SerialName("Empty")
    data object EmptyCustomFields : CustomFields
    @Serializable
    @SerialName("PlayerCustomFields")
    class PlayerCustomFields(val status: PLAYER_STATUS, val playerHealth: Float): CustomFields
}
@Serializable
data class GameState(val objectStates: List<ServerGameObject>, val gameTime: Long)

@Serializable
data class SseEvent(
    val id: String,
    val event: String,
    val data: GameState
)