package com.mygdx.game.Input

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.badlogic.gdx.InputProcessor
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.math.Vector3
import com.mygdx.game.*
import com.mygdx.game.Abilities.AbilityManager
import com.mygdx.game.Action.PlayerAction
import com.mygdx.game.Enums.Direction
import com.mygdx.game.Enums.getDirectionUnitVector
import com.mygdx.game.UI.UIManager


class MyInputProcessor : InputProcessor {
    override fun keyDown(keycode: Int): Boolean {
        return false
    }

    fun handleInput() {
        var directionUnitVector = Vector2(0f, 0f)
        if (Gdx.input.isKeyPressed(Input.Keys.A)) {
            directionUnitVector = getDirectionUnitVector(Direction.LEFT)
        }
        if (Gdx.input.isKeyPressed(Input.Keys.D)) {
            directionUnitVector = getDirectionUnitVector(Direction.RIGHT)
        }
        if (Gdx.input.isKeyPressed(Input.Keys.W)) {
            directionUnitVector = getDirectionUnitVector(Direction.UP)
        }
        if (Gdx.input.isKeyPressed(Input.Keys.S)) {
            directionUnitVector = getDirectionUnitVector(Direction.DOWN)
        }
        if (directionUnitVector != Vector2(0f, 0f)) {
            player.move(directionUnitVector)
            player.setRotation(directionUnitVector, player, 90f)
        }
    }

    override fun keyUp(keycode: Int): Boolean {
        return false
    }

    override fun keyTyped(character: Char): Boolean {
        return false
    }

    override fun touchDown(x: Int, y: Int, pointer: Int, button: Int): Boolean {
        // val point =
        var uiActionHappened = false
        var abilityActivated = false
        val touchPoint = Vector2(
            x.toFloat(),
            Gdx.graphics.height - y.toFloat()
        )
        val worldCoords = camera.unproject(Vector3(x.toFloat(), y.toFloat(), 0f))
        val abilities = AbilityManager.abilityMap.values
        if (abilities.any { it.pressed }) {
            val activeAbility = abilities.first { it.pressed }
            activeAbility.tryActivate(Vector2(worldCoords.x, worldCoords.y))
            abilityActivated = true
        } else {
            for (uiElement in UIManager.uiElements.toMutableList()) {
                if (uiElement.sprite.boundingRectangle.contains(
                        touchPoint
                    )
                ) {
                    uiActionHappened = uiActionHappened || uiElement.onPress()
                }
            }
        }

        if (!uiActionHappened && !abilityActivated) {
            val touchAction = PlayerAction.Move(Pair(worldCoords.x, worldCoords.y))
            playerActions.add(touchAction)
        }

        //player.move(toGo)
        return false
    }

    override fun touchUp(x: Int, y: Int, pointer: Int, button: Int): Boolean {
        return false
    }

    override fun touchCancelled(p0: Int, p1: Int, p2: Int, p3: Int): Boolean {
        return false
    }

    override fun touchDragged(x: Int, y: Int, pointer: Int): Boolean {
        return false
    }

    override fun mouseMoved(x: Int, y: Int): Boolean {
        return false
    }

    override fun scrolled(amountX: Float, amountY: Float): Boolean {
        return false
    }
}