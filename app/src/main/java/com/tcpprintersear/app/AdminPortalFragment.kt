package com.tcpprintersear.app

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import androidx.appcompat.widget.Toolbar
import org.snmp4j.smi.IpAddress


class AdminPortalFragment(var ipAddress: String) : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val  view = inflater.inflate(R.layout.fragment_admin_portal, container, false)
        // Inflate the layout for this fragment
      val navigableMap =  view.findViewById<Toolbar>(R.id.back_navigation)
        navigableMap.setNavigationOnClickListener {
            parentFragmentManager.popBackStackImmediate()
        }

        var webView:WebView =  view.findViewById<WebView>(R.id.webview)
        webView.settings.javaScriptEnabled = true
        navigableMap.setOnClickListener {
            fragmentManager?.popBackStackImmediate()
        }
        navigableMap.title = ipAddress
        webView.loadUrl("http://$ipAddress")
        return view
    }


}