package com.badlogic.masaki.passwordmanagementsample.util

import android.content.Context
import android.os.Build
import android.content.pm.PackageManager
import android.support.v4.app.ActivityCompat
import android.support.v4.content.PermissionChecker
import android.support.v7.app.AppCompatActivity
import com.badlogic.masaki.passwordmanagementsample.fragment.PermissionDialogFragment
import com.badlogic.masaki.passwordmanagementsample.proxy.PermissionProxy


/**
 * Created by masaki on 2017/10/27.
 */
fun requestPermissionsIfNeeded(activity: AppCompatActivity, permission: String,
                       requestCode: Int, callback: PermissionProxy.Callback) {
    if (!hasPermissions(activity, arrayOf(permission))) {

    }

    // Should we show an explanation?
    if (ActivityCompat.shouldShowRequestPermissionRationale(activity,
            permission)) {

        // Show an expanation to the user *asynchronously* -- don't block
        // this thread waiting for the user's response! After the user
        // sees the explanation, try again to request the permission.

        PermissionDialogFragment
                .newInstance(permission, requestCode, callback)
                .show(activity.supportFragmentManager, "hoge")
    } else {

        // No explanation needed, we can request the permission.
        ActivityCompat.requestPermissions(activity, arrayOf(permission), requestCode)
        // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
        // app-defined int constant. The callback method gets the
        // result of the request.
    }
}

fun hasPermissions(context: Context, permissions: Array<String>): Boolean {
    return when (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        true -> permissions.foldRight(true) { value, acc ->
            acc && (PermissionChecker.checkSelfPermission(context, value) === PackageManager.PERMISSION_GRANTED)
        }
        else -> true
    }
}

fun checkGrantResults(grantResults: IntArray): Boolean {
    return grantResults.foldRight(true) { grantResult, acc ->
        acc && (grantResult === PackageManager.PERMISSION_GRANTED) }
}
