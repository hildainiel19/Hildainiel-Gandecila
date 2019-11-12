package funplay.nativeapp.rps.activities

import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.annotation.RequiresApi
import kotlinx.android.synthetic.main.activity_internal_web.*

class GameActivity : InternalWebActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        layout_dim.visibility = View.GONE
        layout_menu.visibility = View.VISIBLE
    }

    @RequiresApi(Build.VERSION_CODES.KITKAT)
    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)
        if (hasFocus) {
            hideSystemUI()
        }
    }

    override fun onBackPressed() {
        destroyWebview()
        super.onBackPressed()
    }

    @RequiresApi(Build.VERSION_CODES.KITKAT)
    private fun hideSystemUI() {
        window.decorView.systemUiVisibility =
            View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY or View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or View.SYSTEM_UI_FLAG_FULLSCREEN
    }

    private fun destroyWebview() {
        try {
            parentPanel.removeAllViews()
            webview.clearHistory()
            webview.clearCache(true)
            webview.loadUrl("about:blank")
            webview.onPause()
            webview.removeAllViews()
            webview.destroyDrawingCache()
            webview.destroy()
        } catch (e: Exception) {
        }
    }
}