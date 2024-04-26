package com.mygdx.game.GameState

import com.mygdx.game.gameState

class GameStateManager {
    companion object {
        val gameStateListeners = mutableListOf<GameStateListener>()
        init {
            gameStateListeners.add(MovePlayers())
        }
        fun executeGameStateListeners(){
            gameStateListeners.forEach {
                it.processGameState(gameState)
            }
        }
    }
}