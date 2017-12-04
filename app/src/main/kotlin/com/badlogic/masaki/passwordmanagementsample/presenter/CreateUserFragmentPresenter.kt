package com.badlogic.masaki.passwordmanagementsample.presenter

import android.content.Context
import com.badlogic.masaki.passwordmanagementsample.constants.PrefKeys.IS_LOGGED_IN
import com.badlogic.masaki.passwordmanagementsample.constants.PrefKeys.NICKNAME
import com.badlogic.masaki.passwordmanagementsample.constants.PrefKeys.USER_CREATED
import com.badlogic.masaki.passwordmanagementsample.constants.PrefKeys.USER_ID
import com.badlogic.masaki.passwordmanagementsample.contract.CreateUserFragmentContract
import com.badlogic.masaki.passwordmanagementsample.room.PasswordManagementDBHelper
import com.badlogic.masaki.passwordmanagementsample.room.User
import com.badlogic.masaki.passwordmanagementsample.storage.AppSettings
import com.trello.rxlifecycle2.components.support.RxAppCompatActivity
import io.reactivex.Single
import io.reactivex.SingleEmitter
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers

/**
 * Created by masaki on 2017/11/16.
 */
class CreateUserFragmentPresenter(private val mCreateUserView: CreateUserFragmentContract.View)
    : CreateUserFragmentContract.Actions {
    override fun registerUser(activity: RxAppCompatActivity, user: User) {
        verifyUserRecordExists(activity)
                .flatMap { hasRecord ->
                    tryToRegisterUser(activity, hasRecord, user)
                }
                .flatMap { resId ->
                    when {
                        resId > 0 -> updateSettings(activity, user)
                        else -> Single.error(RuntimeException("error occurred while settings' update."))
                    }
                }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeBy(
                        onError = { e ->
                            e.printStackTrace()
                            mCreateUserView.showRegisterError()
                        },
                        onSuccess = {
                            mCreateUserView.startActivity()
                        }
                )
    }

    private fun updateSettings(context: Context, user: User): Single<Boolean> {
        return Single.create { emitter: SingleEmitter<Boolean> ->
            try {
                AppSettings.apply {
                    putBoolean(context, USER_CREATED, true)
                    putBoolean(context, IS_LOGGED_IN, true)
                    putString(context, USER_ID, user.userId)
                    putString(context, NICKNAME, user.nickname)
                }
                emitter.onSuccess(true)
            } catch (e: Exception) {
                e.printStackTrace()
                emitter.onError(e)
            }
        }
    }

    private fun tryToRegisterUser(context: Context, hasRecord: Boolean, user: User): Single<Long> {
        return when {
            hasRecord -> Single.error({ RuntimeException("User table has already records.")})
            else ->  {
                Single.create(
                        { emitter: SingleEmitter<Long> ->
                            try {
                                val resId = PasswordManagementDBHelper.database(context).userDao()
                                        .insert(user)
                                emitter.onSuccess(resId)
                            } catch (e: Exception) {
                                e.printStackTrace()
                                emitter.onError(e)
                            }
                        })

            }
        }
    }
    private fun verifyUserRecordExists(context: Context): Single<Boolean> {
        return Single.create(
                { emitter: SingleEmitter<Boolean> ->
                    try {
                        val resId = PasswordManagementDBHelper.database(context).userDao()
                                .hasRecord()
                        emitter.onSuccess(resId > 0)
                    } catch (e: Exception) {
                        e.printStackTrace()
                        emitter.onError(e)
                    }
                })
    }
}