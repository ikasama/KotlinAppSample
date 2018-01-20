package com.badlogic.masaki.passwordmanagementsample

import android.app.Activity
import android.support.test.InstrumentationRegistry.getInstrumentation
import android.support.test.espresso.UiController
import android.support.test.espresso.ViewAction
import android.support.test.espresso.matcher.ViewMatchers.isRoot
import android.support.test.runner.lifecycle.ActivityLifecycleMonitorRegistry
import android.support.test.runner.lifecycle.Stage
import android.view.View
import org.hamcrest.Matcher

/**
 * Created by shojimasaki on 2018/01/20.
 */
fun getActivityInstance(): Activity? {
    var ac: Activity? = null
    getInstrumentation().runOnMainSync {
        val resumedActivities = ActivityLifecycleMonitorRegistry.getInstance().getActivitiesInStage(Stage.RESUMED)
        if (resumedActivities.iterator().hasNext()) {
            ac = resumedActivities.iterator().next()
        }
    }

    return ac
}

fun waitFor(millis: Long): ViewAction {
    return object: ViewAction {
        override fun getDescription(): String {
            return "wait for " + millis + "for action"
        }

        override fun getConstraints(): Matcher<View> = isRoot()

        override fun perform(uiController: UiController?, view: View?) {
            uiController?.loopMainThreadForAtLeast(millis)
        }
    }
}
