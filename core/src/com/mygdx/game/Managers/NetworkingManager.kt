package com.mygdx.game.Managers

import com.mygdx.game.playerActions
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.net.DatagramPacket
import java.net.DatagramSocket
import java.net.InetAddress

class NetworkingManager{
    companion object {
        val serverPort = 50000
        val socket = DatagramSocket(0)
        val localPort = socket.localPort  // Retrieve the automatically assigned
        val serverAddress = "http://192.168.87.147"
        val hostName = "192.168.87.147"
        fun sendMessageToServer(){

            try {
                // Prepare a message to send
                for (action in playerActions){
                    val message = Json.encodeToString(action)
                    val buffer = message.toByteArray()

                    // Assuming the server is running on localhost and listening on port 9999
                    val serverAddress = InetAddress.getByName(hostName)

                    // Create a packet to send to the server
                    val packet = DatagramPacket(buffer, buffer.size, serverAddress, serverPort)
                    socket.send(packet)
                    println("Packet sent to server at ${serverAddress.hostName}:$serverPort")
                }
                if(playerActions.isNotEmpty()){
                    playerActions.clear()
                }

            } catch (e: Exception) {
                socket.close()
                e.printStackTrace()
            }
        }

    }
}