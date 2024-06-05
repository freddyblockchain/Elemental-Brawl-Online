package com.mygdx.game.Abilities

import com.mygdx.game.Algorand.AlgorandManager

class AbilityManager {
    companion object {
        val fireballAbility = FireballAbility()
        val abilityMap = mapOf<Long, Ability>(AlgorandManager.fireballAsa to fireballAbility)
        val availableAbilities = mutableListOf<Ability>(fireballAbility)
    }
}