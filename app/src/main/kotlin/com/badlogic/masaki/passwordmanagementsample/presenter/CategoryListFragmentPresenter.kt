package com.badlogic.masaki.passwordmanagementsample.presenter

import com.badlogic.masaki.passwordmanagementsample.constants.ErrorType
import com.badlogic.masaki.passwordmanagementsample.contract.CategoryListFragmentContract
import com.badlogic.masaki.passwordmanagementsample.room.PasswordManagementDBHelper
import com.badlogic.masaki.passwordmanagementsample.room.pojo.CategoryImage
import com.badlogic.masaki.passwordmanagementsample.util.LogWrapperD
import com.trello.rxlifecycle2.android.ActivityEvent
import com.trello.rxlifecycle2.components.support.RxAppCompatActivity
import com.trello.rxlifecycle2.kotlin.bindToLifecycle
import com.trello.rxlifecycle2.kotlin.bindUntilEvent
import io.reactivex.*
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers

/**
 * Created by masaki on 2017/11/17.
 */
class CategoryListFragmentPresenter(private val mCategoryListView: CategoryListFragmentContract.View)
    : CategoryListFragmentContract.Actions {

    override fun findCategory(activity: RxAppCompatActivity, userId: String?) {
        PasswordManagementDBHelper.database(activity).categoryDao()
                .findAllCategoryWithUrlById(userId ?: "")
                .toObservable()
                .bindUntilEvent(activity, ActivityEvent.PAUSE)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeBy(
                        onError = { it.printStackTrace() },
                        onNext = { result: MutableList<CategoryImage> ->
                            mCategoryListView.updateListView(result)
                        },
                        onComplete = {}
                )
    }

    override fun deleteCategory(activity: RxAppCompatActivity, category: CategoryImage) {
        val subscribe = ObservableOnSubscribe { emitter: ObservableEmitter<CategoryImage> ->
            try {
                val db = PasswordManagementDBHelper.database(activity)
                db.runInTransaction {
                    category.iconId?.let {
                        val result = db.imageDao().deleteById(it)
                        when {
                            result <= 0 -> { throw RuntimeException("delete image failed.") }
                            else -> {}
                        }
                    }
                    val resId = db.categoryDao().deleteById(category.categoryId!!)
                    when {
                        resId > 0 ->  {
                            emitter.onNext(category)
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
                            mCategoryListView.showErrorMessage(ErrorType.DELETE)
                        },
                        onNext = { c ->
                            mCategoryListView.notifyCategoryDeleted(c)
                        }
                )
    }
}