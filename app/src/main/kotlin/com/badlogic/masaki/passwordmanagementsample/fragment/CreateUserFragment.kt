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
import com.badlogic.masaki.passwordmanagementsample.contract.CreateUserFragmentContract
import com.badlogic.masaki.passwordmanagementsample.presenter.CreateUserFragmentPresenter
import com.badlogic.masaki.passwordmanagementsample.room.User
import com.badlogic.masaki.passwordmanagementsample.util.hashPassword
import com.badlogic.masaki.passwordmanagementsample.util.showValidationError
import com.badlogic.masaki.passwordmanagementsample.dialog.ConfirmDialogFragment
import com.mobsandgeeks.saripaar.ValidationError
import com.mobsandgeeks.saripaar.Validator
import com.mobsandgeeks.saripaar.annotation.NotEmpty
import com.mobsandgeeks.saripaar.annotation.Password
import java.util.*

/**
 * Created by masaki on 2017/10/25.
 */
class CreateUserFragment: BaseFragment(), CreateUserFragmentContract.View,
        ConfirmDialogFragment.OnPositiveButtonClickListener {

    private var mValidator: Validator? = null

    @NotEmpty
    @Password(min = 6, scheme = Password.Scheme.ALPHA_NUMERIC)
    private var mPasswordEditText: EditText? = null

    @NotEmpty
    private var mNicknameEditText: EditText? = null

    private val mCrateUserPresenter: CreateUserFragmentContract.Actions
            = CreateUserFragmentPresenter(this)

    companion object {
        fun newInstance() = CreateUserFragment()
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val v: View? = inflater?.inflate(R.layout.fragment_create_user, container, false)
        initViews(v)
        setListener(v)
        return v
    }

    private fun initViews(v: View?) {
        mNicknameEditText = v?.findViewById(R.id.nicknameEditText)
        mPasswordEditText = v?.findViewById(R.id.passwordEditText)
    }

    private fun setListener(v: View?) {
        v?.findViewById<Button>(R.id.okButton)?.setOnClickListener { _ -> mValidator?.validate() }
        mValidator = Validator(this)
        mValidator?.setValidationListener(object: Validator.ValidationListener {
            override fun onValidationFailed(errors: MutableList<ValidationError>?) {
                errors?.let { showValidationError(this@CreateUserFragment.context, errors) }
            }

            override fun onValidationSucceeded() {
                showConfirmDialog()
            }
        })
    }

    override fun showConfirmDialog() {
        ConfirmDialogFragment.newInstance(this as ConfirmDialogFragment.OnPositiveButtonClickListener?)
                .show(activity.supportFragmentManager, null)
    }

    override fun onDialogPositiveButtonClick() {
        val password: String? = mPasswordEditText?.text?.toString()
        val nickname: String? = mNicknameEditText?.text?.toString()
        val user = User(nickname = nickname, password = hashPassword(nickname!!, password!!),
                isLoggedIn = 1, userId = UUID.randomUUID().toString(),
                createdAt = Date(), updatedAt = Date())
        mCrateUserPresenter.registerUser(rxActivity(), user)
    }

    override fun showRegisterError() {
        Toast.makeText(activity.applicationContext, getString(R.string.fail_register_user), Toast.LENGTH_SHORT).show()
    }

    override fun startActivity() {
        activity.run {
            startActivity(Intent(this@CreateUserFragment.activity, CategoryListActivity::class.java))
            finish()
        }
    }
}