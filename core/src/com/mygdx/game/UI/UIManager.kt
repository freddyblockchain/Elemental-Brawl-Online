package com.mygdx.game.UI

import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.mygdx.game.Abilities.AbilityManager.Companion.fireballAbility

class UIManager {
    companion object {
        val uiElements = mutableListOf<UIElement>()
        var UISpriteBatch = SpriteBatch()
        val uiShapeRenderer = ShapeRenderer()
        init {
            uiElements.add(AbilityButton(fireballAbility) { fireballAbility.onPress()})
        }
        fun render(){
            UISpriteBatch.begin()
            uiElements.forEach {
                it.render(UISpriteBatch)
            }
            UISpriteBatch.end()
            uiElements.forEach {
                it.renderShape()
            }
        }
    }
}