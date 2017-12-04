package com.badlogic.masaki.passwordmanagementsample.contract

import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import com.badlogic.masaki.passwordmanagementsample.room.pojo.AccountImage
import com.mobsandgeeks.saripaar.ValidationError
import com.trello.rxlifecycle2.components.support.RxAppCompatActivity

/**
 * Created by masaki on 2017/11/26.
 */
interface EditAccountFragmentContract {
    interface View {
        fun initViews(v: android.view.View?)
        fun restoreViews(savedInstanceState: Bundle?)
        fun showPickPhotoDialog()
        fun showValidationErrors(errors: MutableList<ValidationError>?)
        fun showRegistrationError(msg: String)
        fun onRegistrationFinished(resId: Long)
        fun onLoadDataFinished(requestCode: Int, bitmapPair: Pair<Bitmap?, Bitmap?>)
        fun onLoadDataFailed(e: Throwable)
    }

    interface Actions {
        fun loadData(activity: RxAppCompatActivity, requestCode: Int, data: Intent?)
        fun registerAccount(activity: RxAppCompatActivity, account: AccountImage)
        fun insertAccount(activity: RxAppCompatActivity, account: AccountImage)
        fun updateAccount(activity: RxAppCompatActivity, account: AccountImage)
    }
}