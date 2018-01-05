package com.badlogic.masaki.passwordmanagementsample.di

import android.content.Context
import android.net.ConnectivityManager
import com.badlogic.masaki.passwordmanagementsample.BuildConfig
import com.badlogic.masaki.passwordmanagementsample.api.PasswordManagementClient
import com.badlogic.masaki.passwordmanagementsample.api.RequestInterceptor
import com.badlogic.masaki.passwordmanagementsample.api.service.GooglePlayStoreService
import dagger.Module
import dagger.Provides
import okhttp3.Cache
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.ResponseBody
import okhttp3.logging.HttpLoggingInterceptor
import org.json.JSONException
import javax.inject.Singleton
import org.jsoup.Jsoup
import retrofit2.Converter
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import java.io.File
import java.io.IOException
import java.lang.reflect.Type

@Module
class HttpClientModule {

    companion object {
        const val CACHE_FILE_NAME = "okhttp.cache"
        const val MAX_CACHE_SIZE = (4 * 1024 * 1024).toLong()
    }

    @Provides
    fun provideRequestInterceptor(interceptor: RequestInterceptor): Interceptor {
        return interceptor
    }

    @Singleton
    @Provides
    fun provideOkHttpClient(context: Context, interceptor: Interceptor): OkHttpClient {
        val cacheDir = File(context.cacheDir, CACHE_FILE_NAME)
        val cache = Cache(cacheDir, MAX_CACHE_SIZE)

        return OkHttpClient.Builder()
                .cache(cache)
                .addInterceptor(interceptor)
                .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BASIC))
                .build()
    }

    @Singleton
    @Provides
    fun provideGooglePlayStoreService(client: OkHttpClient): GooglePlayStoreService {
        return Retrofit.Builder()
                .baseUrl(BuildConfig.PLAY_STORE_PATH)
                .client(client)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(JsoupConverterFactory())
                .build()
                .create(GooglePlayStoreService::class.java)
    }

    @Singleton
    @Provides
    fun providePMClient(service: GooglePlayStoreService): PasswordManagementClient =
            PasswordManagementClient(service)

    inner class JsoupConverterFactory : Converter.Factory() {

        override fun responseBodyConverter(type: Type?, annotations: Array<Annotation>?, retrofit: Retrofit?): Converter<ResponseBody, *>? {
            return JsoupConverter()
        }

        inner class JsoupConverter : Converter<ResponseBody, String> {

            @Throws(IOException::class)
            override fun convert(responseBody: ResponseBody): String {
                try {
                    return Jsoup.parse(responseBody.string())
                            .getElementsByAttributeValue("itemprop", "softwareVersion")
                            .first()
                            .text()
                } catch (e: JSONException) {
                    throw IOException("Failed to parse JSON", e)
                }

            }
        }
    }
}