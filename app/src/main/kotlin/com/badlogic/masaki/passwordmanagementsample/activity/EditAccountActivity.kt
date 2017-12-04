package com.badlogic.masaki.passwordmanagementsample.activity

import android.os.Bundle
import android.view.View
import com.badlogic.masaki.passwordmanagementsample.R
import com.badlogic.masaki.passwordmanagementsample.constants.ExtraCode.EXTRA_ACCOUNT
import com.badlogic.masaki.passwordmanagementsample.constants.ExtraCode.EXTRA_CATEGORY_ID
import com.badlogic.masaki.passwordmanagementsample.constants.ExtraCode.EXTRA_CREATE_FLAG
import com.badlogic.masaki.passwordmanagementsample.contract.EditAccountActivityContract
import com.badlogic.masaki.passwordmanagementsample.fragment.EditAccountFragment
import com.badlogic.masaki.passwordmanagementsample.room.pojo.AccountImage

/**
 * Created by masaki on 2017/10/28.
 */
class EditAccountActivity : BaseActivity(), EditAccountActivityContract.View {

    companion object {
        val FLAG_INSERT: Int = 0
        val FLAG_UPDATE: Int = 1
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initViews(savedInstanceState)
    }

    override fun initViews(savedInstanceState: Bundle?) {
        mFloatingActionButton.visibility = View.GONE
        // Flag to decide which operation(insert or update) executes, but not used for now.
        val createFlag: Int = intent?.getIntExtra(EXTRA_CREATE_FLAG, 0) ?: 0
        val categoryId: Int? = if(intent?.getIntExtra(EXTRA_CATEGORY_ID, -1) != -1) {
            intent?.getIntExtra(EXTRA_CATEGORY_ID, -1)
        } else {
            null
        }
        val account: AccountImage = intent?.getParcelableExtra(EXTRA_ACCOUNT) ?: AccountImage()
        account.categoryId = categoryId

        if (savedInstanceState == null) {
            val fragment: EditAccountFragment = EditAccountFragment.newInstance(createFlag, account)
            supportFragmentManager.beginTransaction()
                    .replace(R.id.content_container, fragment)
                    .commit()
        }
    }

    override fun setToolbar() {
        mToolbar.title = getString(R.string.edit_account)
        super.setToolbar()
    }
}