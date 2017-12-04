package com.badlogic.masaki.passwordmanagementsample.condition

import com.badlogic.masaki.passwordmanagementsample.room.Category

/**
 * Created by masaki on 2017/11/11.
 */

enum class CategoryUpdateFlag(val flag: Int) {
    NO_UPDATE(0),
    INSERT(1),
    UPDATE_WITH_ICON(1 shl 1),
    UPDATE_WITHOUT_ICON(1 shl 2)
}

fun checkCategoryUpdateCondition(id: Int?, updatesIcon: Boolean): CategoryUpdateFlag = when {
    id == null -> CategoryUpdateFlag.INSERT
    Category.isValidId(id) && updatesIcon -> CategoryUpdateFlag.UPDATE_WITH_ICON
    Category.isValidId(id) && !updatesIcon -> CategoryUpdateFlag.UPDATE_WITHOUT_ICON
    else -> CategoryUpdateFlag.NO_UPDATE
}