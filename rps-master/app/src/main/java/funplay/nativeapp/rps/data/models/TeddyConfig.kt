package funplay.nativeapp.rps.data.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class TeddyConfig(
    @Expose @SerializedName("enabled")
    val useTeddy: Boolean = false,
    @Expose @SerializedName("domain")
    val domain: String,
    @Expose @SerializedName("path")
    val path: String
)