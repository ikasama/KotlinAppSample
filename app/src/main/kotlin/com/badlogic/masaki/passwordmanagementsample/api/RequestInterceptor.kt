package com.badlogic.masaki.passwordmanagementsample.api

import android.content.Context
import android.net.ConnectivityManager
import okhttp3.Interceptor
import okhttp3.Response
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Created by shojimasaki on 2017/12/29.
 */
@Singleton
class RequestInterceptor
    @Inject constructor(private val mConnectivityManager: ConnectivityManager): Interceptor {

    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        val r = chain.request().newBuilder()

        if (isConnected()) {
            val maxAge = 2 * 60
            r.addHeader("cache-control", "public, max-age=" + maxAge)
        } else {
            val maxStale = 30 * 24 * 60 * 60 // 30 days
            r.addHeader("cache-control", "public, only-if-cached, max-stale=" + maxStale)
        }

        return chain.proceed(r.build())
    }

    private fun isConnected() =
            mConnectivityManager.activeNetworkInfo?.isConnectedOrConnecting ?: false
}