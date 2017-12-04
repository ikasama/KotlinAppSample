package com.badlogic.masaki.passwordmanagementsample.util

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.text.Editable
import com.badlogic.masaki.passwordmanagementsample.constants.PrefKeys.USER_ID
import com.badlogic.masaki.passwordmanagementsample.room.Account
import com.badlogic.masaki.passwordmanagementsample.room.Category
import com.badlogic.masaki.passwordmanagementsample.room.pojo.AccountImage
import com.badlogic.masaki.passwordmanagementsample.room.pojo.CategoryImage
import com.badlogic.masaki.passwordmanagementsample.storage.AppSettings
import com.trello.rxlifecycle2.components.support.RxAppCompatActivity
import java.util.*

/**
 * Created by masaki on 2017/10/25.
 */

fun RxAppCompatActivity.rxActivity() = this

fun CategoryImage.toCategory() =
        Category(categoryId, categoryName, userId, iconId, createdAt, updatedAt)

fun CategoryImage.toCategory(context: Context, categoryId: Int? = null, categoryName: String? = null,
                             userId: String? = null, imageId: Int? = null,
                             createdAt: Date? = null, updatedAt: Date? = null): Category {
    return Category(categoryId = categoryId ?: this.categoryId,
            categoryName = categoryName ?: this.categoryName,
            userId = userId ?: AppSettings.getString(context, USER_ID, ""),
            iconId = imageId ?: iconId,
            createdAt = createdAt ?: this.createdAt,
            updatedAt = updatedAt ?: this.updatedAt)
}

fun AccountImage.toAccount() =
        Account(accountId, accountName, password, userId, categoryId, iconId, comment, createdAt, updatedAt)

fun AccountImage.toAccount(context: Context, accountId: Int? = null, accountName: String? = null,
                             password: String? = null, userId: String? = null,
                             categoryId: Int? = null, iconId: Int? = null, comment: String? = null,
                             createdAt: Date? = null, updatedAt: Date? = null): Account {
    return Account(accountId = accountId ?: this.accountId,
            accountName = accountName ?:  this.accountName,
            password = password ?: this.password,
            userId = userId ?: AppSettings.getString(context, USER_ID, ""),
            categoryId = categoryId ?: this.categoryId,
            iconId = iconId ?: this.iconId,
            comment = comment ?: this.comment,
            createdAt = createdAt ?: this.createdAt,
            updatedAt = updatedAt ?: this.updatedAt)
}

fun <T, R> T.bind(f: ((T) -> R)?): R? = f?.invoke(this)

fun CharSequence.toEditable() = Editable.Factory.getInstance().newEditable(this)
