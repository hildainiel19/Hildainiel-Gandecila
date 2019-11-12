package funplay.nativeapp.rps.activities

import android.annotation.SuppressLint
import android.app.Activity
import android.os.Build
import android.os.Bundle
import android.util.DisplayMetrics
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.webkit.*
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import funplay.nativeapp.rps.R
import funplay.nativeapp.rps.customviews.fab.FloatingActionMenu
import funplay.nativeapp.rps.customviews.fab.SubActionButton
import funplay.nativeapp.rps.data.local.AppState
import funplay.nativeapp.rps.px
import kotlinx.android.synthetic.main.activity_internal_web.*

open class InternalWebActivity : AppCompatActivity(R.layout.activity_internal_web) {

    private lateinit var floatingActionMenu: FloatingActionMenu

    companion object {
        val TAG = InternalWebActivity::class.java.simpleName
        val KEY_URL = "url"
        val KEY_SHOW_FAB = "showFab"
        val KEY_GIFT = "gift"
        val KEY_GIFT_VISIBLE = "giftVisible"
    }

    @SuppressLint("SetJavaScriptEnabled", "JavascriptInterface", "AddJavascriptInterface")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val metrics = DisplayMetrics()
        windowManager.defaultDisplay.getMetrics(metrics)
        Log.d(TAG, "density: ${metrics.densityDpi}")
        DisplayMetrics.DENSITY_HIGH

        layout_menu?.run {
            if (intent.getBooleanExtra(KEY_SHOW_FAB, true)) {
                visibility = View.VISIBLE
                setupFab()
            } else {
                visibility = View.GONE
            }
        }

        webview.apply {
            setBackgroundColor(ContextCompat.getColor(this@InternalWebActivity, android.R.color.transparent))
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                setRendererPriorityPolicy(WebView.RENDERER_PRIORITY_IMPORTANT, false)
                isFocusable = true
                isFocusableInTouchMode = true
            }
            settings.apply {
                javaScriptEnabled = true
                pluginState = WebSettings.PluginState.ON
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                    mediaPlaybackRequiresUserGesture = false
                }
                javaScriptCanOpenWindowsAutomatically = false
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    mixedContentMode = WebSettings.MIXED_CONTENT_ALWAYS_ALLOW
                }
                builtInZoomControls = false
                loadWithOverviewMode = true
                setRenderPriority(WebSettings.RenderPriority.HIGH)
                useWideViewPort = true
                setSupportMultipleWindows(false)
                setSupportZoom(false)
                domStorageEnabled = true
                allowFileAccess = true
                allowFileAccessFromFileURLs = true
                allowUniversalAccessFromFileURLs = true
                setAppCacheEnabled(true)
                cacheMode = WebSettings.LOAD_NO_CACHE
                webViewClient = object : WebViewClient() {
                    override fun shouldOverrideUrlLoading(
                        view: WebView?,
                        request: WebResourceRequest?
                    ) = false
                }
            }
            addJavascriptInterface(object : Any() {
                @JavascriptInterface
                fun registerSuccess(stringifiedRegisterPayload: String) {
                    Log.d(TAG, "registersuccess called with payload: $stringifiedRegisterPayload")
                    AppState.registerResponseString = stringifiedRegisterPayload
                    setResult(Activity.RESULT_OK)
                    finish()
                }
            }, "Native")
        }

        if (intent.hasExtra(KEY_URL)) {
            webview.loadUrl(intent.getStringExtra(KEY_URL).also { Log.d(TAG, "url loaded: $it") })
        }

        layout_dim.visibility = View.GONE
        layout_menu.visibility = View.GONE
    }

    private fun setupFab() {
        val itemBuilder = SubActionButton.Builder(this)
        val homeButton = itemBuilder.setContentView(ImageView(this).apply {
            setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_fab_home))
        }).build().apply {
            setOnClickListener {
                floatingActionMenu.close(true)
                this@InternalWebActivity.finish()
            }
        }
        val backButton = itemBuilder.setContentView(ImageView(this).apply {
            setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_fab_back))
        }).build().apply {
            setOnClickListener {
                floatingActionMenu.close(true)
                if (webview.canGoBack()) {
                    webview.goBack()
                } else {
                    this@InternalWebActivity.finish()
                }
            }
        }

        floatingActionMenu = FloatingActionMenu.Builder(this).setStartAngle(218)
            .setEndAngle(142)
            .setRadius(46f.px)
            .addSubActionView(homeButton)
            .addSubActionView(backButton)
            .attachTo(img_arrow)
            .setStateChangeListener(object : FloatingActionMenu.MenuStateChangeListener {
                override fun onMenuOpened(menu: FloatingActionMenu?) {
                    img_glow.visibility = View.VISIBLE
                    layout_dim.visibility = View.VISIBLE
                }

                override fun onMenuClosed(menu: FloatingActionMenu?) {
                    img_glow.visibility = View.INVISIBLE
                    layout_dim.visibility = View.GONE
                }
            }).build()

        img_arrow.setOnTouchListener(object : View.OnTouchListener {
            private val CLICK_DRAG_TOLERANCE = 10f
            private var downRawX: Float = 0.toFloat()
            private var downRawY: Float = 0.toFloat()
            private var dX: Float = 0.toFloat()
            private var dY: Float = 0.toFloat()

            override fun onTouch(view: View, motionEvent: MotionEvent): Boolean {
                if (img_glow.getVisibility() == View.VISIBLE) {
                    return false
                }

                val action = motionEvent.action
                if (action == MotionEvent.ACTION_DOWN) {

                    downRawX = motionEvent.rawX
                    downRawY = motionEvent.rawY
                    dX = view.x - downRawX
                    dY = view.y - downRawY

                    return true // Consumed
                } else if (action == MotionEvent.ACTION_MOVE) {

                    val viewWidth = view.width
                    val viewHeight = view.height

                    val viewParent = view.parent as View
                    val parentWidth = viewParent.width
                    val parentHeight = viewParent.height

                    var newX = motionEvent.rawX + dX
                    newX = Math.max(
                        0f,
                        newX
                    ) // Don't allow the FAB past the left hand side of the parent
                    newX = Math.min(
                        (parentWidth - viewWidth).toFloat(),
                        newX
                    ) // Don't allow the FAB past the right hand side of the parent

                    var newY = motionEvent.rawY + dY
                    newY = Math.max(0f, newY) // Don't allow the FAB past the top of the parent
                    newY = Math.min(
                        (parentHeight - viewHeight).toFloat(),
                        newY
                    ) // Don't allow the FAB past the bottom of the parent

                    img_glow.animate()
                        .y(newY).setDuration(0).start()

                    view.animate()
                        .y(newY).setDuration(0).start()

                    return true // Consumed
                } else if (action == MotionEvent.ACTION_UP) {

                    val upRawX = motionEvent.rawX
                    val upRawY = motionEvent.rawY

                    val upDX = upRawX - downRawX
                    val upDY = upRawY - downRawY

                    if (Math.abs(upDX) < CLICK_DRAG_TOLERANCE && Math.abs(upDY) < CLICK_DRAG_TOLERANCE) { // A click
                        view.performClick()
                        return true
                    } else { // A drag
                        return true // Consumed
                    }
                } else {
                    return onTouchEvent(motionEvent)
                }
            }
        })

        img_arrow.setOnClickListener {
            if (floatingActionMenu.isOpen) {
                floatingActionMenu.close(true)
            } else {
                floatingActionMenu.open(true)
            }
        }

        layout_dim.setOnClickListener { img_arrow.performClick() }
    }
}