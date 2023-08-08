package com.tcpprintersear.app

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tcpprintersear.app.connection.TcpConnection
import com.tcpprintersear.app.`interface`.ItemCallback
import com.tcpprintersear.app.model.IPSearchModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class MainActivity : AppCompatActivity() {
    private val TAG = "MainActivity"

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: ItemAdapter

    private lateinit var ipScanner: IPScanner
    var items = ArrayList<IPSearchModel>()
    private val coroutineScope = CoroutineScope(Dispatchers.IO)
    private var printreip  :String =""
    private var tcpConnection : TcpConnection?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        ipScanner = IPScanner()
        recyclerView = findViewById(R.id.recycler_view)
        recyclerView.layoutManager = LinearLayoutManager(this)
        adapter = ItemAdapter(items,object :ItemCallback{
            override fun itemClick(ip: String) {
                coroutineScope.launch {
                    printreip = ip

                    ///getPrinterManufaturerName(printreip)
                    //isPrinterIdle(printreip)

                }

            }

        })
        recyclerView.adapter = adapter


    }
    override fun onStart() {
        super.onStart()
        printeripSearch()
    }

    fun  printeripSearch(){
        coroutineScope.launch {
            try {
                val activeIPs = ipScanner.scanLocalNetwork(applicationContext)
                items.clear()
                runOnUiThread {
                    adapter.updateAdapter(activeIPs)
                }

            } catch (e: Exception) {
                Log.e(TAG, "Error scanning local network", e)
            }
        }
    }
    fun ipSearch(view: View) {
        printeripSearch()
    }

    fun connect(view: View) {
        tcpConnection = TcpConnection(printreip,9100,200)
        coroutineScope.launch {
            tcpConnection?.connect()
        }
        if(tcpConnection?.isConnected() == true){
            Toast.makeText(this,"Printer Connected",Toast.LENGTH_SHORT).show()
        }else{
            Toast.makeText(this,"Printer not Connected",Toast.LENGTH_SHORT).show()
        }
    }
    fun admin(view: View) {
        supportFragmentManager.beginTransaction()
            .addToBackStack("MY_FRAGMENT_TAG")
            .add(R.id.container, AdminPortalFragment(ipAddress = printreip), "MY_FRAGMENT_TAG")
            .commit()
    }
    fun status(view: View) {
        Toast.makeText(this,"in progress",Toast.LENGTH_SHORT).show()
    }

    fun disconnect(view: View) {
        if(tcpConnection==null){
            Toast.makeText(this,"Printer not register",Toast.LENGTH_SHORT).show()
        }
        if(tcpConnection?.isConnected() == true){
            tcpConnection?.disconnect()
            Toast.makeText(this,"Printer disConnected",Toast.LENGTH_SHORT).show()
        }
    }


}