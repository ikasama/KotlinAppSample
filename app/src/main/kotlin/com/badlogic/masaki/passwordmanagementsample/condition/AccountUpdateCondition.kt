package com.badlogic.masaki.passwordmanagementsample.condition

import com.badlogic.masaki.passwordmanagementsample.room.Account

/**
 * Created by masaki on 2017/11/11.
 */

enum class AccountUpdateFlag(val flag: Int) {
    NO_UPDATE(0),
    INSERT(1),
    UPDATE_WITH_ICON(1 shl 1),
    UPDATE_WITHOUT_ICON(1 shl 2)
}

fun checkAccountUpdateCondition(id: Int?, updatesAccount: Boolean = false,
                                       updatesIcon: Boolean = false): AccountUpdateFlag {
    return when {
        !updatesAccount -> AccountUpdateFlag.NO_UPDATE
        id == null -> AccountUpdateFlag.INSERT
        Account.isValidId(id) && updatesIcon -> AccountUpdateFlag.UPDATE_WITH_ICON
        Account.isValidId(id) && !updatesIcon -> AccountUpdateFlag.UPDATE_WITHOUT_ICON
        else -> AccountUpdateFlag.NO_UPDATE
    }
}