package com.mygdx.game.GameObjects.GameObject

import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.math.Vector3
import com.mygdx.game.UI.drawHealthBar
import com.mygdx.game.WINDOW_SCALE
import com.mygdx.game.camera

interface FightableEntity {

    fun showHealth(gameObject: GameObject)
    var health: Float
    val maxHealth: Float

}

interface NormalFightableEntity: FightableEntity {
    override fun showHealth(gameObject: GameObject) {
        val sprite = gameObject.sprite
        val pos = Vector3(sprite.x,sprite.y,0f)
        camera.project(pos)
        drawHealthBar(Vector2(pos.x,pos.y + (sprite.height * WINDOW_SCALE)), Vector2(sprite.width * WINDOW_SCALE,10f * (WINDOW_SCALE / 2)),this)
    }
}