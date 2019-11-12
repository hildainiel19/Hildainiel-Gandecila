package funplay.nativeapp.rps.data.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class PopupConfig(
    @Expose @SerializedName("enabled") val enabled: Boolean,
    @Expose @SerializedName("delay") val delay: Long,
    @Expose @SerializedName("start") val start: Long,
    @Expose @SerializedName("end") val end: Long,
    @Expose @SerializedName("imageUrl") val imageUrl: String,
    @Expose @SerializedName("showX") val showX: Boolean,
    @Expose @SerializedName("redirectUrl") val redirectUrl: String,
    @Expose @SerializedName("linkType") val linkType: String,
    @Expose @SerializedName("autoDismiss") val autoDismiss: Boolean,
    @Expose @SerializedName("backgroundDim") val backgroundDim: String
)