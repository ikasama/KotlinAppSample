package com.badlogic.masaki.passwordmanagementsample.presenter

import com.badlogic.masaki.passwordmanagementsample.constants.ErrorType
import com.badlogic.masaki.passwordmanagementsample.constants.PrefKeys.USER_ID
import com.badlogic.masaki.passwordmanagementsample.contract.AccountListFragmentContract
import com.badlogic.masaki.passwordmanagementsample.room.PasswordManagementDBHelper
import com.badlogic.masaki.passwordmanagementsample.room.pojo.AccountImage
import com.badlogic.masaki.passwordmanagementsample.storage.AppSettings
import com.trello.rxlifecycle2.android.ActivityEvent
import com.trello.rxlifecycle2.components.support.RxAppCompatActivity
import com.trello.rxlifecycle2.kotlin.bindToLifecycle
import com.trello.rxlifecycle2.kotlin.bindUntilEvent
import io.reactivex.Observable
import io.reactivex.ObservableEmitter
import io.reactivex.ObservableOnSubscribe
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import java.util.*

/**
 * Created by masaki on 2017/11/25.
 */
class AccountListFragmentPresenter(private val mAccountListView: AccountListFragmentContract.View)
    : AccountListFragmentContract.Actions {
    override fun findAccount(activity: RxAppCompatActivity, categoryId: Int?) {
        val dao = PasswordManagementDBHelper.database(activity).accountDao()
        val userId = AppSettings.getString(activity, USER_ID, "") ?: ""
        val single: Single<MutableList<AccountImage>> = if (categoryId != null) {
            dao.findByCategoryId(userId, categoryId)
        } else {
            dao.findAll(userId)
        }
        single
                .toObservable()
                .flatMap { result: MutableList<AccountImage> ->
                    Collections.sort(result) { o1, o2 ->
                        val name1: String? = o1?.accountName
                        val name2: String? = o2?.accountName
                        if (name1 != null) {
                            if (name2 != null) {
                                name1.compareTo(name2)
                            } else {
                                1
                            }
                        } else {
                            if (name2 != null) {
                                -1
                            } else {
                                0
                            }
                        }
                    }
                    val a: MutableList<AccountImage> = result
                    Observable.just(a)
                }
                .bindUntilEvent(activity, ActivityEvent.PAUSE)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeBy(
                        onError = { it.printStackTrace() },
                        onNext = { result: MutableList<AccountImage> ->
                            mAccountListView.updateListView(result)
                        },
                        onComplete = {}
                )
    }

    override fun deleteAccount(activity: RxAppCompatActivity, account: AccountImage) {
        val subscribe = ObservableOnSubscribe { emitter: ObservableEmitter<AccountImage> ->
            try {
                val db = PasswordManagementDBHelper.database(activity)
                db.runInTransaction {
                    account.iconId?.let {
                        val result = db.imageDao().deleteById(it)
                        when {
                            result <= 0 -> { throw RuntimeException("delete image failed.") }
                            else -> {}
                        }
                    }
                    val resId = db.accountDao().deleteById(account.accountId!!)
                    when {
                        resId > 0 ->  {
                            emitter.onNext(account)
                            emitter.onComplete()
                        }
                        else -> emitter.onError(RuntimeException("delete failed"))
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
                emitter.onError(e)
            }
        }
        Observable.create(subscribe)
                .bindToLifecycle(activity)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeBy(
                        onError = { e ->
                            e.printStackTrace()
                            mAccountListView.showErrorMessage(ErrorType.DELETE)
                        },
                        onNext = { account ->
                            mAccountListView.notifyAccountDeleted(account)
                        }
                )
    }
}