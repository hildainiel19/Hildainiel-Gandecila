package funplay.nativeapp.rps.data.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class InjectConfig(
    @Expose @SerializedName("injectConfig")
    val injectConfig: PrecacheManifest
) {
    data class PrecacheManifest(
        @Expose @SerializedName("linkInfos")
        val linkInfos: List<LinkInfo>,
        @Expose @SerializedName("lobbyBg")
        val lobbyBg: String?,
        @Expose @SerializedName("giftLink")
        val giftLink: LinkInfo
    ) {
        data class LinkInfo(
            @Expose @SerializedName("displayText")
            val displayText: String?,
            @Expose @SerializedName("linkUrl")
            val linkUrl: String?,
            @Expose @SerializedName("imageUrl")
            val imageUrl: String?,
            @Expose @SerializedName("visible")
            val visible: Boolean = false,
            @Expose @SerializedName("linkType")
            val linkType: String?
        )
    }
}