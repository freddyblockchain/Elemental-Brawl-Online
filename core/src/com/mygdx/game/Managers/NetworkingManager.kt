package com.mygdx.game.Managers

import com.mygdx.game.Action.Action
import com.mygdx.game.GameObjects.MoveableEntities.Characters.PLAYER_STATUS
import com.mygdx.game.gameState
import com.mygdx.game.playerActions
import com.mygdx.game.sessionKey
import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.net.DatagramPacket
import java.net.DatagramSocket
import java.net.InetAddress

@Serializable
data class UdpPacket(val action: Action, val sessionKey: String)

@Serializable
data class Game(val action: Action, val sessionKey: String)

@Serializable
data class GameState(val playerStates: Map<String, PlayerServerData>)

@Serializable
data class PlayerServerData(val position: Pair<Float,Float>, val status: PLAYER_STATUS, val playerNum: Int)
class NetworkingManager{
    companion object {
        val serverPort = 50000
        val clientSocket = DatagramSocket(0)
        val localPort = clientSocket.localPort  // Retrieve the automatically assigned
        val serverAddress = "http://192.168.87.147"
        val hostName = "192.168.87.147"
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
                    clientSocket.send(packet)
                    println("Packet sent to server at ${serverAddress.hostName}:$serverPort")
                }
                if(playerActions.isNotEmpty()){
                    playerActions.clear()
                }

            } catch (e: Exception) {
                clientSocket.close()
                e.printStackTrace()
            }
        }
        fun receiveGameStateFromServer() {
            println("Waiting for server: using port $localPort")
            val buffer = ByteArray(1024)  // Buffer for incoming data
            val packet = DatagramPacket(buffer, buffer.size)
            clientSocket.receive(packet)  // Receive a packet (blocking call)
            val receivedText = String(packet.data, 0, packet.length).trim()
            val packetData = Json.decodeFromString<GameState>(receivedText)

            println("Received from UDP client: $packetData")
            println(packetData.toString())
            gameState = packetData
        // Here you could add logic to process the data
        }

    }
}