class MainActivity : AppCompatActivity() {

    lateinit var webView: WebView
    lateinit var bluetoothService: BluetoothService

    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bluetoothService = BluetoothService(this)

        webView = WebView(this)
        webView.settings.javaScriptEnabled = true
        webView.addJavascriptInterface(JSBridge(), "AndroidBridge")
        webView.loadUrl("file:///android_asset/index.html")

        setContentView(webView)
    }

    inner class JSBridge {
        @JavascriptInterface
        fun onQrScanned(data: String) {
            runOnUiThread {
                bluetoothService.printText(data)
            }
        }
    }
}

