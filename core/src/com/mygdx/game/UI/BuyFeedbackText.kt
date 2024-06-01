package com.mygdx.game.UI

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.g2d.Sprite
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.math.Vector3
import com.mygdx.game.DefaultTextureHandler
import com.mygdx.game.WINDOW_SCALE
import com.mygdx.game.camera

class BuyFeedbackText(): UIElement {

    val pos = Vector2(Gdx.graphics.width / 2f,Gdx.graphics.height / 2f)

    override val sprite = Sprite()

    val text = "Buying..."

    override fun onPress(): Boolean {
        return true
    }

    override var active = false

    override fun renderShape() {

    }

    override fun render(batch: SpriteBatch) {
        FontManager.NormalTextFont.draw(batch, text, pos.y, pos.y)
    }

}