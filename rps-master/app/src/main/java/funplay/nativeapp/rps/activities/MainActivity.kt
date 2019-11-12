package funplay.nativeapp.rps.activities

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.fondesa.kpermissions.extension.listeners
import com.fondesa.kpermissions.extension.permissionsBuilder
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.gson.Gson
import com.squareup.picasso.Picasso
import funplay.nativeapp.rps.BuildConfig
import funplay.nativeapp.rps.R
import funplay.nativeapp.rps.activities.InternalWebActivity.Companion.KEY_GIFT
import funplay.nativeapp.rps.activities.InternalWebActivity.Companion.KEY_GIFT_VISIBLE
import funplay.nativeapp.rps.activities.InternalWebActivity.Companion.KEY_URL
import funplay.nativeapp.rps.data.local.AppState
import funplay.nativeapp.rps.data.models.BannerConfig
import funplay.nativeapp.rps.data.models.InjectConfig
import funplay.nativeapp.rps.data.models.PopupConfig
import funplay.nativeapp.rps.data.models.TeddyConfig
import funplay.nativeapp.rps.data.remote.ManifestService
import funplay.nativeapp.rps.data.remote.RegisterResponse
import funplay.nativeapp.rps.dialogs.PopupDialog
import funplay.nativeapp.rps.dialogs.RegisterSuccessfulDialog
import gameplay.casinomobile.teddybear.Teddy
import gameplay.casinomobile.teddybear.data.models.TeddyModule
import gameplay.casinomobile.teddybear.getDeviceID
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.item_gift_btn.*
import kotlinx.android.synthetic.main.item_gift_btn.view.*
import okhttp3.ResponseBody
import org.jetbrains.anko.browse
import org.jetbrains.anko.getStackTraceString
import org.jetbrains.anko.intentFor
import org.koin.android.ext.android.inject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*

class MainActivity : AppCompatActivity(R.layout.activity_main) {

    companion object {
        val TAG = MainActivity::class.java.simpleName
        val REQUEST_CODE_INTERNAL = 54565
        val REQUEST_CODE_GAME = 54632
    }

