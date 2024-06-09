package com.mygdx.game.GameObjects.MoveableObjects.Projectile

import com.badlogic.gdx.math.Vector2
import com.mygdx.game.CannotMoveStrategy.RemoveObject
import com.mygdx.game.DefaultTextureHandler
import com.mygdx.game.Enums.Direction
import com.mygdx.game.Enums.Layer
import com.mygdx.game.Enums.getDirectionFromUnitVector
import com.mygdx.game.GameObjectData

class Icicle(gameObjectData: GameObjectData, size: Vector2, unitVectorDirection: Vector2, gameObjectNumber: Int) : Projectile(gameObjectData, size, unitVectorDirection, gameObjectNumber)  {
    override val texture = DefaultTextureHandler.getTexture("Icicle.png")
    override var normalSpeed = 8f
    override var currentSpeed = normalSpeed
    override val cannotMoveStrategy = RemoveObject()
    override var direction: Direction = getDirectionFromUnitVector(unitVectorDirection)
    override var canChangeDirection = true
    override val layer = Layer.AIR

    init {
        setRotation(unitVectorDirection,this,0f)
    }
}