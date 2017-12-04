package com.badlogic.masaki.passwordmanagementsample.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.badlogic.masaki.passwordmanagementsample.R
import com.badlogic.masaki.passwordmanagementsample.activity.CategoryListActivity
import com.badlogic.masaki.passwordmanagementsample.constants.PrefKeys.NICKNAME
import com.badlogic.masaki.passwordmanagementsample.constants.PrefKeys.USER_ID
import com.badlogic.masaki.passwordmanagementsample.contract.LoginFragmentContract
import com.badlogic.masaki.passwordmanagementsample.presenter.LoginFragmentPresenter
import com.badlogic.masaki.passwordmanagementsample.storage.AppSettings
import com.badlogic.masaki.passwordmanagementsample.util.showValidationError
import com.mobsandgeeks.saripaar.ValidationError
import com.mobsandgeeks.saripaar.Validator
import com.mobsandgeeks.saripaar.annotation.NotEmpty
import com.trello.rxlifecycle2.components.support.RxAppCompatActivity

/**
 * Created by masaki on 2017/10/25.
 */
class LoginFragment: BaseFragment(), LoginFragmentContract.View {

    companion object {
        val TAG: String = LoginFragment::class.java.simpleName

        fun newInstance() = LoginFragment()
    }

    private var mLoginButton: Button? = null

    private val mValidator: Validator = Validator(this)

    @NotEmpty
    private var mPasswordEditText: EditText? = null

    private val mPresenter: LoginFragmentContract.Actions = LoginFragmentPresenter(this)


    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val v: View? = inflater?.inflate(R.layout.fragment_login, container, false)
        mPasswordEditText = v?.findViewById(R.id.editText)
        mLoginButton = v?.findViewById(R.id.button)
        mValidator.setValidationListener(object: Validator.ValidationListener {
            override fun onValidationFailed(errors: MutableList<ValidationError>?) {
                errors?.let { showValidationErrors(this@LoginFragment.rxActivity(), errors) }
            }

            override fun onValidationSucceeded() {
                val userId: String = AppSettings.getString(this@LoginFragment.activity, USER_ID, "") as String
                val nickname: String = AppSettings.getString(this@LoginFragment.activity, NICKNAME, "") as String
                mPresenter.attemptLogin(rxActivity(), userId, nickname, mPasswordEditText?.text.toString())
            }
        })
        mLoginButton?.setOnClickListener { _ -> mValidator.validate() }
        return v
    }

    override fun showValidationErrors(activity: RxAppCompatActivity, errors: MutableList<ValidationError>) {
        showValidationError(this@LoginFragment.activity, errors)
    }

    override fun onLoginSucceeded() {
        startActivity(Intent(this@LoginFragment.activity, CategoryListActivity::class.java))
        activity.finish()
    }

    override fun onLoginFailed() {
        Toast.makeText(this@LoginFragment.activity.applicationContext, getString(R.string.fail_login), Toast.LENGTH_SHORT).show()
    }
}