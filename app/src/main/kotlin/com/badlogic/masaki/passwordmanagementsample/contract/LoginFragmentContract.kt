package com.badlogic.masaki.passwordmanagementsample.contract

import com.mobsandgeeks.saripaar.ValidationError
import com.trello.rxlifecycle2.components.support.RxAppCompatActivity

/**
 * Created by masaki on 2017/11/15.
 */
interface LoginFragmentContract {
    interface View {
        fun showValidationErrors(activity: RxAppCompatActivity, errors: MutableList<ValidationError>)
        fun onLoginSucceeded()
        fun onLoginFailed()
    }

    interface Actions {
        fun attemptLogin(activity: RxAppCompatActivity, userId: String, nickname: String, password: String)
    }
}