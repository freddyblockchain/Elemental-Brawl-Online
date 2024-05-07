package com.mygdx.game.Abilities

import com.badlogic.gdx.math.Vector2

abstract class Ability {
    var pressed = false
    open fun onPress(){
        AbilityManager.availableAbilities.forEach { it.onDeactivate() }
        pressed = true
    }
    open fun onDeactivate(){
        pressed = false
    }
    abstract fun onActivate(touchPoint: Vector2)

}
