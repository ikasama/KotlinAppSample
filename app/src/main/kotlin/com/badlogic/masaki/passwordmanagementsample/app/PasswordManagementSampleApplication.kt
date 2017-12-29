package com.badlogic.masaki.passwordmanagementsample.app

import android.app.Application
import android.content.Context
import com.badlogic.masaki.passwordmanagementsample.di.AppComponent
import com.badlogic.masaki.passwordmanagementsample.di.AppModule
import com.badlogic.masaki.passwordmanagementsample.di.DaggerAppComponent
import com.badlogic.masaki.passwordmanagementsample.di.HttpClientModule
import com.badlogic.masaki.passwordmanagementsample.room.PasswordManagementDBHelper
import com.badlogic.masaki.passwordmanagementsample.storage.AppSettings
import com.facebook.soloader.SoLoader
import com.facebook.stetho.Stetho
import com.squareup.leakcanary.LeakCanary
import com.squareup.leakcanary.RefWatcher


/**
 * Created by masaki on 2017/10/25.
 */
class PasswordManagementSampleApplication: Application() {

    private val refWatcher: RefWatcher by lazy { initLeakCanary() }
    private val mAppComponent: AppComponent by lazy {
        DaggerAppComponent.builder()
                .appModule(AppModule(this))
                .httpClientModule(HttpClientModule())
                .build()
    }

    fun getAppComponent() = mAppComponent

    fun getWatcher(context: Context): RefWatcher {
        val app = context.applicationContext as PasswordManagementSampleApplication
        return app.refWatcher
    }

    private fun initLeakCanary(): RefWatcher {
        return LeakCanary.install(this)
    }

    override fun onCreate() {
        super.onCreate()
        AppSettings.putBoolean(this, "update_checked", false)

        mAppComponent.inject(this)

        PasswordManagementDBHelper.init(this)
        /*
         *initializes Conceal (encryption library)
         */
        SoLoader.init(this, false)
        /*
        initializes Stetho
         */
        Stetho.initializeWithDefaults(this)
        /*
        initializes LeakCanary
         */
        initLeakCanary()
    }
}