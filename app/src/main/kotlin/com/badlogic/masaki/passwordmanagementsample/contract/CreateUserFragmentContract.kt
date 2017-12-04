package com.badlogic.masaki.passwordmanagementsample.contract

import android.content.Context
import com.badlogic.masaki.passwordmanagementsample.room.User
import com.trello.rxlifecycle2.components.support.RxAppCompatActivity

/**
 * Created by masaki on 2017/11/16.
 */
interface CreateUserFragmentContract {
    interface View {
        fun showConfirmDialog()
        fun showRegisterError()
        fun startActivity()
    }

    interface Actions {
        fun registerUser(activity: RxAppCompatActivity, user: User)
    }
}