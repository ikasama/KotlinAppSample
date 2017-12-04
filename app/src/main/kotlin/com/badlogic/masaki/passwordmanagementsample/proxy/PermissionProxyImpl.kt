package com.badlogic.masaki.passwordmanagementsample.proxy

import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.support.v4.app.ActivityCompat
import android.support.v4.app.Fragment
import android.support.v4.content.PermissionChecker
import android.support.v7.app.AppCompatActivity
import com.badlogic.masaki.passwordmanagementsample.fragment.PermissionDialogFragment

/**
 * Created by masaki on 2017/10/27.
 */
class PermissionProxyImpl(private val mFragment: Fragment) : PermissionProxy {

    override fun verifyPermission(permission: String, requestCode: Int, callback: PermissionProxy.Callback) {
        if (hasPermissions(mFragment.context!!, arrayOf(permission))) {
            callback.onVerifyPermissionGranted(permission, requestCode)
        } else {
            requestPermissionIfNeeded(permission, requestCode, callback)
        }
    }

    private fun requestPermissionIfNeeded(permission: String, requestCode: Int, callback: PermissionProxy.Callback) {
        if (mFragment.shouldShowRequestPermissionRationale(permission)) {
            PermissionDialogFragment
                    .newInstance(permission, requestCode, callback)
                    .show(mFragment.activity.supportFragmentManager,null)
        } else {
            mFragment.requestPermissions(arrayOf(permission), requestCode)
        }
    }

    override fun requestPermission(permission: String, requestCode: Int) {
        mFragment.requestPermissions(arrayOf(permission), requestCode)
    }

    private fun hasPermissions(context: Context, permissions: Array<String>): Boolean {
        return when (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            true -> permissions.foldRight(true) { value, acc ->
                acc && (PermissionChecker.checkSelfPermission(context, value) === PackageManager.PERMISSION_GRANTED)
            }
            else -> true
        }
    }

    override fun checkGrantResults(grantResults: IntArray): Boolean {
        return grantResults.foldRight(true) { grantResult, acc ->
            acc && (grantResult === PackageManager.PERMISSION_GRANTED) }
    }
}