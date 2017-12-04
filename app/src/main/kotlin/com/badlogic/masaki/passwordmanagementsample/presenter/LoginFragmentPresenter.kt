package com.badlogic.masaki.passwordmanagementsample.presenter

import com.badlogic.masaki.passwordmanagementsample.contract.LoginFragmentContract
import com.badlogic.masaki.passwordmanagementsample.room.PasswordManagementDBHelper
import com.badlogic.masaki.passwordmanagementsample.util.hashPassword
import com.trello.rxlifecycle2.android.ActivityEvent
import com.trello.rxlifecycle2.components.support.RxAppCompatActivity
import com.trello.rxlifecycle2.kotlin.bindUntilEvent
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers

/**
 * Created by masaki on 2017/11/15.
 */
class LoginFragmentPresenter(private val mLoginView: LoginFragmentContract.View)
    : LoginFragmentContract.Actions {

    override fun attemptLogin(activity: RxAppCompatActivity, userId: String, nickname: String, password: String) {
        val hash: String = hashPassword(nickname, password)
        PasswordManagementDBHelper.database(activity).userDao()
                .authenticate(userId, hash)
                .toObservable()
                .bindUntilEvent(activity, ActivityEvent.PAUSE)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeBy(
                        onError = { it.printStackTrace() },
                        onNext = { resId ->
                            when (resId) {
                                1 -> mLoginView.onLoginSucceeded()
                                else -> mLoginView.onLoginFailed()
                            }
                        }
                )
    }
}