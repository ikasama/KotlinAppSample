package com.badlogic.masaki.passwordmanagementsample.contract

import android.app.Application
import android.content.Context
import com.trello.rxlifecycle2.components.support.RxAppCompatActivity

/**
 * Created by masaki on 2017/11/15.
 */
interface LoginActivityContract {

    interface View {
        fun initFragment()
        fun showUpdateAppDialog()
    }

    interface Actions {
        fun init(app: Application)
        fun onActivityResumed(activity: RxAppCompatActivity)
        fun makeVersionUpdateChecked(context: Context)
    }
}