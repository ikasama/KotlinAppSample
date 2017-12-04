package com.badlogic.masaki.passwordmanagementsample.proxy

import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity

/**
 * Created by masaki on 2017/10/27.
 */
interface PermissionProxy {
    interface Callback {
        fun onVerifyPermissionGranted(permission: String, requestCode: Int)
        fun onVerifyPermissionRevoked(permission: String, requestCode: Int)
        fun onAgreePermissionExplanation(permission: String, requestCode: Int)
        fun onDisagreePermissionExplanation(permission: String, requestCode: Int)
    }

    fun verifyPermission(permission: String, requestCode: Int, callback: Callback)
    fun requestPermission(permission: String, requestCode: Int)
    fun checkGrantResults(grantResults: IntArray): Boolean
}