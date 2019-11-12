package funplay.nativeapp.rps.data.local

import com.chibatching.kotpref.KotprefModel
import org.koin.core.KoinComponent

object AppState : KotprefModel(), KoinComponent {

    var registerResponseString by stringPref()
    var lastPopupShow: Long by longPref()
}