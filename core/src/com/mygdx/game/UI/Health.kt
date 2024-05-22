package com.mygdx.game.UI

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.badlogic.gdx.math.Vector2
import com.mygdx.game.GameObjects.GameObject.FightableEntity

fun drawHealthBar(pos: Vector2, size: Vector2, fightableEntity: FightableEntity){
    val shapeRenderer = UIManager.uiShapeRenderer
    shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
    shapeRenderer.color = Color.WHITE;
    shapeRenderer.rect(pos.x, pos.y, size.x, size.y);
    shapeRenderer.end();
    shapeRenderer.begin(ShapeRenderer.ShapeType.Filled)
    shapeRenderer.color = Color.GREEN
    shapeRenderer.rect(pos.x,pos.y, fightableEntity.health / fightableEntity.maxHealth * size.x,size.y)
    shapeRenderer.end()
}