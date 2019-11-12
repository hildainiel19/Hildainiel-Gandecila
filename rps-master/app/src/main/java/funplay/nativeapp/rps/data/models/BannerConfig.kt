package funplay.nativeapp.rps.data.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class BannerConfig(
    @Expose @SerializedName("enabled") val enabled: Boolean,
    @Expose @SerializedName("start") val start: Long,
    @Expose @SerializedName("end") val end: Long,
    @Expose @SerializedName("imageUrl") val imageUrl: String,
    @Expose @SerializedName("redirectUrl") val redirectUrl: String,
    @Expose @SerializedName("linkType") val linkType: String
)