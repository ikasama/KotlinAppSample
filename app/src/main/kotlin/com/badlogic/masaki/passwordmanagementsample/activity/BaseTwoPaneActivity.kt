package com.badlogic.masaki.passwordmanagementsample.activity

import android.content.Intent
import android.os.Bundle
import android.support.design.widget.CollapsingToolbarLayout
import android.support.design.widget.FloatingActionButton
import android.support.design.widget.NavigationView
import android.support.v7.widget.Toolbar
import android.view.MenuItem
import android.widget.FrameLayout
import com.badlogic.masaki.passwordmanagementsample.R
import com.trello.rxlifecycle2.components.support.RxAppCompatActivity
import kotterknife.bindView

/**
 * Created by masaki on 2017/10/28.
 */
open abstract class BaseTwoPaneActivity: RxAppCompatActivity() {
    protected val mToolbar: Toolbar by bindView(R.id.toolbar)
    protected val mCollapsingToolbarLayout: CollapsingToolbarLayout by bindView(R.id.collapsingToolbarLayout)
    protected val mContentFrame: FrameLayout by bindView(R.id.content_container)
    protected val mFloatingActionButton: FloatingActionButton by bindView(R.id.fab)
    protected val mNavigationView: NavigationView by bindView(R.id.navigation_view)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_two_pane_with_drawer)
        setToolbar()
        mNavigationView.setNavigationItemSelectedListener {
            when(it.itemId) {
                R.id.menu_home ->{
                    val i = Intent(this, CategoryListActivity::class.java)
                    i.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    startActivity(i)
                }
                R.id.menu_about_this_app -> {
                    startActivity(Intent(this, AboutAppActivity::class.java))
                }
                else -> false
            }
            false
        }
    }

    open protected fun setToolbar() {
        mCollapsingToolbarLayout.isTitleEnabled = false
        setSupportActionBar(mToolbar)
        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            setHomeButtonEnabled(true)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        return when (item?.itemId) {
            android.R.id.home -> {
                finish()
                true
            }
            else -> {
                super.onOptionsItemSelected(item)
            }
        }
    }
}