package funplay.nativeapp.rps.dialogs

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import androidx.fragment.app.DialogFragment
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.gson.Gson
import com.squareup.picasso.Picasso
import funplay.nativeapp.rps.R
import funplay.nativeapp.rps.activities.InternalWebActivity
import funplay.nativeapp.rps.activities.InternalWebActivity.Companion.KEY_URL
import funplay.nativeapp.rps.data.local.AppState
import funplay.nativeapp.rps.data.models.PopupConfig
import funplay.nativeapp.rps.data.remote.RegisterResponse
import kotlinx.android.synthetic.main.dialog_popup.*
import org.jetbrains.anko.browse
import org.jetbrains.anko.intentFor
import org.koin.android.ext.android.inject

class PopupDialog : DialogFragment() {
    companion object {
        val TAG = PopupDialog::class.java.simpleName
    }

    private val remoteConfig: FirebaseRemoteConfig by inject()
    private val gson: Gson by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, android.R.style.Theme_NoTitleBar_Fullscreen)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.dialog_popup, container)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dialog?.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        setStyle(STYLE_NO_FRAME, android.R.style.Theme)

        gson.fromJson<PopupConfig>(
            remoteConfig.getString("popupConfig"),
            PopupConfig::class.java
        ).apply {
            dialogParent.setBackgroundColor(Color.parseColor(backgroundDim))
            Picasso.get().load(imageUrl).into(iv_image)
            btn_close.apply {
                visibility = if (showX) View.VISIBLE else View.GONE
                setOnClickListener {
                    this@PopupDialog.dismissAllowingStateLoss()
                }
            }
            dialogParent.setOnClickListener { this@PopupDialog.dismissAllowingStateLoss() }
            iv_image.setOnClickListener {
                val user = gson.fromJson<RegisterResponse>(
                    AppState.registerResponseString,
                    RegisterResponse::class.java
                )?.responseData
                val link = redirectUrl.replace("{TOKEN}", user?.appKey ?: "", true)
                Log.d(TAG, "link: $link")
                when (linkType) {
                    "ext" -> {
                        context?.browse(link, false)
                        if (autoDismiss) this@PopupDialog.dismiss()
                    }
                    "int" -> {
                        startActivity(context?.intentFor<InternalWebActivity>(KEY_URL to link))
                        if (autoDismiss) this@PopupDialog.dismiss()
                    }
                }
            }
        }
    }
}