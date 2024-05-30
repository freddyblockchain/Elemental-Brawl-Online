package com.mygdx.game.Abilities

class AbilityManager {
    companion object {
        val fireballAbility = FireballAbility()
        val availableAbilities = mutableListOf<Ability>(fireballAbility)
    }
}