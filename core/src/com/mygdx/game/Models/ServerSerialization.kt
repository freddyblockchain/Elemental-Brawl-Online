package com.mygdx.game.Models

import com.badlogic.gdx.math.Vector2
import com.mygdx.game.GameObjectData
import com.mygdx.game.GameObjects.GameObject.MoveableObject
import com.mygdx.game.GameObjects.MoveableEntities.Characters.PLAYER_STATUS
import com.mygdx.game.GameObjects.MoveableEntities.Characters.Player
import com.mygdx.game.GameObjects.MoveableObjects.Projectile.Fireball
import com.mygdx.game.Managers.AreaManager
import com.mygdx.game.Managers.ClientStateManager.Companion.setIncrement
import com.mygdx.game.Managers.ClientStateManager.Companion.updateObjectFuture
import com.mygdx.game.Vector2
import kotlinx.serialization.Serializable

enum class GameObjectType{PLAYER, FIREBALL}

@Serializable
data class ServerGameObjectData(val position: Pair<Float, Float>, val unitVectorDirection: Pair<Float,Float>, val speed: Float, val gameObjectNum: Int, val gameObjectType: GameObjectType)

@Serializable
open class ServerGameObject(val serverGameObjectData: ServerGameObjectData)
@Serializable
class PlayerData(val playerGameObjectData: ServerGameObjectData, val status: PLAYER_STATUS): ServerGameObject(playerGameObjectData)

@Serializable
data class FireballData(val fireballGameObjectData: ServerGameObjectData): ServerGameObject(fireballGameObjectData)

@Serializable
data class GameState(val objectStates: List<ServerGameObject>, val gameTime: Long)