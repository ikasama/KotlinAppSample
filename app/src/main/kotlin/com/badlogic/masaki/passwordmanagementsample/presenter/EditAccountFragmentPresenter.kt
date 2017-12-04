package com.badlogic.masaki.passwordmanagementsample.presenter

import android.content.Context
import android.content.Intent
import android.database.sqlite.SQLiteConstraintException
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.media.ThumbnailUtils
import android.os.Bundle
import android.text.TextUtils
import android.widget.Toast
import com.badlogic.masaki.passwordmanagementsample.GlideApp
import com.badlogic.masaki.passwordmanagementsample.R
import com.badlogic.masaki.passwordmanagementsample.condition.CategoryUpdateFlag
import com.badlogic.masaki.passwordmanagementsample.condition.checkCategoryUpdateCondition
import com.badlogic.masaki.passwordmanagementsample.constants.Image.THUMBNAIL_HEIGHT
import com.badlogic.masaki.passwordmanagementsample.constants.Image.THUMBNAIL_WIDTH
import com.badlogic.masaki.passwordmanagementsample.constants.RequestCode.CODE_CAMERA
import com.badlogic.masaki.passwordmanagementsample.constants.RequestCode.CODE_GALLERY
import com.badlogic.masaki.passwordmanagementsample.contract.EditAccountFragmentContract
import com.badlogic.masaki.passwordmanagementsample.contract.EditCategoryFragmentContract
import com.badlogic.masaki.passwordmanagementsample.fragment.EditCategoryFragment
import com.badlogic.masaki.passwordmanagementsample.room.Category
import com.badlogic.masaki.passwordmanagementsample.room.Image
import com.badlogic.masaki.passwordmanagementsample.room.PasswordManagementDBHelper
import com.badlogic.masaki.passwordmanagementsample.room.pojo.AccountImage
import com.badlogic.masaki.passwordmanagementsample.room.pojo.CategoryImage
import com.badlogic.masaki.passwordmanagementsample.storage.AppSettings
import com.badlogic.masaki.passwordmanagementsample.util.LogWrapperD
import com.badlogic.masaki.passwordmanagementsample.util.getSingleOfSavingImageToInternal
import com.badlogic.masaki.passwordmanagementsample.util.toAccount
import com.badlogic.masaki.passwordmanagementsample.util.toCategory
import com.trello.rxlifecycle2.components.support.RxAppCompatActivity
import com.trello.rxlifecycle2.kotlin.bindToLifecycle
import io.reactivex.*
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import org.w3c.dom.Text
import java.io.IOException
import java.io.InputStream
import java.util.*

/**
 * Created by masaki on 2017/11/23.
 */
