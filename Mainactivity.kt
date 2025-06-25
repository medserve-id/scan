package com.example.qrcetak

import android.annotation.SuppressLint
import android.os.Bundle
import android.webkit.*
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    lateinit var bluetoothService: BluetoothService

    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bluetoothService = BluetoothService(this)

        val webView = WebView(this)
        webView.settings.javaScriptEnabled = true
        webView.settings.allowFileAccess = true
        webView.settings.mediaPlaybackRequiresUserGesture = false
        webView.webChromeClient = WebChromeClient()
        webView.webViewClient = WebViewClient()

        webView.addJavascriptInterface(object {
            @JavascriptInterface
            fun onQrScanned(data: String) {
                bluetoothService.printText(data)
            }
        }, "AndroidBridge")

        webView.loadUrl("file:///android_asset/index.html")
        setContentView(webView)
    }
}
