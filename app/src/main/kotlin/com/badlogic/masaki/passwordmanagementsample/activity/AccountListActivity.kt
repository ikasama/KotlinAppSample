package com.badlogic.masaki.passwordmanagementsample.activity

import android.content.Intent
import android.os.Bundle
import android.widget.FrameLayout
import com.badlogic.masaki.passwordmanagementsample.R
import com.badlogic.masaki.passwordmanagementsample.activity.EditAccountActivity.Companion.FLAG_INSERT
import com.badlogic.masaki.passwordmanagementsample.constants.ExtraCode.EXTRA_ACCOUNT
import com.badlogic.masaki.passwordmanagementsample.constants.ExtraCode.EXTRA_CATEGORY
import com.badlogic.masaki.passwordmanagementsample.constants.ExtraCode.EXTRA_CATEGORY_ID
import com.badlogic.masaki.passwordmanagementsample.constants.ExtraCode.EXTRA_CREATE_FLAG
import com.badlogic.masaki.passwordmanagementsample.contract.AccountListActivityContract
import com.badlogic.masaki.passwordmanagementsample.fragment.AccountDetailFragment
import com.badlogic.masaki.passwordmanagementsample.fragment.AccountListFragment
import com.badlogic.masaki.passwordmanagementsample.listener.OnAccountItemClickListener
import com.badlogic.masaki.passwordmanagementsample.room.Category
import com.badlogic.masaki.passwordmanagementsample.room.pojo.AccountImage

/**
 * Created by masaki on 2017/10/26.
 */
class AccountListActivity : BaseTwoPaneActivity(),
        AccountListActivityContract.View,
        OnAccountItemClickListener {

    companion object {
        val TAG: String = CategoryListActivity::class.java.simpleName
    }

    private val mCategory: Category? by lazy { getCategory() }

    private fun getCategory(): Category? {
        return intent?.getParcelableExtra(EXTRA_CATEGORY)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        getCategory()
        initViews(savedInstanceState)
    }

    private fun isTwoPane() =
        findViewById<FrameLayout>(R.id.content_container_account_detail) != null

    override fun initViews(savedInstanceState: Bundle?) {
        val fragment = AccountListFragment.newInstance(mCategory)
        supportFragmentManager.beginTransaction()
                .replace(R.id.content_container_account_list, fragment)
                .commit()

        mFloatingActionButton.setOnClickListener { _ ->
            startActivity(Intent(this, EditAccountActivity::class.java)
                    .putExtra(EXTRA_CREATE_FLAG, FLAG_INSERT)
                    .putExtra(EXTRA_CATEGORY_ID, mCategory?.categoryId))
        }
    }

    override fun onAccountItemClick(account: AccountImage) {
        when(isTwoPane()) {
            true -> {
                replaceDetailFragment(account)
            }
            false -> {
                startAccountDetailActivity(account)
            }
        }
    }

    override fun replaceDetailFragment(account: AccountImage) {
        val fragment = AccountDetailFragment.newInstance(account)
        supportFragmentManager.beginTransaction()
                .replace(R.id.content_container_account_detail, fragment)
                .commit()
    }

    override fun startAccountDetailActivity(account: AccountImage) {
        startActivity(Intent(this,
               AccountDetailActivity::class.java).putExtra(EXTRA_ACCOUNT, account))
    }

    override fun setToolbar() {
        mToolbar.title = getString(R.string.account_list)
        super.setToolbar()
    }
}