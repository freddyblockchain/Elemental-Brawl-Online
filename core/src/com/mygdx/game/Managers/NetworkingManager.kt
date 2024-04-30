package com.mygdx.game.Managers

import com.mygdx.game.*
import com.mygdx.game.Action.Action
import com.mygdx.game.GameObjects.MoveableEntities.Characters.PLAYER_STATUS
import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.net.DatagramPacket
import java.net.DatagramSocket
import java.net.InetAddress
import java.net.InetSocketAddress

@Serializable
data class UdpPacket(val action: Action, val sessionKey: String)

@Serializable
data class Game(val action: Action, val sessionKey: String)

@Serializable
data class GameState(val playerStates: Map<String, PlayerServerData>, val gameTime: Long)

@Serializable
data class PlayerServerData(val position: Pair<Float,Float>, val status: PLAYER_STATUS, val playerNum: Int, val speed: Float, val unitVectorDirection: Pair<Float,Float>)
class NetworkingManager{
    companion object {
        val serverPort = 50000
        val clientSendingSocket = DatagramSocket(0)
        val localPort = clientSendingSocket.localPort  // Retrieve the automatically assigned
        val serverAddress = "http://192.168.87.147"
        val hostName = "192.168.87.147"

        val listeningSocket = DatagramSocket(null)

        init {
            listeningSocket.reuseAddress = true
            clientSendingSocket.reuseAddress = true
            listeningSocket.bind(InetSocketAddress(clientSendingSocket.localPort))
        }

        fun sendMessageToServer(){

            try {

                // Prepare a message to send
                for (action in playerActions){
                    val message = Json.encodeToString(UdpPacket(action, sessionKey))
                    val buffer = message.toByteArray()

                    // Assuming the server is running on localhost and listening on port 9999
                    val serverAddress = InetAddress.getByName(hostName)

                    // Create a packet to send to the server
                    val packet = DatagramPacket(buffer, buffer.size, serverAddress, serverPort)
                    clientSendingSocket.send(packet)
                }
                if(playerActions.isNotEmpty()){
                    playerActions.clear()
                }

            } catch (e: Exception) {
                clientSendingSocket.close()
                e.printStackTrace()
            }
        }
        fun receiveGameStateFromServer() {
            while(true) {
                val buffer = ByteArray(1024)  // Buffer for incomingF data
                val packet = DatagramPacket(buffer, buffer.size)
                listeningSocket.receive(packet)  // Receive a packet (blocking call)
                val receivedText = String(packet.data, 0, packet.length).trim()
                val packetData = Json.decodeFromString<GameState>(receivedText)

                if(packetData.gameTime > newGameState.gameTime){
                    newGameState = packetData
                }
               //println("current time is: " + currentTime + "Time s: " + newGameState.gameTime)
               //gameStates.sortBy { it.gameStateNum }
            }
        // Here you could add logic to process the data
        }

    }
}