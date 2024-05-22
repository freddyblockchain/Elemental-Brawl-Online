package com.mygdx.game.Managers

import com.example.game.JsonConfig
import com.mygdx.game.*
import com.mygdx.game.Action.TouchAction
import com.mygdx.game.Models.GameState
import com.mygdx.game.Models.ServerGameObject
import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.net.DatagramPacket
import java.net.DatagramSocket
import java.net.InetAddress
import java.net.InetSocketAddress

@Serializable
data class UdpPacket(val action: TouchAction, val sessionKey: String)
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
                    val message = JsonConfig.json.encodeToString(UdpPacket(action, sessionKey))
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
                val buffer = ByteArray(4096)  // Buffer for incomingF data
                val packet = DatagramPacket(buffer, buffer.size)
                listeningSocket.receive(packet)  // Receive a packet (blocking call)
                val receivedText = String(packet.data, 0, packet.length).trim()
                val packetData = JsonConfig.json.decodeFromString<GameState>(receivedText)

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