package com.mygdx.game.Models

import com.badlogic.gdx.math.Vector2
import com.mygdx.game.GameObjects.MoveableEntities.Characters.PLAYER_STATUS
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

enum class FIGHTER_STATE {FREE, STUNNED, DASHING}

enum class GameObjectType{PLAYER, FIREBALL, ICICLE, SNOWBALL}

@Serializable
data class ServerGameObjectData(val position: Pair<Float, Float>, val size: Pair<Float, Float>, val unitVectorDirection: Pair<Float,Float>, val speed: Float, val gameObjectNum: Int, val gameObjectType: GameObjectType)

@Serializable
open class ServerGameObject(val serverGameObjectData: ServerGameObjectData, val customFields: CustomFields = CustomFields.EmptyCustomFields)

@Serializable
sealed interface CustomFields{
    @Serializable
    @SerialName("Empty")
    data object EmptyCustomFields : CustomFields
    @Serializable
    @SerialName("PlayerCustomFields")
    class PlayerCustomFields(val fighterState: FIGHTER_STATE, val playerHealth: Float): CustomFields
}

@Serializable
sealed interface PlayerEvent{
    @Serializable
    val timestamp: Long

    @Serializable
    @SerialName("PlayerDeath")
    class PlayerDeath(val deadPlayer: Int, val killingPlayer: Int, override val timestamp: Long): PlayerEvent
}

@Serializable
data class GameState(val objectStates: List<ServerGameObject>, val gameTime: Long, val playerEvents: List<PlayerEvent>)

@Serializable
data class SseEvent(
    val id: String,
    val event: String,
    val data: GameState
)