    private val remoteConfig: FirebaseRemoteConfig by inject()
    private val gson: Gson by inject()
    private val teddy: Teddy by inject()
    private val manifestService: ManifestService by inject()
    private var giftLink = ""
    private var giftLinkInfo: InjectConfig.PrecacheManifest.LinkInfo? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        fetchRemoteConfig()
    }

    private fun fetchRemoteConfig() {
        remoteConfig.fetchAndActivate().addOnCompleteListener {
            configureGiftLink()
            configureLinks()
            configureTeddy()
            configurePopupDialog()
            configureBanner()
        }.addOnFailureListener {
            Log.d(TAG, "fetch failed. reason: ${it.getStackTraceString()}")
        }
    }

    private fun configureGiftLink() {
        giftLinkInfo =
            gson.fromJson<InjectConfig.PrecacheManifest.LinkInfo>(remoteConfig.getString("giftLink").also {
                Log.d(
                    TAG,
                    "giftLink: $it"
                )
            }, InjectConfig.PrecacheManifest.LinkInfo::class.java)
        giftLinkInfo?.let {
            giftLink = configureButton(gift, it) ?: ""
            try {
                if (it.visible) {
                    glow.visibility = View.VISIBLE
                } else {
                    glow.visibility = View.GONE
                }
            } catch (e: Exception) {
            }
        }
        toggleRegistrationLimit()
    }

    private fun toggleRegistrationLimit() {
        if (AppState.registerResponseString.isEmpty().not()) {
            gift.visibility = View.GONE
        }
    }

    private fun configureBanner() {
        try {
            gson.fromJson<BannerConfig>(
                remoteConfig.getString("bannerConfig"),
                BannerConfig::class.java
            ).also { Log.d(TAG, "bannerConfig: $it") }.apply {
                if (enabled.also {
                        Log.d(
                            TAG,
                            "banner enabled: $it"
                        )
                    } && (start <= System.currentTimeMillis()).also {
                        Log.d(
                            TAG,
                            "start: $it"
                        )
                    } && (end >= System.currentTimeMillis()).also { Log.d(TAG, "end: $it") }) {
                    Log.d(TAG, "banner should be visible.")
                    banner.visibility = View.VISIBLE
                    Picasso.get().load(imageUrl).into(banner)
                    banner.setOnClickListener {
                        when (linkType) {
                            "int" -> startActivityForResult(
                                intentFor<InternalWebActivity>(KEY_URL to redirectUrl),
                                REQUEST_CODE_INTERNAL
                            )
                            "ext" -> browse(redirectUrl, false)
                        }
                    }
                } else {
                    Log.d(TAG, "banner should be gone")
                    banner.visibility = View.GONE
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
            Log.d(TAG, "an error occured")
        }
    }

    fun configurePopupDialog() {
        try {
            gson.fromJson<PopupConfig>(
                remoteConfig.getString("popupConfig"),
                PopupConfig::class.java
            ).also { Log.d(TAG, "popupConfig: $it currenttime: ${System.currentTimeMillis()}") }
                .apply {
                    if (enabled.also {
                            Log.d(
                                TAG,
                                "enabled: $it"
                            )
                        } && (start <= System.currentTimeMillis()).also {
                            Log.d(
                                TAG,
                                "start: $it"
                            )
                        } && (end >= System.currentTimeMillis()).also {
                            Log.d(
                                TAG,
                                "end: $it"
                            )
                        } && (System.currentTimeMillis() - AppState.lastPopupShow >= delay).also {
                            Log.d(
                                TAG,
                                "delay: $it"
                            )
                        }) {
                        val popupDialog = PopupDialog()
                        popupDialog.show(supportFragmentManager, "popup")
                        AppState.lastPopupShow = System.currentTimeMillis()
                    }
                }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun configureTeddy() {
        val teddyConfig = gson.fromJson<TeddyConfig>(remoteConfig.getString("teddyConfig").also {
            Log.d(
                TAG,
                "teddyConfig: $it"
            )
        }, TeddyConfig::class.java)
        if (teddyConfig.useTeddy) {
            permissionsBuilder(
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE
            ).build().apply {
                listeners {
                    onAccepted { runTeddy(teddyConfig) }
                }
                send()
            }
        }
    }

    private fun configureLinks() {
        val linkInfos = gson.fromJson<Array<InjectConfig.PrecacheManifest.LinkInfo>>(
            remoteConfig.getString("linkInfos").also { Log.d(TAG, "linkInfos: $it") },
            Array<InjectConfig.PrecacheManifest.LinkInfo>::class.java
        )

        val includes =
            arrayOf(item_ranking, item_promotions, item_playforfun, item_howtoplay, item_facebook)
        includes.forEachIndexed { index, view ->
            configureButton(view, linkInfos.get(index))
        }
    }

    private fun configureButton(
        view: View,
        linkInfo: InjectConfig.PrecacheManifest.LinkInfo
    ): String? {
        if (linkInfo.visible) {
            view.visibility = View.VISIBLE
            Picasso.get().load(linkInfo.imageUrl).into(view.imageBtn)
            view.text.text = when (linkInfo.displayText) {
                "RANKING" -> getString(R.string.ranking)
                "PROMOTIONS" -> getString(R.string.promotions)
                "PLAY FOR FUN" -> getString(R.string.play_for_fun)
                "HOW TO PLAY" -> getString(R.string.how_to_play)
                "FACEBOOK" -> getString(R.string.fb)
                "OPEN TO GET GIFT" -> getString(R.string.open_to_get_gift)
                else -> linkInfo.displayText
            }
            view.setOnClickListener {
                when (linkInfo.linkType) {
                    "ext" -> {
                        var linkUrl = linkInfo.linkUrl
                        try {
                            val user = gson.fromJson<RegisterResponse>(
                                AppState.registerResponseString,
                                RegisterResponse::class.java
                            ).responseData
                            linkUrl = linkUrl?.replace("{TOKEN}", user?.token ?: "", true)
                        } catch (e: Exception) {
                        }
                        linkUrl?.let { browse(it, false) }
                    }
                    "int" -> startActivityForResult(
                        intentFor<InternalWebActivity>(KEY_URL to linkInfo.linkUrl),
                        REQUEST_CODE_INTERNAL
                    )
                    "game" -> {
                        val giftVisible =
                            giftLinkInfo?.visible ?: false && AppState.registerResponseString.isEmpty()
                        startActivityForResult(
                            intentFor<GameActivity>(
                                KEY_URL to linkInfo.linkUrl, KEY_GIFT_VISIBLE to giftVisible
                            ), REQUEST_CODE_GAME
                        )
                    }
                    else -> {
                        Log.d(TAG, "Link type ${linkInfo.linkType} not supported")
                    }
                }
            }
        } else {
            view.visibility = View.GONE
        }
        return linkInfo.linkUrl
    }

    private fun runTeddy(teddyConfig: TeddyConfig) {
        if (teddyConfig.useTeddy.also { Log.d(TAG, "useTeddy: $it") }) {
            Log.d(TAG, "calling teddy init")
            teddy.init(
                application = application,
                baseUrlOverride = teddyConfig.domain,
                customOperatorPath = teddyConfig.path,
                buildVersion = BuildConfig.VERSION_NAME,
                jwtToken = ""
            )
            Log.d(TAG, "calling teddy start")
            teddy.start(
                teddyModules = listOf(
                    TeddyModule(
                        module = "apps",
                        enabled = true,
                        askPermission = false,
                        onPermissionRequired = { _, _ ->
                        })
                ), activity = this@MainActivity
            )

            var ipAddress = ""
            Log.d(TAG, "calling teddy getting ip")
            Handler().postDelayed({
                manifestService.getIPAddress().enqueue(object : Callback<ResponseBody> {
                    override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                        Log.d(TAG, "ip fail, logging in.")
                        teddyLogin(ipAddress)
                    }

                    override fun onResponse(
                        call: Call<ResponseBody>,
                        response: Response<ResponseBody>
                    ) {
                        if (response.isSuccessful) {
                            Log.d(TAG, "ip success, logging in.")
                            ipAddress = response.body()?.string() ?: ""
                        } else {
                            Log.d(TAG, "ip fail, logging in.")
                        }
                        teddyLogin(ipAddress)
                    }
                })
            }, 10000)
        }
    }

    override fun onResume() {
        toggleRegistrationLimit()
        super.onResume()
    }

    private fun teddyLogin(ipAddress: String) {
        teddy.userLoggedIn(
            language = Locale.getDefault().getDisplayLanguage(),
            productToken = "",
            username = getDeviceID(),
            ip = ipAddress,
            rGroup = "",
            memberId = 0L,
            currency = ""
        )
    }

    fun displayRegisterSuccessfulDialog() {
        val registerSuccessfulDialog = RegisterSuccessfulDialog()
        registerSuccessfulDialog.show(supportFragmentManager, "registerSuccessful")
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        Log.d(TAG, "request code: $requestCode result code: $resultCode")
        when (requestCode) {
            REQUEST_CODE_INTERNAL -> {
                if (resultCode == Activity.RESULT_OK) {
                    displayRegisterSuccessfulDialog()
                }
            }
            REQUEST_CODE_GAME -> {
                if (resultCode == Activity.RESULT_OK) {
                    if (data?.getBooleanExtra(KEY_GIFT, false) == true && giftLink.isNotBlank()) {
                        Log.d(TAG, "has gift")
                        startActivityForResult(
                            intentFor<InternalWebActivity>(KEY_URL to giftLink),
                            REQUEST_CODE_INTERNAL
                        )
                    } else {
                        Log.d(TAG, "no gift")
                    }
                }
            }
        }
    }
}
