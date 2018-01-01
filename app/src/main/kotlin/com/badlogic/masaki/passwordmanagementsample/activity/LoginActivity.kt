package com.badlogic.masaki.passwordmanagementsample.activity

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import com.badlogic.masaki.passwordmanagementsample.R
import com.badlogic.masaki.passwordmanagementsample.app.PasswordManagementSampleApplication
import com.badlogic.masaki.passwordmanagementsample.constants.PrefKeys.USER_CREATED
import com.badlogic.masaki.passwordmanagementsample.contract.LoginActivityContract
import com.badlogic.masaki.passwordmanagementsample.dialog.UpdateAppVersionDialogFragment
import com.badlogic.masaki.passwordmanagementsample.fragment.CreateUserFragment
import com.badlogic.masaki.passwordmanagementsample.fragment.LoginFragment
import com.badlogic.masaki.passwordmanagementsample.presenter.LoginActivityPresenter
import com.badlogic.masaki.passwordmanagementsample.storage.AppSettings
import com.trello.rxlifecycle2.components.support.RxAppCompatActivity

/**
 * Created by masaki on 2017/10/25.
 */
class LoginActivity : RxAppCompatActivity(),
        LoginActivityContract.View,
        UpdateAppVersionDialogFragment.OnPositiveButtonClickListener {

    companion object {
        const val URL_GOOGLE_PLAY: String = "https://play.google.com/store/apps/details?id=com.badlogic.masaki.passwordmanagementsample"
    }

    private val mPresenter: LoginActivityContract.Actions = LoginActivityPresenter(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        val app: PasswordManagementSampleApplication = application as PasswordManagementSampleApplication
        app.getAppComponent()?.inject(this)

        if (savedInstanceState == null) {
            initFragment()
        }

        mPresenter.init(application)
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

    override fun onResume() {
        super.onResume()
        mPresenter.onActivityResumed(this)
    }

    override fun showUpdateAppDialog() {
        UpdateAppVersionDialogFragment
                .newInstance(this as UpdateAppVersionDialogFragment.OnPositiveButtonClickListener?)
                .show(supportFragmentManager, null)
    }

    override fun onDialogPositiveButtonClick() {
        startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(URL_GOOGLE_PLAY)))
    }
}