package com.badlogic.masaki.passwordmanagementsample.activity

import android.os.Bundle
import android.view.View
import com.badlogic.masaki.passwordmanagementsample.R
import com.badlogic.masaki.passwordmanagementsample.constants.ExtraCode.EXTRA_CATEGORY
import com.badlogic.masaki.passwordmanagementsample.contract.EditCategoryActivityContract
import com.badlogic.masaki.passwordmanagementsample.fragment.EditCategoryFragment
import com.badlogic.masaki.passwordmanagementsample.room.pojo.CategoryImage


/**
 * Created by masaki on 2017/10/25.
 */
class EditCategoryActivity : BaseActivity(), EditCategoryActivityContract.View {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initViews(savedInstanceState)
    }

    override fun initViews(savedInstanceState: Bundle?) {
        mFloatingActionButton.visibility = View.GONE
        if (savedInstanceState == null) {
            val i = intent
            val c: CategoryImage? = i.getParcelableExtra(EXTRA_CATEGORY)
            val fragment: EditCategoryFragment = EditCategoryFragment.newInstance(c)
            supportFragmentManager.beginTransaction()
                    .replace(R.id.content_container, fragment)
                    .commit()
        }
    }

    override fun setToolbar() {
        mToolbar.title = getString(R.string.edit_category)
        super.setToolbar()
    }
}