package com.mygdx.game.Abilities

import com.badlogic.gdx.math.Vector2
import com.mygdx.game.Action.TouchAction
import com.mygdx.game.playerActions

class FireballAbility: Ability() {

    override fun onActivate(touchPoint: Vector2) {
        println("fireball shot in direction $touchPoint")
        playerActions.add(TouchAction.FireAbility(Pair(touchPoint.x, touchPoint.y)))
        super.onDeactivate()
    }
}