package com.badlogic.masaki.passwordmanagementsample.activity

import android.content.Intent
import android.os.Bundle
import android.support.design.widget.CoordinatorLayout
import android.view.Gravity
import android.view.View
import com.badlogic.masaki.passwordmanagementsample.R
import com.badlogic.masaki.passwordmanagementsample.contract.CategoryListActivityContract
import com.badlogic.masaki.passwordmanagementsample.fragment.CategoryListFragment


/**
 * Created by masaki on 2017/10/27.
 */
class CategoryListActivity : BaseActivity(), CategoryListActivityContract.View {

    companion object {
        val TAG: String = CategoryListActivity::class.java.simpleName
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setUpViews()
        initFragment(savedInstanceState)
    }

    override fun setUpViews() {
        mFloatingActionButton.visibility = View.VISIBLE

        val params: CoordinatorLayout.LayoutParams = mFloatingActionButton.layoutParams as CoordinatorLayout.LayoutParams
        params.anchorId = R.id.listView
        params.anchorGravity = Gravity.BOTTOM or Gravity.RIGHT or Gravity.END
        mFloatingActionButton.layoutParams = params

        mFloatingActionButton.setOnClickListener { _ ->
            startActivity(Intent(this, EditCategoryActivity::class.java))
        }

    }

    override fun initFragment(savedInstanceState: Bundle?) {
        if (savedInstanceState == null) {
            val fragment: CategoryListFragment = CategoryListFragment.newInstance()
            supportFragmentManager.beginTransaction()
                    .replace(R.id.content_container, fragment)
                    .commit()
        }
    }

    override fun setToolbar() {
        mToolbar.title = getString(R.string.category_list)
        super.setToolbar()
    }
}