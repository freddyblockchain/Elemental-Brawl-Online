package com.mygdx.game

import FontManager
import com.algorand.algosdk.account.Account
import com.badlogic.gdx.ApplicationAdapter
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.badlogic.gdx.math.Polygon
import com.badlogic.gdx.math.Vector2
import com.mygdx.game.Abilities.AbilityManager
import com.mygdx.game.Action.TouchAction
import com.mygdx.game.Algorand.AlgorandManager
import com.mygdx.game.Algorand.EBOSecurePreferences
import com.mygdx.game.GameModes.GameMode
import com.mygdx.game.GameModes.MainMode
import com.mygdx.game.GameObjects.GameObject.MoveableObject
import com.mygdx.game.GameObjects.MoveableEntities.Characters.Player
import com.mygdx.game.GameObjects.MoveableEntities.Characters.PlayerInitData
import com.mygdx.game.GameObjects.Shop.ShopItem
import com.mygdx.game.Input.MyInputProcessor
import com.mygdx.game.Managers.*
import com.mygdx.game.Managers.NetworkingManager.Companion.receiveGameStateFromServer
import com.mygdx.game.Managers.NetworkingManager.Companion.sendMessageToServer
import com.mygdx.game.Models.GameState
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

var player: Player = Player(GameObjectData(), Vector2(0f, 0f), -1)
lateinit var currentGameMode: GameMode
lateinit var mainMode: MainMode
lateinit var sessionKey: String
val playerActions = mutableListOf<TouchAction>()
val players = mutableMapOf<Int, Player>()
var currentGameState = GameState(mutableListOf(), 0)
var newGameState = GameState(mutableListOf(), 0)
var currentPos = Vector2()

val WINDOW_SCALE = 5

var frameCounter = 3

lateinit var playerStartPos: Vector2

var camera: OrthographicCamera = OrthographicCamera()

class ElementalBrawlOnline(val securePreferences: EBOSecurePreferences) : ApplicationAdapter() {

    lateinit var inputProcessor: MyInputProcessor
    lateinit var shapeRenderer: ShapeRenderer
    override fun create() {
        initMappings()
        initAreas()
        FontManager.initFonts()
        Gdx.gl.glLineWidth(5f)

        inputProcessor = MyInputProcessor()
        Gdx.input.inputProcessor = inputProcessor
        camera = OrthographicCamera()
        camera.setToOrtho(
            false,
            Gdx.graphics.width.toFloat() / WINDOW_SCALE,
            Gdx.graphics.height.toFloat() / WINDOW_SCALE
        )
        mainMode = MainMode(inputProcessor)
        currentGameMode = mainMode
        shapeRenderer = ShapeRenderer()
        initObjects()
        DialogueManager.initSpeakableObjects()
        // getArticyDraftEntries()
        AreaManager.setActiveArea(AreaManager.areas[0].areaIdentifier)
        //val otherPlayer = Player(GameObjectData(x = 100, y = -100),  size = Vector2(64f,64f), 100)
        // AreaManager.getActiveArea()!!.gameObjects.add(otherPlayer)

        runBlocking {
            //get address
            val storedAddress = securePreferences.getString("EBOAccount", "")
            var newPlayer = false
            if(storedAddress == ""){
                val acct: Account = Account()
                newPlayer = true
                securePreferences.putString("EBOAccount", acct.toMnemonic())
            }
            val myValue = securePreferences.getString("EBOAccount", "")
            val account = Account(myValue)
            println("Address is: " + account.address.toString())
            val authorizationData =
                AuthorizationData("signed message", 10, account.address.toString(), "fwqerwqeqw", NetworkingManager.localPort)
            val response = httpClient.post("${NetworkingManager.serverAddress}:8080/authorize") {
                contentType(ContentType.Application.Json)
                setBody(Json.encodeToString(authorizationData))
            }

            //Give players one gold to be able to buy the fireball in the game.
            if(newPlayer){
                AlgorandManager.optIntoGameAssets()
                launch {
                    delay(5000)
                    httpClient.post("${NetworkingManager.serverAddress}:8080/request-starting-gold")
                }
            }

            // Init player
            val playerInitData = Json.decodeFromString<PlayerInitData>(response.bodyAsText())
            sessionKey = playerInitData.sessionKey
            player = Player(GameObjectData(x = 100, y = -100), Vector2(32f, 32f), playerInitData.playerNum)
            playerStartPos = player.initPosition
            players[playerInitData.playerNum] = player
            AreaManager.getActiveArea()!!.gameObjects.add(player)

            val shopItem = ShopItem(GameObjectData(x = 200, y = 0), Vector2(32f,32f), AbilityManager.fireballAbility)
            AreaManager.getActiveArea()!!.gameObjects.add(shopItem)
        }
        receiveGameStateFromServer()
    }

    override fun render() {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT or GL20.GL_DEPTH_BUFFER_BIT)
        val moveableObjects = AreaManager.getActiveArea()!!.gameObjects.filterIsInstance<MoveableObject>()
        moveableObjects.forEach {
            currentPos = getInterpolatedPosition(
                ClientStateManager.T0,
                ClientStateManager.T1,
                it.X0,
                it.X1,
                System.currentTimeMillis() - ClientStateManager.startTime
            )
            it.setPosition(currentPos)
        }
        val playerNew = player.currentPosition()
        if (newGameState != currentGameState) {
            currentGameState = newGameState
            ClientStateManager.serverUpdateState(currentGameState)

            //do it halfway through the frame
        } else if (newGameState == currentGameState && (System.currentTimeMillis() - ClientStateManager.startTime) >= ((ClientStateManager.T1 - ClientStateManager.T0) - ((Gdx.graphics.deltaTime * 1000) / 2).toLong())) {
            ClientStateManager.clientUpdateState()
            moveableObjects.forEach {
                ClientStateManager.updateObjectFuture(it.X1 + it.increment, it)
            }
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

    fun drawPolygonShape(polygon: Polygon, shapeRenderer: ShapeRenderer) {
        shapeRenderer.projectionMatrix = camera.combined
        shapeRenderer.begin(ShapeRenderer.ShapeType.Line)
        shapeRenderer.polygon(polygon.transformedVertices)
        shapeRenderer.end()
    }
}