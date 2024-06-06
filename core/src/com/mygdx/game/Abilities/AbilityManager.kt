package com.mygdx.game.Abilities

import com.badlogic.gdx.math.Vector2
import com.mygdx.game.Action.PlayerAction
import com.mygdx.game.Action.PlayerAction.FireAbility
import com.mygdx.game.Action.PlayerAction.IcicleAbility
import com.mygdx.game.Algorand.AlgorandManager

class AbilityManager {
    companion object {
        val fireballAbility = ProjectileAbility(5f, "fireball.png", ::FireAbility)
        val icicleAbility = ProjectileAbility(3f, "Icicle.png", ::IcicleAbility)
        val abilityMap = mapOf<Long, Ability>(AlgorandManager.fireballAsa to fireballAbility,
                                             AlgorandManager.icicleAsa to icicleAbility)
    }
}