package com.badlogic.masaki.passwordmanagementsample.activity

import android.os.Bundle
import android.support.v4.app.Fragment
import com.badlogic.masaki.passwordmanagementsample.R
import com.badlogic.masaki.passwordmanagementsample.constants.PrefKeys.USER_CREATED
import com.badlogic.masaki.passwordmanagementsample.contract.LoginActivityContract
import com.badlogic.masaki.passwordmanagementsample.fragment.CreateUserFragment
import com.badlogic.masaki.passwordmanagementsample.fragment.LoginFragment
import com.badlogic.masaki.passwordmanagementsample.storage.AppSettings
import com.trello.rxlifecycle2.components.support.RxAppCompatActivity

/**
 * Created by masaki on 2017/10/25.
 */
class LoginActivity : RxAppCompatActivity(), LoginActivityContract.View {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        if (savedInstanceState == null) {
            initFragment()
        }
    }

    override fun initFragment() {
        fun getAppropriateFragment(): Fragment {
            return when (AppSettings.getBoolean(this, USER_CREATED)) {
                true -> LoginFragment.newInstance()
                false -> CreateUserFragment.newInstance()
                else -> throw RuntimeException("AppSettings.getBoolean returned null")
            }
        }
        supportFragmentManager.beginTransaction()
                .replace(R.id.content_container_activity_test_login, getAppropriateFragment())
                .commit()
    }
}