class EditAccountFragmentPresenter(private val mEditAccountView: EditAccountFragmentContract.View)
    : EditAccountFragmentContract.Actions {

    companion object {
        val TAG: String = EditAccountFragmentPresenter::class.java.simpleName
    }

    override fun loadData(activity: RxAppCompatActivity, requestCode: Int, data: Intent?) {
        fun getSingleOnSubscribe(code: Int):
                (SingleEmitter<Pair<out Bitmap?, out Bitmap?>>) -> Unit {
            return when(code) {
                CODE_CAMERA -> { emitter ->
                    try {
                        val extras: Bundle? = data?.extras
                        val glideBitmap: Bitmap? = extras?.get("data") as Bitmap?
                        val categoryBitmap: Bitmap?
                                = ThumbnailUtils.extractThumbnail(glideBitmap, THUMBNAIL_WIDTH, THUMBNAIL_HEIGHT)
                        emitter.onSuccess(Pair(glideBitmap, categoryBitmap))
                    } catch (e: Exception) {
                        emitter.onError(e)
                    }
                }

                CODE_GALLERY -> { emitter ->
                    try {
                        data?.data?.let {
                            val iStream = activity.contentResolver.openInputStream(it)
                            val glideBitmap: Bitmap? = BitmapFactory.decodeStream(iStream)
                            val categoryBitmap
                                    = ThumbnailUtils.extractThumbnail(glideBitmap, THUMBNAIL_WIDTH, THUMBNAIL_HEIGHT)
                            iStream?.let { it.close() }
                            emitter.onSuccess(Pair(glideBitmap, categoryBitmap))
                        }
                    } catch (e: Exception) {
                        emitter.onError(e)
                    }
                }

                else -> {
                    throw RuntimeException("Illegal requestCode has been passed.")
                }
            }
        }

        Single.create(getSingleOnSubscribe(requestCode))
                .bindToLifecycle(activity)
                .doOnDispose { LogWrapperD(TAG, "loadData doOnDispose called.")}
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeBy(
                        onError = { mEditAccountView.onLoadDataFailed(it) },
                        onSuccess = { mEditAccountView.onLoadDataFinished(requestCode, it)}
                )
    }

    override fun registerAccount(activity: RxAppCompatActivity, account: AccountImage) {
        when (checkCategoryUpdateCondition(account?.accountId, account.updatesIcon)) {
            CategoryUpdateFlag.NO_UPDATE -> {
                LogWrapperD(TAG, "NO_UPDATE")
            }
            CategoryUpdateFlag.INSERT -> {
                LogWrapperD(TAG, "INSERT")
                insertAccount(activity, account)
            }
            CategoryUpdateFlag.UPDATE_WITH_ICON, CategoryUpdateFlag.UPDATE_WITHOUT_ICON -> {
                LogWrapperD(TAG, "UPDATE_WITH_ICON")
                updateAccount(activity, account)
            }
        }
    }

    override fun insertAccount(activity: RxAppCompatActivity, account: AccountImage) {
        getSingleOfSavingImageToInternal(activity, account.bitmap)
                .flatMap({ path: String ->
                    createSingleOfInsertAccount(activity, account, path)
                })
                .bindToLifecycle(activity)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeBy(
                        onError = {
                            mEditAccountView.showRegistrationError(getErrorMessage(activity, it))
                        },
                        onSuccess = { resId ->
                            account.bitmap?.recycle()
                            account.bitmap = null
                            mEditAccountView.onRegistrationFinished(resId)
                        }
                )
    }

    private fun getErrorMessage(context: Context, e: Throwable): String {
        return if(e is SQLiteConstraintException) {
            context.getString(R.string.category_constraint_exception)
        } else {
            context.getString(R.string.fail_save_category)
        }
    }

    private fun createSingleOfInsertAccount(activity: RxAppCompatActivity,
                                          account: AccountImage, path: String): Single<Long> {
        return Single.create { emitter: SingleEmitter<Long> ->
            try {
                val db = PasswordManagementDBHelper.database(activity)
                db.runInTransaction {
                    var iconId: Long? = null
                    if (!TextUtils.isEmpty(path)) {
                        val date = Date()
                        iconId = db.imageDao().insertImage(Image(url = path, createdAt = date, updatedAt = date))
                        if (iconId <= 0) {
                            emitter.onError(RuntimeException("failed to update image."))
                        }
                    }
                    val date = Date()
                    val a = account.toAccount(context = activity, iconId = iconId?.toInt(),
                            createdAt = date, updatedAt = date)
                    val resId = db.accountDao().insert(a)
                    emitter.onSuccess(resId)
                }
            } catch (e: Exception) {
                e.printStackTrace()
                emitter.onError(e)
            }
        }
    }

    private fun createSingleOfAccountUpdate(activity: RxAppCompatActivity,
                                             account: AccountImage, path: String): Single<Long> {
        return Single.create { emitter: SingleEmitter<Long> ->
            try {
                val db = PasswordManagementDBHelper.database(activity)
                db.runInTransaction {
                    if (!TextUtils.isEmpty(path) && account.updatesIcon) {
                        if (account.iconId == null) {
                            account.iconId = db.imageDao().insertImage(Image(url = path, createdAt = Date(), updatedAt = Date())).toInt()
                        } else {
                            val result = db.imageDao().update(Image(imageId = account.iconId, url = path, updatedAt = Date()))
                            if (result <= 0) {
                                throw RuntimeException("failed to update image.")
                            }
                        }
                        val a = account.toAccount(context = activity, updatedAt = Date())
                        val resId = db.accountDao().update(a).toLong()
                        emitter.onSuccess(resId)
                    } else {
                        val a = account.toAccount(context = activity, updatedAt = Date())
                        val resId = db.accountDao().update(a).toLong()
                        emitter.onSuccess(resId)
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
                emitter.onError(e)
            }
        }
    }
    override fun updateAccount(activity: RxAppCompatActivity, account: AccountImage) {
        getSingleOfSavingImageToInternal(activity, account.bitmap)
                .flatMap({ path: String ->
                    createSingleOfAccountUpdate(activity, account, path)
                })
                .bindToLifecycle(activity)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeBy(
                        onError = { e ->
                            mEditAccountView.showRegistrationError(getErrorMessage(activity, e))
                        },
                        onSuccess = { resId ->
                            account.bitmap?.recycle()
                            account.bitmap = null
                            mEditAccountView.onRegistrationFinished(resId)
                        }
                )
    }
}