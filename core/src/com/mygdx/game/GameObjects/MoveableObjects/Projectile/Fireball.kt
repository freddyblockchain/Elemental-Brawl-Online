package com.mygdx.game.GameObjects.MoveableObjects.Projectile

import com.badlogic.gdx.math.Vector2
import com.mygdx.game.CannotMoveStrategy.RemoveObject
import com.mygdx.game.DefaultTextureHandler
import com.mygdx.game.Enums.Direction
import com.mygdx.game.Enums.Layer
import com.mygdx.game.Enums.getDirectionFromUnitVector
import com.mygdx.game.GameObjectData
import com.mygdx.game.GameObjects.GameObject.GameObject
import com.mygdx.game.GameObjects.MoveableEntities.Characters.Player


class Fireball(gameObjectData: GameObjectData, size: Vector2, unitVectorDirection: Vector2, gameObjectNumber: Int) : Projectile(gameObjectData, size, unitVectorDirection, gameObjectNumber) {
    override var currentSpeed = 0f
    override var normalSpeed = 0f
    override val cannotMoveStrategy = RemoveObject()
    override val texture = DefaultTextureHandler.getTexture("fireball.png")
    override val layer = Layer.AIR
    override var direction: Direction = getDirectionFromUnitVector(unitVectorDirection)
    override var canChangeDirection = true
    init {
        setRotation(unitVectorDirection,this,0f)
    }
}