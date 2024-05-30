package com.mygdx.game.UI

import com.badlogic.gdx.graphics.g2d.Sprite
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.mygdx.game.Abilities.AbilityManager
import com.mygdx.game.Abilities.AbilityManager.Companion.fireballAbility
import com.mygdx.game.DefaultTextureHandler

class UIManager {
    companion object {
        val abilityButtons = mutableListOf<AbilityButton>()
        var UISpriteBatch = SpriteBatch()
        val uiShapeRenderer = ShapeRenderer()
        init {
            val fireballAbilityTimer = fireballAbility.timer
            abilityButtons.add(AbilityButton(Sprite(DefaultTextureHandler.getTexture("fireball.png")), fireballAbilityTimer) { fireballAbility.onPress()})
        }
        fun render(){
            UISpriteBatch.begin()
            abilityButtons.forEach {
                it.render(UISpriteBatch)
            }
            UISpriteBatch.end()
            abilityButtons.forEach {
                it.renderButton()
            }
        }
    }
}