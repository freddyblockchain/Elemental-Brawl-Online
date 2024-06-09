package com.mygdx.game.Abilities

import com.mygdx.game.Action.PlayerAction.*
import com.mygdx.game.Algorand.AlgorandManager

class AbilityManager {
    companion object {
        val fireballAbility = DefaultAbility(3f, "fireball.png", ::FireAbility)
        val icicleAbility = DefaultAbility(3f, "Icicle.png", ::IcicleAbility)
        val snowballAbility = DefaultAbility(6f, "snowball.png", ::SnowballAbility)
        val dashAbility = DefaultAbility(6f, "Dash.png", ::DashAbility)
        val abilityMap = mapOf<Long, Ability>(AlgorandManager.fireballAsa to fireballAbility,
                                             AlgorandManager.icicleAsa to icicleAbility,
                                            AlgorandManager.snowballAsa to snowballAbility,
                                            AlgorandManager.dashAsa to dashAbility)
    }
}