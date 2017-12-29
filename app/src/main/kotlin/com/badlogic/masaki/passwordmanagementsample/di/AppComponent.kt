package com.badlogic.masaki.passwordmanagementsample.di

import com.badlogic.masaki.passwordmanagementsample.activity.LoginActivity
import com.badlogic.masaki.passwordmanagementsample.app.PasswordManagementSampleApplication
import com.badlogic.masaki.passwordmanagementsample.presenter.LoginActivityPresenter
import dagger.Component
import javax.inject.Singleton

/**
 * Created by shojimasaki on 2017/12/28.
 */
@Singleton
@Component(modules = arrayOf(AppModule::class, HttpClientModule::class))
interface AppComponent {
    fun inject(context: PasswordManagementSampleApplication)
    fun inject(loginActivity: LoginActivity)
    fun inject(loginActivityPresenter: LoginActivityPresenter)
}