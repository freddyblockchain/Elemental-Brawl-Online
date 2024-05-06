package com.mygdx.game.Managers

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.math.Vector2
import com.mygdx.game.*
import com.mygdx.game.GameObjects.GameObject.MoveableObject
import com.mygdx.game.GameObjects.MoveableEntities.Characters.Player


class ClientStateManager {

    companion object {
        var T0 = 0L
        var T1 = 0L + 50L
        var startTime = System.currentTimeMillis()
        val stateUpdateTime = 50L

        fun serverUpdateState(gameState: GameState){
            //update time
            updateClientTime(gameState.gameTime)

            gameState.playerStates.forEach { entry ->
                val state = entry.value
                var correspondingPlayer =
                    AreaManager.getActiveArea()?.gameObjects?.firstOrNull { it is Player && it.playerNum == state.playerNum } as Player?

                if(correspondingPlayer == null){
                    correspondingPlayer = Player(GameObjectData(x = state.position.first.toInt(), y = state.position.second.toInt()), Vector2(32f,32f), state.playerNum)
                    AreaManager.getActiveArea()?.gameObjects?.add(correspondingPlayer)
                }
                updateObjectFuture(Vector2(state.position.first, state.position.second), correspondingPlayer)
                setIncrement(correspondingPlayer)
            }
        }

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

        fun updateObjectFuture(X1: Vector2, moveableObject: MoveableObject){
            moveableObject.X1 = X1
            moveableObject.X0 = moveableObject.currentPosition()
        }
        fun setIncrement(moveableObject: MoveableObject){
            moveableObject.increment = Vector2((moveableObject.X1 - moveableObject.X0)/(stateUpdateTime.toFloat() / (Gdx.graphics.deltaTime * 1000)))
        }
    }
}