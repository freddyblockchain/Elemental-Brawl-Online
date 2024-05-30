package com.mygdx.game.UI
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.g2d.Sprite
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.mygdx.game.Timer.CooldownTimer
import com.mygdx.game.UI.UIManager.Companion.uiShapeRenderer

class AbilityButton(override val sprite: Sprite, val cooldownTimer: CooldownTimer, override val onPress: () -> Unit) : UIElement {

    override var active = false
    init {
        sprite.setSize(200f, 200f)
        sprite.setPosition(Gdx.graphics.width - 250f, 100f)
    }

    fun render(UIbatch: SpriteBatch) {
        if(cooldownTimer.cooldownAvailable()){
            sprite.setAlpha(1f)
        }else{
            sprite.setAlpha(0.5f)
        }
        sprite.draw(UIbatch)
    }
    fun renderButton(){
        // Draw border around the sprite
        if(active){
            uiShapeRenderer.begin(ShapeRenderer.ShapeType.Line);
            uiShapeRenderer.color = Color.GREEN
            uiShapeRenderer.rect(sprite.getX(), sprite.getY(), sprite.getWidth(), sprite.getHeight());
            uiShapeRenderer.end();
        }
    }
}