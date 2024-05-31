package com.mygdx.game.UI
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.g2d.Sprite
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.mygdx.game.Abilities.Ability
import com.mygdx.game.Timer.CooldownTimer
import com.mygdx.game.UI.UIManager.Companion.uiShapeRenderer

class AbilityButton(val ability: Ability, override val onPress: () -> Unit) : UIElement {
    val cooldownTimer = ability.timer
    val sprite = Sprite(ability.tooltipPicture)
    override var active = false
    init {
        sprite.setSize(200f, 200f)
        sprite.setPosition(Gdx.graphics.width - 250f, 100f)
    }

    override fun render(UIbatch: SpriteBatch) {
        if(cooldownTimer.cooldownAvailable()){
            sprite.setAlpha(1f)
        }else{
            sprite.setAlpha(0.5f)
        }
        sprite.draw(UIbatch)
    }

    override fun renderShape() {
        if(active){
            uiShapeRenderer.begin(ShapeRenderer.ShapeType.Line);
            uiShapeRenderer.color = Color.GREEN
            uiShapeRenderer.rect(sprite.getX(), sprite.getY(), sprite.getWidth(), sprite.getHeight());
            uiShapeRenderer.end();
        }
    }

}