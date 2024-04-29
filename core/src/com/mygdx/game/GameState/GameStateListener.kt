package com.mygdx.game.GameState

import com.badlogic.gdx.math.Vector2
import com.mygdx.game.*
import com.mygdx.game.Managers.GameState

interface GameStateListener {
    fun processGameState(gameState: GameState)
}
class MovePlayers(): GameStateListener{
    override fun processGameState(gameState: GameState) {
        //println("game state is: " + gameState)
        gameState.playerStates.forEach {
            val playerData = it.value
            val player = players[playerData.playerNum]
            player?.status = playerData.status
           // player?.setPosition(Vector2((playerData.position.first), playerData.position.second))
            X0 = player!!.currentPosition()
            X1 = Vector2(it.value.position.first, it.value.position.second)
            //println(Vector2((playerData.position.first), playerData.position.second))
            player?.currentUnitVector = Vector2(playerData.unitVectorDirection.first, playerData.unitVectorDirection.second)
            player?.speed = playerData.speed
        }
    }

}

