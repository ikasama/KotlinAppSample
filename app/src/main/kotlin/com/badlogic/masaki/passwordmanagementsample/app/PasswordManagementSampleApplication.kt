package com.badlogic.masaki.passwordmanagementsample.app

import android.app.Application
import android.content.Context
import com.badlogic.masaki.passwordmanagementsample.room.PasswordManagementDBHelper
import com.facebook.soloader.SoLoader
import com.facebook.stetho.Stetho
import com.squareup.leakcanary.LeakCanary
import com.squareup.leakcanary.RefWatcher


/**
 * Created by masaki on 2017/10/25.
 */
class PasswordManagementSampleApplication: Application() {

    private val refWatcher: RefWatcher by lazy { initLeakCanary() }

    fun getWatcher(context: Context): RefWatcher {
        val app = context.applicationContext as PasswordManagementSampleApplication
        return app.refWatcher
    }

    private fun initLeakCanary(): RefWatcher {
        return LeakCanary.install(this)
    }

    override fun onCreate() {
        super.onCreate()
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