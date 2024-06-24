package com.mygdx.game.Managers

import VerificationManager
import com.badlogic.gdx.Gdx
import com.example.game.JsonConfig
import com.mygdx.game.Action.PlayerAction
import com.mygdx.game.Algorand.AlgorandManager
import com.mygdx.game.Models.SseEvent
import com.mygdx.game.Models.VerificationData
import com.mygdx.game.newGameState
import com.mygdx.game.player
import com.mygdx.game.playerActions
import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import okhttp3.sse.EventSource
import okhttp3.sse.EventSourceListener
import okhttp3.sse.EventSources.createFactory
import java.net.DatagramPacket
import java.net.DatagramSocket
import java.net.InetAddress
import java.net.InetSocketAddress


@Serializable
data class UdpPacket(val action: PlayerAction, val verificationData: VerificationData)
class NetworkingManager {
    companion object {
        val localAddress = "192.168.87.147"
        val remoteAddress = "159.65.23.143"
        val serverPort = 50000

        // prev 0
        val clientSendingSocket = DatagramSocket(49999)
        val localPort = clientSendingSocket.localPort  // Retrieve the automatically assigned

        val hostName = localAddress
        val serverAddress = "http://$hostName"

        val listeningSocket = DatagramSocket(null)

        init {
            listeningSocket.reuseAddress = true
            clientSendingSocket.reuseAddress = true
            listeningSocket.bind(InetSocketAddress(clientSendingSocket.localPort))
        }

        fun sendMessageToServer() {

            try {

                for (action in playerActions) {
                    val message =
                        JsonConfig.json.encodeToString(UdpPacket(action, VerificationManager.getVerificationData()))
                    val buffer = message.toByteArray()

                    val serverAddress = InetAddress.getByName(hostName)

                    val packet = DatagramPacket(buffer, buffer.size, serverAddress, serverPort)
                    clientSendingSocket.send(packet)
                }
                if (playerActions.isNotEmpty()) {
                    playerActions.clear()
                }

            } catch (e: Exception) {
                clientSendingSocket.close()
                e.printStackTrace()
            }
        }

        fun receiveGameStateFromServer() {
            val client = OkHttpClient()
            val request: Request = Request.Builder()
                .url(("${serverAddress}:8080/gameState/${AlgorandManager.playerAccount.address}"))
                .build()
            createFactory(client).newEventSource(request, object : EventSourceListener() {
                override fun onOpen(eventSource: EventSource, response: Response) {
                    Gdx.app.log("SSE", "Connection opened")
                }

                override fun onEvent(eventSource: EventSource, id: String?, type: String?, data: String) {
                    val sseEvent = JsonConfig.json.decodeFromString<SseEvent>(data)
                    val receivedGameState = sseEvent.data
                    if (receivedGameState.gameTime > newGameState.gameTime) {
                        newGameState = receivedGameState
                    }
                    // Handle game state update
                }

                override fun onClosed(eventSource: EventSource) {
                    Gdx.app.log("SSE", "Connection closed")
                }

                override fun onFailure(eventSource: EventSource, t: Throwable?, response: Response?) {
                    println("here")
                }
            })
        }

    }
}