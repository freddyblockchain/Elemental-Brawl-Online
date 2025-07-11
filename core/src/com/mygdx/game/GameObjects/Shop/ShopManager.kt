package com.mygdx.game.GameObjects.Shop

import com.badlogic.gdx.math.Vector2
import com.mygdx.game.Abilities.AbilityManager
import com.mygdx.game.Algorand.AlgorandManager
import com.mygdx.game.GameObjectData
import com.mygdx.game.Managers.AreaManager

class ShopManager {
    companion object{
        var shopItemPrices: Map<Long, Int> = mapOf()
            private set
        fun initShop(){
            shopItemPrices = AlgorandManager.getAbilityPrices()
        }
        fun initShopItems(){
            val fireball = ShopItem(GameObjectData(x = 150, y = -25), Vector2(32f,32f), AlgorandManager.fireballAsa)
            val icicle = ShopItem(GameObjectData(x = 250, y = -25), Vector2(32f,32f), AlgorandManager.icicleAsa)
            val dash = ShopItem(GameObjectData(x = 150, y = 25), Vector2(32f,32f), AlgorandManager.dashAsa)
            val snowball = ShopItem(GameObjectData(x = 250, y = 25), Vector2(32f,32f), AlgorandManager.snowballAsa)
            AreaManager.getActiveArea()!!.gameObjects.add(fireball)
            AreaManager.getActiveArea()!!.gameObjects.add(icicle)
            AreaManager.getActiveArea()!!.gameObjects.add(dash)
            AreaManager.getActiveArea()!!.gameObjects.add(snowball)
        }
    }
}