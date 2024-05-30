package com.mygdx.game.UI

import com.badlogic.gdx.graphics.g2d.Sprite
import com.badlogic.gdx.math.Rectangle

interface UIElement {
    val onPress: () -> Unit
    val sprite: Sprite
    var active: Boolean
}