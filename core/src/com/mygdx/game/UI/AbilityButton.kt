package com.mygdx.game.UI
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.g2d.Sprite
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.badlogic.gdx.math.Vector2
import com.mygdx.game.Abilities.Ability
import com.mygdx.game.Abilities.AbilityManager
import com.mygdx.game.UI.UIManager.Companion.uiShapeRenderer

class AbilityButton(val ability: Ability, val abilityOnPress: () -> Unit) : UIElement {
    val cooldownTimer = ability.timer
    override val sprite = Sprite(ability.tooltipPicture)
    override var active = false
    init {
        sprite.setSize(200f, 200f)

        val position = when(ability){
            AbilityManager.fireballAbility -> Vector2(Gdx.graphics.width - 250f, 200f)
            AbilityManager.icicleAbility -> Vector2( 250f, 200f)
            else -> Vector2(0f,0f)
        }
        sprite.setPosition(position.x, position.y)
    }

    override fun render(UIbatch: SpriteBatch) {
        if(cooldownTimer.cooldownAvailable()){
            sprite.setAlpha(1f)
        }else{
            sprite.setAlpha(0.5f)
        }
        sprite.draw(UIbatch)
    }

    override fun onPress(): Boolean {
        if(this.cooldownTimer.cooldownAvailable()){
            println("pressed!")
            this.active = true
            abilityOnPress()
            return true
        }
        return false
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