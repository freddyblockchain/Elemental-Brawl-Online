package com.mygdx.game.UI

import FontManager
import com.badlogic.gdx.graphics.Color
import com.mygdx.game.GameObjects.Shop.Inventory
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.g2d.Sprite
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.math.Vector2

object GoldText: UIElement {
    val pos = Vector2(Gdx.graphics.width.toFloat() - 650f,Gdx.graphics.height - 100f)
    override val sprite = Sprite()
    var loading = false
    private var text = ""
    override fun onPress(): Boolean {
        return true
    }
    override var active = true
    override fun renderShape() {

    }
    override fun render(batch: SpriteBatch) {
        text = if(loading) "Loading Gold" else "${Inventory.gold} G"
        val beforeColor = FontManager.NormalTextFont.color
        FontManager.NormalTextFont.color = Color.YELLOW
        FontManager.NormalTextFont.draw(batch, text, pos.x, pos.y)
        FontManager.NormalTextFont.color = beforeColor
    }

}