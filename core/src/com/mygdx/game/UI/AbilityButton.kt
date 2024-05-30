package com.mygdx.game.UI
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.g2d.Sprite
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.badlogic.gdx.math.Rectangle
import com.badlogic.gdx.math.Vector2
import com.mygdx.game.UI.UIManager.Companion.uiShapeRenderer

class AbilityButton(override val sprite: Sprite, override val onPress: () -> Unit) : UIElement {

    override var active = false
    init {
        sprite.setSize(200f, 200f)
        sprite.setPosition(Gdx.graphics.width - 250f, 100f)
    }

    fun render(UIbatch: SpriteBatch) {
        sprite.draw(UIbatch)
    }
    fun renderButton(){
        // Draw border around the sprite
        uiShapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        uiShapeRenderer.color = if(active) Color.GREEN else Color.WHITE
        uiShapeRenderer.rect(sprite.getX(), sprite.getY(), sprite.getWidth(), sprite.getHeight());
        uiShapeRenderer.end();
    }
}