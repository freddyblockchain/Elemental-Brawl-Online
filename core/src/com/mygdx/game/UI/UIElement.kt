package com.mygdx.game.UI

import com.badlogic.gdx.graphics.g2d.Sprite
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.math.Rectangle

interface UIElement {
    fun onPress(): Boolean
    var active: Boolean
    val sprite: Sprite
    fun renderShape(): Unit
    fun render(batch: SpriteBatch): Unit
}