package com.mygdx.game.Abilities
import com.badlogic.gdx.math.Vector2
import com.mygdx.game.Action.PlayerAction
import com.mygdx.game.DefaultTextureHandler
import com.mygdx.game.playerActions

class ProjectileAbility(cooldown: Float, textureImg: String,val playerActionConstructor: (pos: Pair<Float,Float>) -> PlayerAction): Ability(cooldown){
    override val tooltipPicture = DefaultTextureHandler.getTexture(textureImg)

    override fun onActivate(targetPos: Vector2) {
        playerActions.add(playerActionConstructor(Pair(targetPos.x, targetPos.y)))
        super.onDeactivate()
    }

}