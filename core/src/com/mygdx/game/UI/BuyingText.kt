package com.mygdx.game.UI

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.g2d.Sprite
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.math.Vector2

object BuyingText: UIElement {
    val pos = Vector2(Gdx.graphics.width / 2f,Gdx.graphics.height / 2f)
    var buying = false
    override val sprite = Sprite()
    val text = "Buying..."

    override fun onPress(): Boolean {
        return true
    }

    override var active = false

    override fun renderShape() {

    }

    override fun render(batch: SpriteBatch) {
        if(buying){
            FontManager.NormalTextFont.draw(batch, text, pos.x, pos.y)
        }
    }

}