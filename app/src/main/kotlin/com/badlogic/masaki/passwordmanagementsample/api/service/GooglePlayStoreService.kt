package com.badlogic.masaki.passwordmanagementsample.api.service

import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Query
import javax.inject.Singleton

/**
 * Created by shojimasaki on 2017/12/27.
 */
@Singleton
interface GooglePlayStoreService {
    @GET("/store/apps/details")
    fun getLatestAppVersion(@Query("id")id: String): Single<String>
}