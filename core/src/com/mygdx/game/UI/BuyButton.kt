package com.mygdx.game.UI

import FontManager
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.g2d.Sprite
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.math.Vector3
import com.mygdx.game.Algorand.AlgorandManager
import com.mygdx.game.DefaultTextureHandler
import com.mygdx.game.GameObjects.Shop.InventoryManager
import com.mygdx.game.GameObjects.Shop.ShopItem
import com.mygdx.game.GameObjects.Shop.ShopManager
import com.mygdx.game.WINDOW_SCALE
import com.mygdx.game.camera

class BuyButton(val shopItem: ShopItem): UIElement {

    val price
        get() = ShopManager.shopItemPrices[shopItem.abilityAsa] ?: 999999
    val size = Vector2(90f * WINDOW_SCALE, 24f * WINDOW_SCALE)
    override var active = false
    override val sprite = Sprite(DefaultTextureHandler.getTexture("Box.png"))
    val offsety = 16
    val offsetx = 48

    init {
        sprite.setSize(size.x, size.y)
        val position = Vector3(200f, 32f, 0f)
        println("pos before: " + position)
        camera.project(position)
        println("pos after: " + position)
        sprite.setPosition(position.x, Gdx.graphics.height - position.y - sprite.height)
    }
    override fun renderShape() {
    }
    override fun render(batch: SpriteBatch) {
        if(active){
            sprite.color = Color.BLACK
            sprite.draw(batch)
            val beforeColor = FontManager.NormalTextFont.color
            FontManager.NormalTextFont.color = if(InventoryManager.gold >= price) Color.GREEN else Color.RED
            FontManager.NormalTextFont.draw(batch, "Buy: $price G", sprite.x + offsetx, sprite.y + sprite.height - offsety)
            FontManager.NormalTextFont.color = beforeColor
        }
    }

    override fun onPress(): Boolean {
        if(InventoryManager.gold >= price && active && !BuyingText.buying && !GoldText.loading){
            BuyingText.buying = true
            AlgorandManager.buyAbility(shopItem.abilityAsa)
            return true
        }
        return false
    }

}