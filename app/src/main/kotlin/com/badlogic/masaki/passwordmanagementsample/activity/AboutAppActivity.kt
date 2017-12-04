package com.badlogic.masaki.passwordmanagementsample.activity

import android.os.Bundle
import android.view.View
import com.badlogic.masaki.passwordmanagementsample.R
import com.badlogic.masaki.passwordmanagementsample.fragment.AboutAppFragment

/**
 * Created by masaki on 2017/12/02.
 */
class AboutAppActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initViews(savedInstanceState)
    }

    private fun initViews(savedInstanceState: Bundle?) {
        mFloatingActionButton.visibility = View.GONE

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                    .replace(R.id.content_container, AboutAppFragment.newInstance())
                    .commit()
        }
    }

    override fun setToolbar() {
        mToolbar.title = getString(R.string.about_this_app)
        super.setToolbar()
    }
}