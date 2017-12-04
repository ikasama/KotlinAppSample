package com.badlogic.masaki.passwordmanagementsample.contract

import com.badlogic.masaki.passwordmanagementsample.constants.ErrorType
import com.badlogic.masaki.passwordmanagementsample.room.pojo.CategoryImage
import com.trello.rxlifecycle2.components.support.RxAppCompatActivity

/**
 * Created by masaki on 2017/11/17.
 */
interface CategoryListFragmentContract {

    interface View {
        fun showErrorMessage(errorType: ErrorType)
        fun showDeleteDialog(category: CategoryImage)
        fun updateListView(categoryList: List<CategoryImage>)
        fun notifyCategoryDeleted(category: CategoryImage)
        fun startEditCategoryActivity(category: CategoryImage)
    }

    interface Actions {
        fun findCategory(activity: RxAppCompatActivity, userId: String?)
        fun deleteCategory(activity: RxAppCompatActivity, category: CategoryImage)
    }
}