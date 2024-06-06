package com.mygdx.game.GameObjects.Shop

import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.math.Vector3
import com.mygdx.game.Abilities.Ability
import com.mygdx.game.Abilities.AbilityManager
import com.mygdx.game.Collisions.AreaEntranceCollition
import com.mygdx.game.Collisions.DefaultAreaEntranceCollition
import com.mygdx.game.Collition.Collision
import com.mygdx.game.Enums.Layer
import com.mygdx.game.GameObjectData
import com.mygdx.game.GameObjects.GameObject.GameObject
import com.mygdx.game.Managers.AreaManager
import com.mygdx.game.Managers.CollitionManager
import com.mygdx.game.UI.BuyButton
import com.mygdx.game.UI.UIManager
import com.mygdx.game.player

class ShopItem(gameObjectData: GameObjectData, size: Vector2, val abilityAsa: Long) : GameObject(gameObjectData, size) {
    val ability = AbilityManager.abilityMap[abilityAsa]!!
    override val texture = ability.tooltipPicture
    val buyButton = BuyButton(this)

    init {
        UIManager.uiElements.add(buyButton)
    }

    override val layer: Layer = Layer.ONGROUND

    override val collision = ShopItemCollision(buyButton)

    override fun frameTask() {
        super.frameTask()
        //Only check collision of player
        CollitionManager.handleMoveCollisions(this, this.polygon, listOf(player))

        //Remove ability from shop if player has it
    }
}

class ShopItemCollision(private val buyButton: BuyButton): DefaultAreaEntranceCollition() {
    override fun movedInsideAction(objectEntered: GameObject) {
        if(objectEntered == player){
            buyButton.active = true
        }
    }

    override fun movedOutsideAction(objectLeaved: GameObject) {
        if(objectLeaved == player){
            buyButton.active = false
        }
    }

    override var canMoveAfterCollision = true

}