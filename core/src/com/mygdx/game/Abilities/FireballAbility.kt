package com.mygdx.game.Abilities

import com.badlogic.gdx.math.Vector2
import com.mygdx.game.Action.PlayerAction
import com.mygdx.game.DefaultTextureHandler
import com.mygdx.game.playerActions

class FireballAbility: Ability(5f) {
    override val price = 1
    override val tooltipPicture = DefaultTextureHandler.getTexture("fireball.png")
    override fun onActivate(touchPoint: Vector2) {
        playerActions.add(PlayerAction.FireAbility(Pair(touchPoint.x, touchPoint.y)))
        super.onDeactivate()
    }
}