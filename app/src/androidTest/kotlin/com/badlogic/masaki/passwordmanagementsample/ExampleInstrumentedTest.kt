package com.badlogic.masaki.passwordmanagementsample

import android.content.Context
import android.support.test.InstrumentationRegistry
import android.support.test.espresso.Espresso.onView
import android.support.test.espresso.assertion.ViewAssertions.matches
import android.support.test.espresso.matcher.ViewMatchers.withId
import android.support.test.espresso.matcher.ViewMatchers.withText
import android.support.test.rule.ActivityTestRule
import android.support.test.runner.AndroidJUnit4
import com.badlogic.masaki.passwordmanagementsample.activity.LoginActivity

import org.junit.Test
import org.junit.runner.RunWith

import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule

/**
 * Instrumentation test, which will execute on an Android device.

 * @see [Testing documentation](http://d.android.com/tools/testing)
 */
@RunWith(AndroidJUnit4::class)
class ExampleInstrumentedTest {
    @JvmField
    @Rule
    val mActivityRule = ActivityTestRule(LoginActivity::class.java)

    var mActivity: LoginActivity? = null

    var mContext: Context? = null

    @Before
    fun setUp() {
        mActivity = mActivityRule.activity
        mContext = InstrumentationRegistry.getTargetContext()
    }

    @Test
    @Throws(Exception::class)
    fun useAppContext() {
        assertEquals("com.badlogic.masaki.passwordmanagementsample", mContext?.packageName)
    }

    @Test
    fun test() {
        onView(withId(R.id.textView)).check(matches(withText(mContext?.getString(R.string.app_name))))
    }
}
