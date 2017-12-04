package com.badlogic.masaki.passwordmanagementsample.fragment

import com.trello.rxlifecycle2.components.support.RxAppCompatActivity
import com.trello.rxlifecycle2.components.support.RxFragment

/**
 * Created by masaki on 2017/11/23.
 */
abstract class BaseFragment : RxFragment() {

    fun BaseFragment.rxActivity(): RxAppCompatActivity {
        return when (activity) {
            is RxAppCompatActivity -> activity as RxAppCompatActivity
            else -> throw RuntimeException("You must have RxAppCompatActivity as parent.")
        }
    }

    fun BaseFragment.getString(resId: Int): String {
        return activity.getString(resId)
    }
}