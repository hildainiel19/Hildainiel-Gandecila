package funplay.nativeapp.rps.data.remote

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import funplay.nativeapp.rps.data.models.User

data class RegisterResponse(
    @Expose @SerializedName("ResponseData")
    val responseData: User?,
    @Expose @SerializedName("ResponseCode")
    val responseCode: Int?,
    @Expose @SerializedName("ResponseMessage")
    val responseMessage: String?,
    @Expose @SerializedName("ResponseUserStatus")
    val responseUserStatus: ResponseUserStatus?
) {

    data class ResponseUserStatus(
        @Expose @SerializedName("Status")
        val status: Int?,
        @Expose @SerializedName("Message")
        val message: String?
    )
}