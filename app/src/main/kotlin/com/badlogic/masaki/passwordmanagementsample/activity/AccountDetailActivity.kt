package com.badlogic.masaki.passwordmanagementsample.activity

import android.os.Bundle
import android.view.View
import com.badlogic.masaki.passwordmanagementsample.R
import com.badlogic.masaki.passwordmanagementsample.constants.ExtraCode.EXTRA_ACCOUNT
import com.badlogic.masaki.passwordmanagementsample.contract.AccountDetailActivityContract
import com.badlogic.masaki.passwordmanagementsample.fragment.AccountDetailFragment
import com.badlogic.masaki.passwordmanagementsample.room.pojo.AccountImage

/**
 * Created by masaki on 2017/10/28.
 */
class AccountDetailActivity: BaseActivity(), AccountDetailActivityContract.View {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initViews(savedInstanceState)
    }

    override fun initViews(savedInstanceState: Bundle?) {
        mFloatingActionButton.visibility = View.GONE

        if (savedInstanceState == null) {
            val account = intent?.getParcelableExtra<AccountImage>(EXTRA_ACCOUNT)
            supportFragmentManager.beginTransaction()
                    .replace(R.id.content_container, AccountDetailFragment.newInstance(account))
                    .commit()
        }
    }

    override fun setToolbar() {
        mToolbar.title = getString(R.string.account_detail)
        super.setToolbar()
    }
}