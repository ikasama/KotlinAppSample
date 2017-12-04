package com.badlogic.masaki.passwordmanagementsample.contract

import android.os.Bundle

/**
 * Created by masaki on 2017/11/19.
 */
interface CategoryListActivityContract {
    interface View {
        fun setUpViews()
        fun initFragment(savedInstanceState: Bundle?)
    }
}