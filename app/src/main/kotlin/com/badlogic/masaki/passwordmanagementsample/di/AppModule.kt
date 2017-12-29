package com.badlogic.masaki.passwordmanagementsample.di

import android.app.Application
import android.content.Context
import dagger.Module
import dagger.Provides
import javax.inject.Singleton
import android.net.ConnectivityManager


/**
 * Created by shojimasaki on 2017/12/27.
 */
@Module
class AppModule(private val mContext: Application) {

    @Singleton
    @Provides
    fun provideContext(): Context = mContext

    @Provides
    @Singleton
    fun provideConnectivityManager(context: Context): ConnectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

}