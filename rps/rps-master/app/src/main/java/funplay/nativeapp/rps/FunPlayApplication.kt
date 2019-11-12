package funplay.nativeapp.rps

import android.app.Application
import android.os.Build
import android.webkit.WebView
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings
import com.google.gson.GsonBuilder
import funplay.nativeapp.rps.data.remote.ManifestService
import gameplay.casinomobile.teddybear.Teddy
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class FunPlayApplication : Application() {
    companion object {
        val TAG = FunPlayApplication::class.java.simpleName
    }

    val funPlayModule = module {
        single {
            GsonBuilder().excludeFieldsWithoutExposeAnnotation().create()
        }
        single {
            Retrofit.Builder().baseUrl(BuildConfig.precacheBaseUrl).client(
                OkHttpClient.Builder()
                    .addInterceptor(HttpLoggingInterceptor().apply {
                        level = HttpLoggingInterceptor.Level.BODY
                    }).build()
            ).addConverterFactory(GsonConverterFactory.create(get()))
                .build().create(ManifestService::class.java)
        }
        single {
            FirebaseRemoteConfig.getInstance().apply {
                if (BuildConfig.DEBUG) {
                    setConfigSettingsAsync(
                        FirebaseRemoteConfigSettings.Builder().setMinimumFetchIntervalInSeconds(1).build()
                    )
                }
                setDefaultsAsync(R.xml.firebase_defaults)
            }
        }
        single {
            Teddy
        }
    }

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@FunPlayApplication)
            modules(listOf(funPlayModule))
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            WebView.setWebContentsDebuggingEnabled(BuildConfig.DEBUG)
        }
    }
}