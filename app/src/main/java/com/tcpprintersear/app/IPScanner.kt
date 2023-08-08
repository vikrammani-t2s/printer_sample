package com.tcpprintersear.app

import android.content.Context
import android.net.wifi.WifiManager
import android.util.Log
import com.tcpprintersear.app.model.IPSearchModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.net.InetSocketAddress
import java.net.Socket


class IPScanner {
     val printerName ="PRINTER"

    suspend fun scanLocalNetwork(context: Context): List<IPSearchModel> =
        withContext(Dispatchers.IO) {
            val localIPAddress = getLocalIpAddress(context)
            val subnet = getSubnet(localIPAddress)
            if (subnet.isNullOrEmpty()) {
                return@withContext emptyList()
            }

            val maxConcurrentChecks = 10 // Limit the number of concurrent checks
            val maxRetries = 3 // Set the maximum number of retries

            // Create a coroutine channel to receive the reachability check results
            val resultChannel = Channel<IPSearchModel>()

            // Launch a coroutine to handle the reachability checks
            CoroutineScope(Dispatchers.IO).launch {
                for (i in 1..255) {
                    val ip = "$subnet$i"

                    // Launch a coroutine for each IP address
                    launch {
                        var retryCount = 0

                        while (retryCount <= maxRetries) {
                            if (isHostReachable(ip)) {
                                resultChannel.send(IPSearchModel(ip, getMacAddress(ip)))
                                break // Printer successfully reached, break out of the retry loop
                            }
                            retryCount++
                        }
                    }

                    // Limit the number of concurrent checks to avoid overwhelming the system
                    if (i % maxConcurrentChecks == 0) {
                        delay(100) // A short delay before starting the next batch
                    }
                }
                resultChannel.close() // Close the channel when all checks are done
            }
            // Receive the results from the channel and collect them into a list
            val uniquePrinters = mutableListOf<IPSearchModel>()
            for (result in resultChannel) {
                uniquePrinters.add(result)
            }

            uniquePrinters.toList()
        }

    fun getLocalIpAddress(context: Context): String? {
        try {
            // Retrieve the WiFi service of the device
            val wifiManager = context.applicationContext
                .getSystemService(Context.WIFI_SERVICE) as WifiManager
            // Get the IP address from the WiFi connection
            val ipAddress = wifiManager.connectionInfo.ipAddress
            return String.format(
                "%d.%d.%d.%d",
                ipAddress and 0xff,
                ipAddress shr 8 and 0xff,
                ipAddress shr 16 and 0xff,
                ipAddress shr 24 and 0xff
            )
        } catch (e: Exception) {
            e.message
        }
        return null
    }


    private fun getSubnet(ipAddress: String?): String? {
        val parts = ipAddress?.split(".")
        return if (parts?.size == 4) {
            "${parts[0]}.${parts[1]}.${parts[2]}."
        } else {
            null
        }
    }

    suspend fun getMacAddress(ipAddress: String): String = withContext(Dispatchers.IO) {
        try {
            val process = Runtime.getRuntime().exec("ip neigh")
            val reader = BufferedReader(InputStreamReader(process.inputStream))
            var line: String?

            while (reader.readLine().also { line = it } != null) {
                val splitLine = line!!.split("\\s+".toRegex())
                if (ipAddress == splitLine[0] && splitLine.size >= 5) {
                    return@withContext splitLine[4]
                }
            }
            return@withContext ""
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return@withContext ""
    }

    private suspend fun isHostReachable(ipAddress: String): Boolean = withContext(Dispatchers.IO) {
        return@withContext try {
            val printerPort = 9100 // Default printer port
            val socket = Socket()
            socket.connect(
                InetSocketAddress(ipAddress, printerPort),
                200
            ) // Adjust the timeout as needed
            socket.close()
            true
        } catch (e: IOException) {
            e.message
            false
        }
    }


}
