package funplay.nativeapp.rps.data.remote

import funplay.nativeapp.rps.data.models.InjectConfig
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Url

interface ManifestService {
    @GET
    fun fetchManifest(@Url url: String): Call<InjectConfig.PrecacheManifest>

    @GET
    fun fetchInjectConfig(@Url url: String): Call<InjectConfig>

    @GET("http://whatismyip.akamai.com")
    fun getIPAddress(): Call<ResponseBody>
}