package com.mygdx.game.Particles

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.g2d.ParticleEffect
import com.badlogic.gdx.math.Vector2
import com.mygdx.game.Enums.Layer
import com.mygdx.game.GameObjects.GameObject.GameObject
import com.mygdx.game.ObjectProperties.EBOParticleEffect

class FireDashEffect(objectAttached: GameObject, posModifier: Vector2 = Vector2(objectAttached.sprite.width/2, objectAttached.sprite.height/2)): EBOParticleEffect(
    ParticleEffect(), objectAttached, posModifier) {
    override val layer = Layer.FOREGROUND

    init {
        particleEffect.load(Gdx.files.internal("ParticleEmitters/Dash.p"), Gdx.files.internal(""))
    }
}