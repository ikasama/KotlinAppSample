package com.badlogic.masaki.passwordmanagementsample

import android.content.Context
import android.support.test.InstrumentationRegistry
import android.support.test.espresso.Espresso.onView
import android.support.test.espresso.action.ViewActions.click
import android.support.test.rule.ActivityTestRule
import android.support.test.runner.AndroidJUnit4
import com.badlogic.masaki.passwordmanagementsample.activity.AccountListActivity
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import com.badlogic.masaki.passwordmanagementsample.activity.CategoryListActivity
import android.support.test.espresso.matcher.ViewMatchers.*
import org.junit.Assert.assertTrue


/**
 * Created by shojimasaki on 2018/01/20.
 */
@RunWith(AndroidJUnit4::class)
class EspressoTest {

    @JvmField
    @Rule
    val mActivityRule = ActivityTestRule(CategoryListActivity::class.java)

    var mContext: Context? = null
    var mActivity: CategoryListActivity? = null

    @Before
    @Throws(Exception::class)
    fun setUp() {
        mContext = InstrumentationRegistry.getTargetContext()
        mActivity = mActivityRule.activity
    }

    @After
    @Throws(Exception::class)
    fun tearDown() {
        mContext = null
        mActivity = null
    }

    @Test
    @Throws(Exception::class)
    fun testCategoryListToAccountList() {
        onView(withId(R.id.frame_header_category_list)).perform(click())
        onView(isRoot()).perform(waitFor(2000))
        val ac = getActivityInstance()
        assertTrue(ac is AccountListActivity)
    }
}