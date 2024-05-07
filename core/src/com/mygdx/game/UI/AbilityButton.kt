package com.mygdx.game.UI
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.g2d.Sprite
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.math.Rectangle
import com.badlogic.gdx.math.Vector2

class AbilityButton(override val sprite: Sprite, override val onPress: () -> Unit) : UIElement {
    init {
        sprite.setSize(200f, 200f)
        sprite.setPosition(Gdx.graphics.width - 250f, 100f)
    }

    fun render(UIbatch: SpriteBatch) {
        sprite.draw(UIbatch)
    }
}