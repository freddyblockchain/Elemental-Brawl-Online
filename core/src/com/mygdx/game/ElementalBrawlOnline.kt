package com.mygdx.game

import FontManager
import com.badlogic.gdx.ApplicationAdapter
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.badlogic.gdx.math.Polygon
import com.badlogic.gdx.math.Vector2
import com.mygdx.game.Action.Action
import com.mygdx.game.GameModes.GameMode
import com.mygdx.game.GameModes.MainMode
import com.mygdx.game.GameObjects.MoveableEntities.Characters.Player
import com.mygdx.game.GameObjects.MoveableEntities.Characters.PlayerInitData
import com.mygdx.game.GameState.GameStateManager
import com.mygdx.game.Input.MyInputProcessor
import com.mygdx.game.Managers.*
import com.mygdx.game.Managers.NetworkingManager.Companion.receiveGameStateFromServer
import com.mygdx.game.Managers.NetworkingManager.Companion.sendMessageToServer
import com.mygdx.game.ServerTraffic.Models.AuthorizationData
import com.mygdx.game.ServerTraffic.httpClient
import com.mygdx.game.UI.UIManager
import com.mygdx.game.Utils.RenderGraph
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import kotlinx.coroutines.*
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

var player: Player = Player(GameObjectData(), Vector2(0f,0f),-1)
lateinit var currentGameMode: GameMode
lateinit var mainMode: MainMode
lateinit var sessionKey: String
val playerActions = mutableListOf<Action>()
val players = mutableMapOf<Int,Player>()
var currentGameState = GameState(emptyMap(), 0)
var newGameState = GameState(emptyMap(), 0)
var currentPos = Vector2()

var frameCounter = 3

lateinit var playerStartPos: Vector2

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
        mainMode = MainMode(inputProcessor)
        currentGameMode = mainMode
        shapeRenderer = ShapeRenderer()
        initObjects()
        DialogueManager.initSpeakableObjects()
       // getArticyDraftEntries()
        AreaManager.setActiveArea(AreaManager.areas[0].areaIdentifier)
        //val otherPlayer = Player(GameObjectData(x = 100, y = -100),  size = Vector2(64f,64f), 100)
       // AreaManager.getActiveArea()!!.gameObjects.add(otherPlayer)

        val authorizationData = AuthorizationData("signed message", 10, "algo address", "fwqerwqeqw", NetworkingManager.localPort)
        runBlocking {
           val response = httpClient.post("${NetworkingManager.serverAddress}:8080/authorize"){
                contentType(ContentType.Application.Json)
                setBody(Json.encodeToString(authorizationData))
            }
            // Init player
            val playerInitData = Json.decodeFromString<PlayerInitData>(response.bodyAsText())
            sessionKey = playerInitData.sessionKey
            player = Player(GameObjectData(x = 100, y = -100), Vector2(32f, 32f), playerInitData.playerNum)
            playerStartPos = player.initPosition
            players[playerInitData.playerNum] = player
            AreaManager.getActiveArea()!!.gameObjects.add(player)
        }
        val receiveInputScope = CoroutineScope(Dispatchers.IO)
        receiveInputScope.launch {
            receiveGameStateFromServer()
        }
    }

    override fun render() {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT or GL20.GL_DEPTH_BUFFER_BIT)
        val prevPos = currentPos.cpy()
        currentPos = getInterpolatedPosition(ClientStateManager.T0, ClientStateManager.T1, player.X0, player.X1, System.currentTimeMillis() - ClientStateManager.startTime)
        //println("distance is: " + distance(prevPos, currentPos))
        player.setPosition(currentPos)
        if(newGameState != currentGameState){
            currentGameState = newGameState
            ClientStateManager.serverUpdateState(currentGameState)

            //do it halfway through the frame
        }else if (newGameState == currentGameState && (System.currentTimeMillis() - ClientStateManager.startTime ) >= ((ClientStateManager.T1 - ClientStateManager.T0) - ((Gdx.graphics.deltaTime * 1000) / 2).toLong())){
            ClientStateManager.clientUpdateState()
            ClientStateManager.updateObjectFuture(player.X1 + player.increment, player)
        }

        currentGameMode.spriteBatch.projectionMatrix = camera.combined
        RenderGraph.render(currentGameMode.spriteBatch)
        //AnimationManager.addAnimationsToRender()
        currentGameMode.FrameAction()
       // drawrects()
        camera.position.set(player.sprite.x, player.sprite.y, 0f)
        camera.update()
        sendMessageToServer()
        UIManager.render()
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