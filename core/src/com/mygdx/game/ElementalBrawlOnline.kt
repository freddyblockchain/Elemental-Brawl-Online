package com.mygdx.game

import FontManager
import com.badlogic.gdx.ApplicationAdapter
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.badlogic.gdx.math.Polygon
import com.badlogic.gdx.math.Vector2
import com.mygdx.game.Action.Action
import com.mygdx.game.GameModes.GameMode
import com.mygdx.game.GameModes.MainMode
import com.mygdx.game.GameObjects.MoveableEntities.Characters.Player
import com.mygdx.game.Managers.AnimationManager
import com.mygdx.game.Managers.AreaManager
import com.mygdx.game.Managers.DialogueManager
import com.mygdx.game.Managers.NetworkingManager
import com.mygdx.game.Managers.NetworkingManager.Companion.sendMessageToServer
import com.mygdx.game.ServerTraffic.Models.AuthorizationData
import com.mygdx.game.ServerTraffic.httpClient
import com.mygdx.game.Utils.RenderGraph
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.http.content.*
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

lateinit var player: Player
lateinit var currentGameMode: GameMode
lateinit var mainMode: MainMode
lateinit var sessionKey: String
val playerActions = mutableListOf<Action>()

var camera: OrthographicCamera = OrthographicCamera()
class ElementalBrawlOnline : ApplicationAdapter() {

    lateinit var inputProcessor: MyInputProcessor
    lateinit var shapeRenderer: ShapeRenderer
    override fun create() {
        initMappings()
        initAreas()
        FontManager.initFonts()

        inputProcessor = MyInputProcessor()
        Gdx.input.inputProcessor = inputProcessor
        camera = OrthographicCamera()
        camera.setToOrtho(false, Gdx.graphics.width.toFloat() / 6, Gdx.graphics.height.toFloat() / 6)
        player = Player(GameObjectData(x = 120, y = 0), Vector2(32f, 32f))
        mainMode = MainMode(inputProcessor)
        currentGameMode = mainMode
        shapeRenderer = ShapeRenderer()
        initObjects()
        DialogueManager.initSpeakableObjects()
       // getArticyDraftEntries()
        AreaManager.setActiveArea(AreaManager.areas[0].areaIdentifier)
        AreaManager.getActiveArea()!!.gameObjects.add(player)

        val authorizationData = AuthorizationData("signed message", 10, "algo address", "fwqerwqeqw", NetworkingManager.localPort)
        runBlocking {
           val response = httpClient.post("${NetworkingManager.serverAddress}:8080/authorize"){
                contentType(ContentType.Application.Json)
                setBody(Json.encodeToString(authorizationData))
            }
            sessionKey = response.bodyAsText()
        }

    }

    override fun render() {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT or GL20.GL_DEPTH_BUFFER_BIT)
        currentGameMode.spriteBatch.projectionMatrix = camera.combined
        RenderGraph.render(currentGameMode.spriteBatch)
        AnimationManager.addAnimationsToRender()
        currentGameMode.FrameAction()
        drawrects()
        camera.position.set(player.sprite.x, player.sprite.y, 0f)
        camera.update()
        sendMessageToServer()
    }

    override fun dispose() {
        currentGameMode.spriteBatch.dispose()
    }

    fun drawrects() {
        AreaManager.getActiveArea()!!.gameObjects.forEach { x -> drawPolygonShape(x.polygon, shapeRenderer) }
    }

    fun drawPolygonShape(polygon: Polygon, shapeRenderer: ShapeRenderer){
        shapeRenderer.projectionMatrix = camera.combined
        shapeRenderer.begin(ShapeRenderer.ShapeType.Line)
        shapeRenderer.polygon(polygon.transformedVertices)
        shapeRenderer.end()
    }
}