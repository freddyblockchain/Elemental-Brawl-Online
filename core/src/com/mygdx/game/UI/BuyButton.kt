package com.mygdx.game.UI

import FontManager
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.g2d.Sprite
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.math.Vector3
import com.mygdx.game.GameObjects.Shop.Inventory
import com.mygdx.game.GameObjects.Shop.ShopItem
import com.mygdx.game.UI.UIManager.Companion.uiShapeRenderer
import com.mygdx.game.WINDOW_SCALE
import com.mygdx.game.camera
import com.mygdx.game.player

class BuyButton(val shopItem: ShopItem,val amount: Int): UIElement {

    val size = Vector2(80f * WINDOW_SCALE, 24f * WINDOW_SCALE)

    override val onPress = {}
    override var active = false
    val offset = 8f

    val text = "Buy: $amount G"

    override fun renderShape() {
        val position = Vector3(shopItem.topleft.x,shopItem.topleft.y + offset,0f)
        camera.project(position)
        if(active){
            uiShapeRenderer.begin(ShapeRenderer.ShapeType.Line);
            uiShapeRenderer.color = Color.WHITE
            uiShapeRenderer.rect(position.x, position.y, size.x, size.y);
            uiShapeRenderer.end();
        }
    }
    override fun render(batch: SpriteBatch) {
        val position = Vector3(shopItem.topleft.x + 4f,shopItem.topleft.y + (offset / 2),0f)
        camera.project(position)
        if(active){
            val beforeColor = FontManager.NormalTextFont.color
            FontManager.NormalTextFont.color = if(Inventory.gold >= amount) Color.GREEN else Color.RED
            FontManager.NormalTextFont.draw(batch, text, position.x, position.y + size.y)
            FontManager.NormalTextFont.color = beforeColor
        }
    }

}