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
            val shopItem = ShopItem(GameObjectData(x = 200, y = 0), Vector2(32f,32f), AlgorandManager.fireballAsa)
            AreaManager.getActiveArea()!!.gameObjects.add(shopItem)
        }
    }
}