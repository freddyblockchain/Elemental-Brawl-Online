package com.mygdx.game.GameObjects.Shop

import com.mygdx.game.Abilities.Ability

class InventoryManager {
    companion object {
        var gold: Int = 0
        var abilityList = mutableListOf<Ability>()
    }
}