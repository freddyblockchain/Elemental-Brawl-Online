package com.mygdx.game.Managers

import VerificationManager
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.math.Vector2
import com.mygdx.game.*
import com.mygdx.game.Algorand.AlgorandManager
import com.mygdx.game.GameObjects.GameObject.MoveableObject
import com.mygdx.game.GameObjects.MoveableEntities.Characters.Player
import com.mygdx.game.GameObjects.MoveableObjects.Projectile.Fireball
import com.mygdx.game.GameObjects.MoveableObjects.Projectile.Icicle
import com.mygdx.game.GameObjects.MoveableObjects.Projectile.Snowball
import com.mygdx.game.GameObjects.MoveableObjects.Projectile.projectileFactory
import com.mygdx.game.Models.*
import com.mygdx.game.Particles.FireDashEffect
import com.mygdx.game.UI.GoldText


class ClientStateManager {

    companion object {
        var T0 = 0L
        var T1 = 0L + 50L
        var startTime = System.currentTimeMillis()
        val stateUpdateTime = 50L

        var lastEventTime: Long = 0

        fun clientUpdateState() {
            updateClientTime(T1 + (Gdx.graphics.deltaTime * 1000).toLong())
        }

        private fun updateClientTime(newT1: Long) {
            if (T0 >= T1) {
                T0 = T1 - 1
                //Handle the case where our prediction makes it so that we are actally in front of T1
            }
            startTime = System.currentTimeMillis()
            T0 = T1
            T1 = newT1
        }

        fun serverUpdateState(gameState: GameState) {
            VerificationManager.serverUUID = gameState.serverUUID
            //update time
            updateClientTime(gameState.gameTime)
            //Handle server objects
            gameState.objectStates.forEach { entry ->
                parseServerGameObject(entry)
            }
            removeOldObjects(gameState.objectStates)

            //Handle player events
            val newEvents = gameState.playerEvents.filter { it.timestamp > lastEventTime }
            if(newEvents.isNotEmpty()){
                lastEventTime = newEvents.map { it.timestamp }.max()
                handleEvents(newEvents)
            }
        }

        fun parseServerGameObject(serverGameObject: ServerGameObject) {
            val serverGameObjectData = serverGameObject.serverGameObjectData
            val existingObjects = AreaManager.getActiveArea()!!.gameObjects.filterIsInstance<MoveableObject>()
            val objectInAreaManager =
                existingObjects.firstOrNull() { it.gameObjectNumber == serverGameObjectData.gameObjectNum }
            if (objectInAreaManager != null) {
                setObjectBasedOnData(objectInAreaManager, serverGameObject)
            } else {
                val gameObject: MoveableObject = when (serverGameObjectData.gameObjectType) {
                    GameObjectType.PLAYER -> Player(
                        gameObjectData = GameObjectData(
                            x = serverGameObjectData.position.first.toInt(),
                            y = serverGameObjectData.position.second.toInt()
                        ), Vector2(serverGameObjectData.size), serverGameObjectData.gameObjectNum
                    )

                    GameObjectType.FIREBALL -> projectileFactory(serverGameObjectData, ::Fireball)
                    GameObjectType.ICICLE -> projectileFactory(serverGameObjectData, ::Icicle)
                    GameObjectType.SNOWBALL -> projectileFactory(serverGameObjectData, ::Snowball)
                }
                gameObject.X0 = Vector2(serverGameObjectData.position.first, serverGameObjectData.position.second)
                gameObject.X1 = Vector2(serverGameObjectData.position.first, serverGameObjectData.position.second)
                AreaManager.getActiveArea()!!.gameObjects.add(gameObject)
            }

        }

        fun setObjectBasedOnData(gameObject: MoveableObject, serverGameObject: ServerGameObject) {
            val serverGameObjectData = serverGameObject.serverGameObjectData
            gameObject.currentSpeed = serverGameObjectData.speed
            gameObject.currentUnitVector =
                Vector2(serverGameObjectData.unitVectorDirection.first, serverGameObjectData.unitVectorDirection.second)
            updateObjectFuture(
                Vector2(serverGameObjectData.position.first, serverGameObjectData.position.second),
                gameObject
            )
            setIncrement(gameObject)
            if (gameObject is Player) {
                val customData = serverGameObject.customFields as CustomFields.PlayerCustomFields
                gameObject.health = customData.playerHealth

                if(gameObject.fighterState != FIGHTER_STATE.DASHING && customData.fighterState == FIGHTER_STATE.DASHING){
                    val fireDashEffect = FireDashEffect(gameObject)
                    gameObject.properties.add(fireDashEffect)
                    fireDashEffect.start()
                } else if(gameObject.fighterState == FIGHTER_STATE.DASHING && customData.fighterState != FIGHTER_STATE.DASHING){
                    val fireDashEffects = gameObject.properties.filterIsInstance<FireDashEffect>()
                    gameObject.properties.removeAll(fireDashEffects)
                }
                gameObject.fighterState = customData.fighterState

            }
        }

        fun updateObjectFuture(X1: Vector2, moveableObject: MoveableObject) {
            moveableObject.X1 = X1
            moveableObject.X0 = moveableObject.currentPosition()
        }

        fun setIncrement(moveableObject: MoveableObject) {
            moveableObject.increment =
                Vector2((moveableObject.X1 - moveableObject.X0) / (stateUpdateTime.toFloat() / (Gdx.graphics.deltaTime * 1000)))
        }

        fun removeOldObjects(serverGameObjects: List<ServerGameObject>) {
            val serverGameObjectNumbers = serverGameObjects.map { it.serverGameObjectData.gameObjectNum }
            val existingObjects = AreaManager.getActiveArea()!!.gameObjects.filterIsInstance<MoveableObject>()
            existingObjects.forEach {
                if (it.gameObjectNumber !in serverGameObjectNumbers) {
                    AreaManager.getActiveArea()!!.gameObjects.remove(it)
                }
            }
        }

        fun handleEvents(playerEventList: List<PlayerEvent>){
            playerEventList.forEach {
                if(it is PlayerEvent.PlayerDeath){
                    if(it.killingPlayer == player.playerNum){
                        GoldText.loading = true
                        AlgorandManager.updateGoldCount(6000)
                    }
                }
            }
        }
    }
}