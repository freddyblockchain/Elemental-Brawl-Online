package com.mygdx.game.Abilities

import com.badlogic.gdx.math.Vector2

class FireballAbility: Ability() {

    override fun onActivate(touchPoint: Vector2) {
        println("fireball shot in direction $touchPoint")
        super.onDeactivate()
    }
}