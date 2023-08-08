package com.tcpprintersear.app.connection

import kotlinx.coroutines.Job
import java.io.IOException
import java.net.InetAddress
import java.net.InetSocketAddress
import java.net.Socket


class TcpConnection(var address: String?,var port: Int, var timeout: Int) {
     var socket: Socket? = null
    private var disconnectJob: Job? = null

    /**
     * Check if the TCP device is connected by socket.
     *
     * @return true if is connected
     */
    fun isConnected(): Boolean {
        return socket != null && socket!!.isConnected
    }

    /**
     * Start socket connection with the TCP device.
     */
    suspend fun connect()  {
        try {
            socket = Socket()
            socket!!.connect(
                InetSocketAddress(InetAddress.getByName(address), port),
                timeout
            )

        } catch (e: Exception) {
            e.printStackTrace()
            println("enter the printer error"+e)
            //disconnect()
        }
    }

    /**
     * Close the socket connection with the TCP device.
     */
    fun disconnect(){
        if (socket != null) {
            try {
                socket!!.close()
                socket = null
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }
}