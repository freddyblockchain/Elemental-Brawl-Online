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
import com.mygdx.game.GameObjects.MoveableEntities.Characters.PlayerInitData
import com.mygdx.game.GameState.GameStateManager
import com.mygdx.game.Input.MyInputProcessor
import com.mygdx.game.Managers.*
import com.mygdx.game.Managers.NetworkingManager.Companion.receiveGameStateFromServer
import com.mygdx.game.Managers.NetworkingManager.Companion.sendMessageToServer
import com.mygdx.game.ServerTraffic.Models.AuthorizationData
import com.mygdx.game.ServerTraffic.httpClient
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
val gameStates = mutableListOf<GameState>()
var T0 = 0L
var T1 = 0L + 50L
var X0 = Vector2()
var X1 = Vector2()
var startTime = System.currentTimeMillis()
var currentPos = Vector2()

var increment = Vector2()

var testT0 = System.currentTimeMillis()
var testT1 = System.currentTimeMillis() + 50L

var testX1 = Vector2()

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
            X0 = player.initPosition
            X1 = player.initPosition
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
        //player.setPosition(getInterpolatedPosition(T0, T1 + 2000L, player.initPosition, Vector2(200f,100f)))
        //player?.setPosition(Vector2( playerData.position.first, playerData.position.second))
        val prevPos = currentPos.cpy()
        currentPos = getInterpolatedPosition(T0, T1, X0, X1, startTime)
        println("distance is: " + distance(prevPos, currentPos))
        player.setPosition(currentPos)
        if (distance(prevPos, currentPos) <= 0){
            println("X0 is " + X0)
            println("X1 is " + X1)
            println("T0 is " + T0)
            println("T1 is " + T1)
            println("startTime is " + startTime)
        }
        if(newGameState != currentGameState){
            T0 = T1
            startTime = System.currentTimeMillis()

            currentGameState = newGameState
            T1 = newGameState.gameTime
            if(T0 >= T1){
                T0 = T1 - 1
                println("Are we ever here?")
                //Handle the case where our prediction makes it so that we are actally in front of T1
            }
            //GameStateManager.executeGameStateListeners()

            val playerState = newGameState.playerStates.firstNotNullOf { it.value }
            X1 = Vector2(playerState.position.first, playerState.position.second)

            X0 = currentPos

            println("frame counter is: " + frameCounter)
            frameCounter = 0
            //Depending on frames
            increment = Vector2((X1 - X0)/3f)
        }else if (newGameState == currentGameState && currentPos.epsilonEquals(X1, 0.1f)){
            val timeBetweenLastUpdate = 16

            T0 = T1
            startTime = System.currentTimeMillis()

            X1 += increment


            X0 = currentPos
            if(frameCounter >= 4){
                println("X1 is: " + X1)
                println("X0 is: " + X0)
                println("increment is: " + increment)
            }
            T1 += timeBetweenLastUpdate
        }

        currentGameMode.spriteBatch.projectionMatrix = camera.combined
        RenderGraph.render(currentGameMode.spriteBatch)
        //AnimationManager.addAnimationsToRender()
        currentGameMode.FrameAction()
       // drawrects()
        camera.position.set(player.sprite.x, player.sprite.y, 0f)
        camera.update()
        sendMessageToServer()
        frameCounter += 1
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