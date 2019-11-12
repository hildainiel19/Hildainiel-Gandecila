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
import funplay.nativeapp.rps.R
import funplay.nativeapp.rps.data.local.AppState
import funplay.nativeapp.rps.data.remote.RegisterResponse
import kotlinx.android.synthetic.main.dialog_register_successful.*
import org.jetbrains.anko.browse
import org.koin.android.ext.android.inject

class RegisterSuccessfulDialog : DialogFragment() {

    companion object {
        val TAG = RegisterSuccessfulDialog::class.java.simpleName
    }

    private val remoteConfig: FirebaseRemoteConfig by inject()
    private val gson: Gson by inject()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.dialog_register_successful, container)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dialog?.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        setStyle(STYLE_NO_FRAME, android.R.style.Theme)

        tvRegisterSuccessful.text =
            if (remoteConfig.getString("registerSuccessfulText").isBlank()) getString(R.string.register_success) else remoteConfig.getString(
                "registerSuccessfulText"
            ).replace("\\n", "\n")
        btn_back.setOnClickListener { dismiss() }
        btn_playnow.setOnClickListener {
            val user = gson.fromJson<RegisterResponse>(
                AppState.registerResponseString,
                RegisterResponse::class.java
            ).responseData
            Log.d(TAG, "user: $user")
            context?.browse(
                remoteConfig.getString("registerSuccessfulRedirect")
                    .replace("{TOKEN}", user?.appKey ?: "", true)
            )
            dismiss()
        }
    }

}