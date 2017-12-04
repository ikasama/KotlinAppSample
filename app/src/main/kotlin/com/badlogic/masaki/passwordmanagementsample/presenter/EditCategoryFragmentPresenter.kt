package com.badlogic.masaki.passwordmanagementsample.presenter

import android.content.Context
import android.content.Intent
import android.database.sqlite.SQLiteConstraintException
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.media.ThumbnailUtils
import android.os.Bundle
import android.text.TextUtils
import com.badlogic.masaki.passwordmanagementsample.R
import com.badlogic.masaki.passwordmanagementsample.condition.CategoryUpdateFlag
import com.badlogic.masaki.passwordmanagementsample.condition.checkCategoryUpdateCondition
import com.badlogic.masaki.passwordmanagementsample.constants.Image.THUMBNAIL_HEIGHT
import com.badlogic.masaki.passwordmanagementsample.constants.Image.THUMBNAIL_WIDTH
import com.badlogic.masaki.passwordmanagementsample.constants.RequestCode.CODE_CAMERA
import com.badlogic.masaki.passwordmanagementsample.constants.RequestCode.CODE_GALLERY
import com.badlogic.masaki.passwordmanagementsample.contract.EditCategoryFragmentContract
import com.badlogic.masaki.passwordmanagementsample.room.Image
import com.badlogic.masaki.passwordmanagementsample.room.PasswordManagementDBHelper
import com.badlogic.masaki.passwordmanagementsample.room.pojo.CategoryImage
import com.badlogic.masaki.passwordmanagementsample.util.LogWrapperD
import com.badlogic.masaki.passwordmanagementsample.util.getSingleOfSavingImageToInternal
import com.badlogic.masaki.passwordmanagementsample.util.toCategory
import com.trello.rxlifecycle2.components.support.RxAppCompatActivity
import com.trello.rxlifecycle2.kotlin.bindToLifecycle
import io.reactivex.*
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import java.util.*

/**
 * Created by masaki on 2017/11/23.
 */
class EditCategoryFragmentPresenter(private val mEditCategoryView: EditCategoryFragmentContract.View)
    : EditCategoryFragmentContract.Actions {

    companion object {
        val TAG: String = EditCategoryFragmentPresenter::class.java.simpleName
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
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeBy(
                        onError = { mEditCategoryView.onLoadDataFailed(it) },
                        onSuccess = { mEditCategoryView.onLoadDataFinished(requestCode, it)}
                )
    }

    override fun registerCategory(activity: RxAppCompatActivity, category: CategoryImage) {
        when (checkCategoryUpdateCondition(category?.categoryId, category.updatesIcon)) {
            CategoryUpdateFlag.NO_UPDATE -> {
                LogWrapperD(TAG, "NO_UPDATE")
            }
            CategoryUpdateFlag.INSERT -> {
                LogWrapperD(TAG, "INSERT")
                insertCategory(activity, category)
            }
            CategoryUpdateFlag.UPDATE_WITH_ICON, CategoryUpdateFlag.UPDATE_WITHOUT_ICON -> {
                LogWrapperD(TAG, "UPDATE_WITH_ICON")
                updateCategory(activity, category)
            }
        }
    }

    override fun insertCategory(activity: RxAppCompatActivity, category: CategoryImage) {
        getSingleOfSavingImageToInternal(activity, category.bitmap)
                .flatMap({ path: String ->
                    createSingleOfInsertCategory(activity, category, path)
                })
                .bindToLifecycle(activity)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeBy(
                        onError = { e ->
                            mEditCategoryView.showRegistrationError(getErrorMessage(activity, e))
                        },
                        onSuccess = { resId ->
                            category.bitmap?.recycle()
                            category.bitmap = null
                            mEditCategoryView.onRegistrationFinished(resId)
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

    private fun createSingleOfInsertCategory(activity: RxAppCompatActivity,
                                          category: CategoryImage, path: String): Single<Long> {
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
                    val c = category.toCategory(context = activity, imageId = iconId?.toInt(),
                            createdAt = date, updatedAt = date)
                    val resId = db.categoryDao().insert(c)
                    emitter.onSuccess(resId)
                }
            } catch (e: Exception) {
                e.printStackTrace()
                emitter.onError(e)
            }
        }
    }

    private fun createSingleOfCategoryUpdate(activity: RxAppCompatActivity,
                                             category: CategoryImage, path: String): Single<Long> {
        return Single.create { emitter: SingleEmitter<Long> ->
            try {
                val db = PasswordManagementDBHelper.database(activity)
                db.runInTransaction {
                    if (!TextUtils.isEmpty(path) && category.updatesIcon) {
                        if (category.iconId == null) {
                            category.iconId = db.imageDao().insertImage(Image(url = path, createdAt = Date(), updatedAt = Date())).toInt()
                        } else {
                            val result = db.imageDao().update(Image(imageId = category.iconId, url = path, updatedAt = Date()))
                            if (result <= 0) {
                                throw RuntimeException("failed to update image.")
                            }
                        }
                        val c = category.toCategory(context = activity, updatedAt = Date())
                        val resId = db.categoryDao().update(c).toLong()
                        emitter.onSuccess(resId)
                    } else {
                        val c = category.toCategory(context = activity, updatedAt = Date())
                        val resId = db.categoryDao().update(c).toLong()
                        emitter.onSuccess(resId)
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
                emitter.onError(e)
            }
        }
    }

    override fun updateCategory(activity: RxAppCompatActivity, category: CategoryImage) {
        getSingleOfSavingImageToInternal(activity, category.bitmap)
                .flatMap({ path: String ->
                    createSingleOfCategoryUpdate(activity, category, path)
                })
                .bindToLifecycle(activity)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeBy(
                        onError = { e ->
                            mEditCategoryView.showRegistrationError(getErrorMessage(activity, e))
                        },
                        onSuccess = { resId ->
                            category.bitmap?.recycle()
                            category.bitmap = null
                            mEditCategoryView.onRegistrationFinished(resId)
                        }
                )
    }
}