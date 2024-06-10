package com.mygdx.game.ObjectProperties

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.g2d.ParticleEffect
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.math.Vector2
import com.mygdx.game.GameObjects.GameObject.GameObject
import com.mygdx.game.Rendering.Renderable

abstract class EBOParticleEffect(val particleEffect: ParticleEffect, val objectAttached: GameObject, val posModifier: Vector2 = Vector2(0f,0f)):
    Renderable {
    override fun render(batch: SpriteBatch) {
        particleEffect.emitters.forEach {it.setPosition(objectAttached.sprite.x + posModifier.x,objectAttached.sprite.y + posModifier.y) }
        particleEffect.update(Gdx.graphics.deltaTime)
        particleEffect.draw(batch)
    }
    fun start(){
        particleEffect.reset()
        particleEffect.start()
    }
}