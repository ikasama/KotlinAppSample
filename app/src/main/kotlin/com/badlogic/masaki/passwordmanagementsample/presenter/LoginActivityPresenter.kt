package com.badlogic.masaki.passwordmanagementsample.presenter

import android.app.Application
import android.content.Context
import android.widget.Toast
import com.badlogic.masaki.passwordmanagementsample.BuildConfig
import com.badlogic.masaki.passwordmanagementsample.api.PasswordManagementClient
import com.badlogic.masaki.passwordmanagementsample.app.PasswordManagementSampleApplication
import com.badlogic.masaki.passwordmanagementsample.contract.LoginActivityContract
import com.badlogic.masaki.passwordmanagementsample.storage.AppSettings
import com.badlogic.masaki.passwordmanagementsample.util.LogWrapperD
import com.badlogic.masaki.passwordmanagementsample.util.getCurrentVersionName
import com.badlogic.masaki.passwordmanagementsample.util.getVersionInt
import com.trello.rxlifecycle2.android.ActivityEvent
import com.trello.rxlifecycle2.components.RxActivity
import com.trello.rxlifecycle2.components.support.RxAppCompatActivity
import com.trello.rxlifecycle2.kotlin.bindUntilEvent
import io.reactivex.Single
import io.reactivex.SingleEmitter
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

/**
 * Created by shojimasaki on 2017/12/29.
 */
class LoginActivityPresenter(private val mView: LoginActivityContract.View)
    : LoginActivityContract.Actions {

    @Inject
    lateinit var mPMClient: PasswordManagementClient

    override fun init(app: Application) {
        (app as PasswordManagementSampleApplication).getAppComponent().inject(this)
    }

    override fun onActivityResumed(activity: RxAppCompatActivity) {
        fun checkIfUpdateAvailable(ac: RxAppCompatActivity) {
            mPMClient.getLatestAppVersion(BuildConfig.APPLICATION_ID)
                    .bindUntilEvent(ac, ActivityEvent.PAUSE)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeBy(
                            onError = { e ->
                                e.printStackTrace()
                            },
                            onSuccess = { version ->
                                val currentVerInt = getVersionInt(getCurrentVersionName(ac)!!)
                                val latestVerInt = getVersionInt(version)
                                if (currentVerInt < latestVerInt) {
                                    mView.showUpdateAppDialog()
                                }
                            }
                    )
        }

        val updateChecked = AppSettings.getBoolean(activity, "update_checked", false)
        when (updateChecked) {
            true -> return
            else -> checkIfUpdateAvailable(activity)
        }
    }

    override fun makeVersionUpdateChecked(context: Context) {
        AppSettings.putBoolean(context, "update_checked", true)
    }
}