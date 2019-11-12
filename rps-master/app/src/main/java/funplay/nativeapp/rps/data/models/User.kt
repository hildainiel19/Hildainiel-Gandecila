package funplay.nativeapp.rps.data.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class User(
    @Expose @SerializedName("Status")
    val status: Status?,
    @Expose @SerializedName("Balance")
    val balance: Balance?,
    @Expose @SerializedName("AppKey")
    val appKey: String?,
    @Expose @SerializedName("AppSessionId")
    val appSessionId: String?,
    @Expose @SerializedName("RequestObject")
    val requestObject: RequestObject?,
    @Expose @SerializedName("FullName")
    val fullName: String?,
    @Expose @SerializedName("AccountName")
    val accountName: String?,
    @Expose @SerializedName("CurrencyCode")
    val currencyCode: String?,
    @Expose @SerializedName("PartialSignup")
    val partialSignup: PartialSignup?,
    @Expose @SerializedName("ResetPassword")
    val resetPassword: Boolean?,
    @Expose @SerializedName("LanguageCode")
    val languageCode: String?,
    @Expose @SerializedName("CountryCode")
    val countryCode: String?,
    @Expose @SerializedName("Token")
    val token: String?,
    @Expose @SerializedName("MemberCode")
    val memberCode: String?,
    @Expose @SerializedName("MemberId")
    val memberId: Long?,
    @Expose @SerializedName("RiskId")
    val riskId: String?,
    @Expose @SerializedName("Palazzo")
    val palazzo: String?,
    @Expose @SerializedName("PaymentGroup")
    val paymentGroup: String?,
    @Expose @SerializedName("CurrentSessionId")
    val currentSessionId: String?,
    @Expose @SerializedName("MemberName")
    val memberName: String?,
    @Expose @SerializedName("IsTestAccount")
    val isTestAccount: Boolean?,
    @Expose @SerializedName("OddsType")
    val oddsType: Int?,
    @Expose @SerializedName("IsPriorityVip")
    val isPriorityVip: Boolean?,
    @Expose @SerializedName("ProductToken")
    val productToken: String?,
    @Expose @SerializedName("SubPlatformId")
    val subPlatformId: Int?
) {
    data class Status(
        @Expose @SerializedName("ReturnValue")
        val returnValue: Int?,
        @Expose @SerializedName("ReturnMessage")
        val returnMessage: String?
    )

    data class Balance(
        @Expose @SerializedName("Id")
        val id: Long?,
        @Expose @SerializedName("Name")
        val name: String?,
        @Expose @SerializedName("Balance")
        val balance: String?,
        @Expose @SerializedName("CurrencyLabel")
        val currencyLabel: String?,
        @Expose @SerializedName("StatusCode")
        val statusCode: Int?,
        @Expose @SerializedName("SortOrder")
        val sortOrder: String?
    )

    data class RequestObject(
        @Expose @SerializedName("test")
        val test: String?
    )

    data class PartialSignup(
        @Expose @SerializedName("test")
        val test: String?
    )
}