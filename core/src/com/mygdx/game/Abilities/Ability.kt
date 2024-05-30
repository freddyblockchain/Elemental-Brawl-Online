package com.mygdx.game.Abilities

import com.badlogic.gdx.math.Vector2
import com.mygdx.game.Timer.CooldownTimer
import com.mygdx.game.UI.UIManager

abstract class Ability(val cooldown: Float) {
    var pressed = false
    protected abstract fun onActivate(targetPos: Vector2)
    val timer = CooldownTimer(cooldown)
    open fun onPress(){
        pressed = true
    }
    open fun onDeactivate(){
        pressed = false
        for (ability in UIManager.abilityButtons){
            ability.active = false
        }
    }
    fun tryActivate(targetPos: Vector2){
        if(timer.tryUseCooldown()){
            onActivate(targetPos)
            println("activating fireball!")
        }
    }

}
