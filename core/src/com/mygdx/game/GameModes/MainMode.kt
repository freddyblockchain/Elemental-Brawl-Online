package com.mygdx.game.GameModes

import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.mygdx.game.GameObjects.GameObject.FightableEntity
import com.mygdx.game.Managers.AreaManager
import com.mygdx.game.Input.MyInputProcessor

class MainMode(val inputProcessor: MyInputProcessor): GameMode {
    override val spriteBatch = SpriteBatch()

    override fun FrameAction() {
        for(gameObject in AreaManager.getActiveArea()!!.gameObjects.toMutableList()){
            if(gameObject is FightableEntity){
                gameObject.showHealth(gameObject)
            }
            gameObject.frameTask()
        }
        inputProcessor.handleInput()
    }
}