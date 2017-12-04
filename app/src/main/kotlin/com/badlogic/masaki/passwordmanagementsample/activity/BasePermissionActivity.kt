package com.badlogic.masaki.passwordmanagementsample.activity

import android.Manifest
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.Toast
import com.badlogic.masaki.passwordmanagementsample.R
import com.badlogic.masaki.passwordmanagementsample.fragment.EditCategoryFragment
import com.badlogic.masaki.passwordmanagementsample.proxy.PermissionProxy
import com.badlogic.masaki.passwordmanagementsample.proxy.PermissionProxyImpl

/**
 * Created by masaki on 2017/10/27.
 */
open abstract class BasePermissionActivity(open protected val mPermissionProxy: PermissionProxyImpl): BaseActivity(),
                                                                PermissionProxy.Callback,
                                                                PermissionProxy by mPermissionProxy {
    constructor(): this(PermissionProxyImpl(EditCategoryFragment()))

    override fun onVerifyPermissionGranted(permission: String, requestCode: Int) {
    }

    override fun onVerifyPermissionRevoked(permission: String, requestCode: Int) {
    }

    override fun onAgreePermissionExplanation(permission: String, requestCode: Int) {
        requestPermission( permission, requestCode)
    }

    override fun onDisagreePermissionExplanation(permission: String, requestCode: Int) {
        //show error
        val msg: String = when {
            permission === Manifest.permission.CAMERA ->  {
                getString(R.string.cannot_use_camera_without_perm)
            }
            permission === Manifest.permission.READ_EXTERNAL_STORAGE ->  {
                getString(R.string.cannot_read_storage_without_perm)
            }
            else -> "not implemented yet"
        }
        Toast.makeText(this.applicationContext, msg, Toast.LENGTH_SHORT).show()
    }

}