package com.mygdx.game.GameState

import com.badlogic.gdx.math.Vector2
import com.mygdx.game.Managers.GameState
import com.mygdx.game.players

interface GameStateListener {
    fun processGameState(gameState: GameState)
}
class MovePlayers(): GameStateListener{
    override fun processGameState(gameState: GameState) {
        gameState.playerStates.forEach {
            val playerData = it.value
            val player = players[playerData.playerNum]
            player?.status = playerData.status
            player?.setPosition(Vector2( playerData.position.first, playerData.position.second))
            println("new pos " +  Vector2(playerData.position.first, playerData.position.second))
        }
    }

}

