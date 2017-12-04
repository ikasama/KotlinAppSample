package com.badlogic.masaki.passwordmanagementsample.contract

import com.badlogic.masaki.passwordmanagementsample.constants.ErrorType
import com.badlogic.masaki.passwordmanagementsample.room.pojo.AccountImage
import com.trello.rxlifecycle2.components.support.RxAppCompatActivity

/**
 * Created by masaki on 2017/11/25.
 */
interface AccountListFragmentContract {
    interface View {
        fun showErrorMessage(errorType: ErrorType)
        fun showDeleteDialog(account: AccountImage)
        fun updateListView(accountList: MutableList<AccountImage>)
        fun notifyAccountDeleted(account: AccountImage)
        fun startEditAccountActivity(account: AccountImage, categoryId: Int?)
        fun showAccountDetailScreen(account: AccountImage)
    }

    interface Actions {
        fun findAccount(activity: RxAppCompatActivity, categoryId: Int?)
        fun deleteAccount(activity: RxAppCompatActivity, account: AccountImage)
    }
}