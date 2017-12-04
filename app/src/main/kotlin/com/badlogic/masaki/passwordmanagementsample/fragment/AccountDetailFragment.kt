package com.badlogic.masaki.passwordmanagementsample.fragment

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.badlogic.masaki.passwordmanagementsample.GlideApp
import com.badlogic.masaki.passwordmanagementsample.R
import com.badlogic.masaki.passwordmanagementsample.contract.AccountDetailFragmentContract
import com.badlogic.masaki.passwordmanagementsample.room.pojo.AccountImage
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import com.badlogic.masaki.passwordmanagementsample.constants.ExtraCode.EXTRA_ACCOUNT
import com.badlogic.masaki.passwordmanagementsample.util.dateFormat


/**
 * Created by masaki on 2017/10/28.
 */
class AccountDetailFragment: BaseFragment(), AccountDetailFragmentContract.View {
    private val IMAGE_WIDTH = 96
    private val IMAGE_HEIGHT = 96

    private var mAccountName: TextView? = null
    private var mPassword: TextView? = null
    private var mComment: TextView? = null
    private var mUpdatedAt: TextView? = null
    private var mIcon: ImageView? = null
    private var mPasswordShown: Boolean = false

    companion object {
        val TAG: String = AccountDetailFragment::class.java.simpleName

        @JvmStatic
        fun newInstance(account: AccountImage?): AccountDetailFragment {
            val f = AccountDetailFragment()
            account?.let {
                val args = Bundle()
                args.putParcelable(EXTRA_ACCOUNT, account)
                f.arguments = args
            }
            return f
        }
    }
    private val mAccount: AccountImage? by lazy { getAccountImage() }

    private fun getAccountImage(): AccountImage? {
        return arguments?.getParcelable(EXTRA_ACCOUNT)
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val v: View? = inflater?.inflate(R.layout.fragment_account_detail, container, false)
        val bitmap: Bitmap? = BitmapFactory.decodeResource(resources, R.drawable.icon)
        v?.findViewById<ImageView>(R.id.imageIcon)?.setImageBitmap(bitmap)
        initViews(v)
        return v
    }

    override fun onResume() {
        super.onResume()
        GlideApp.with(this)
                .load(mAccount?.url).override(IMAGE_WIDTH, IMAGE_HEIGHT)
                .circleCrop().error(R.drawable.icon).into(mIcon)
    }

    private fun initViews(v: View?) {
        v?.run {
            mAccountName = findViewById(R.id.textAccountName)
            mPassword = findViewById(R.id.textPassword)
            mPassword?.setOnClickListener { swapPasswordVisibility() }
            mComment = findViewById(R.id.textComment)
            mUpdatedAt = findViewById(R.id.textUpdatedAt)
            mIcon = findViewById(R.id.imageIcon)
        }
        getAccountImage()
        mAccount?.run {
            mAccountName?.text = accountName ?: ""
            mPassword?.text = password ?: ""
            mComment?.text = comment
            mUpdatedAt?.text = StringBuilder(mUpdatedAt?.text).append(dateFormat(updatedAt)).toString()
        }
    }

    override fun swapPasswordVisibility() {
        synchronized(this) {
            when (mPasswordShown) {
                true -> {
                    mPassword?.transformationMethod = HideReturnsTransformationMethod.getInstance()
                    mPasswordShown = false
                }
                false -> {
                    mPassword?.transformationMethod = PasswordTransformationMethod.getInstance()
                    mPasswordShown = true
                }
            }
        }
    }
}