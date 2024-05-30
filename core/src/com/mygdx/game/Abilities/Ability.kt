package com.mygdx.game.Abilities

import com.badlogic.gdx.math.Vector2
import com.mygdx.game.UI.UIManager

abstract class Ability {
    var pressed = false
    open fun onPress(){
        pressed = true
    }
    open fun onDeactivate(){
        pressed = false
        for (ability in UIManager.abilityButtons){
            ability.active = false
        }
    }
    abstract fun onActivate(touchPoint: Vector2)

}
