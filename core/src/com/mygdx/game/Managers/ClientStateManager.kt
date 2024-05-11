package com.mygdx.game.Managers

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.math.Vector2
import com.mygdx.game.*
import com.mygdx.game.GameObjects.GameObject.MoveableObject
import com.mygdx.game.GameObjects.MoveableEntities.Characters.Player
import com.mygdx.game.GameObjects.MoveableObjects.Projectile.Fireball
import com.mygdx.game.Models.GameObjectType
import com.mygdx.game.Models.GameState
import com.mygdx.game.Models.ServerGameObject


class ClientStateManager {

    companion object {
        var T0 = 0L
        var T1 = 0L + 50L
        var startTime = System.currentTimeMillis()
        val stateUpdateTime = 50L

        fun clientUpdateState(){
            updateClientTime(T1 + (Gdx.graphics.deltaTime * 1000).toLong())
        }

        private fun updateClientTime(newT1: Long){
            if(T0 >= T1){
                T0 = T1 - 1
                //Handle the case where our prediction makes it so that we are actally in front of T1
            }
            startTime = System.currentTimeMillis()
            T0 = T1
            T1 = newT1
        }

        fun serverUpdateState(gameState: GameState){
            //update time
            updateClientTime(gameState.gameTime)

            gameState.objectStates.forEach { entry ->
                parseServerGameObject(entry)
            }
        }

        fun parseServerGameObject(serverGameObject: ServerGameObject){
            val serverGameObjectData = serverGameObject.serverGameObjectData
            val objectInAreaManager = AreaManager.getActiveArea()!!.gameObjects.filterIsInstance<MoveableObject>().firstOrNull() { it.gameObjectNumber == serverGameObjectData.gameObjectNum }
            if(objectInAreaManager != null){
                setObjectBasedOnData(objectInAreaManager as MoveableObject, serverGameObject)
            } else {
                val gameObject: MoveableObject =  when(serverGameObjectData.gameObjectType){
                    GameObjectType.PLAYER -> Player(gameObjectData = GameObjectData(x = serverGameObjectData.position.first.toInt(), y = serverGameObjectData.position.second.toInt()), Vector2(32f,32f), serverGameObjectData.gameObjectNum)
                    GameObjectType.FIREBALL -> Fireball(gameObjectData = GameObjectData(x = serverGameObjectData.position.first.toInt(), y = serverGameObjectData.position.second.toInt()), size = Vector2(60f,30f), unitVectorDirection = Vector2(serverGameObjectData.unitVectorDirection), gameObjectNumber = serverGameObjectData.gameObjectNum)
                }
                AreaManager.getActiveArea()!!.gameObjects.add(gameObject)
            }
        }

        fun setObjectBasedOnData(gameObject: MoveableObject, serverGameObject: ServerGameObject){
            val serverGameObjectData = serverGameObject.serverGameObjectData
            gameObject.speed = serverGameObjectData.speed
            gameObject.currentUnitVector = Vector2(serverGameObjectData.unitVectorDirection.first, serverGameObjectData.unitVectorDirection.second)
            updateObjectFuture(Vector2(serverGameObjectData.position.first, serverGameObjectData.position.second), gameObject)
            setIncrement(gameObject)
        }

        fun updateObjectFuture(X1: Vector2, moveableObject: MoveableObject){
            moveableObject.X1 = X1
            moveableObject.X0 = moveableObject.currentPosition()
        }
        fun setIncrement(moveableObject: MoveableObject){
            moveableObject.increment = Vector2((moveableObject.X1 - moveableObject.X0)/(stateUpdateTime.toFloat() / (Gdx.graphics.deltaTime * 1000)))
        }
    }
}