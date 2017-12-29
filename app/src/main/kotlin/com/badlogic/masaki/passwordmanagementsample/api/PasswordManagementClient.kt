package com.badlogic.masaki.passwordmanagementsample.api

import com.badlogic.masaki.passwordmanagementsample.api.service.GooglePlayStoreService
import io.reactivex.Single
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Created by shojimasaki on 2017/12/27.
 */
@Singleton
class PasswordManagementClient
@Inject constructor(private val mGooglePlayStoreService: GooglePlayStoreService) {

    fun getLatestAppVersion(id: String): Single<String> = mGooglePlayStoreService.getLatestAppVersion(id)
}