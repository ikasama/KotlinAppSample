package com.badlogic.masaki.passwordmanagementsample.fragment

import android.Manifest
import android.os.Bundle
import android.widget.Toast
import com.badlogic.masaki.passwordmanagementsample.R
import com.badlogic.masaki.passwordmanagementsample.proxy.PermissionProxy
import com.badlogic.masaki.passwordmanagementsample.proxy.PermissionProxyImpl

/**
 * Created by masaki on 2017/11/03.
 */
open abstract class BasePermissionFragment: BaseFragment(), PermissionProxy.Callback {
    protected val mPermissionProxy: PermissionProxy by lazy { initProxy() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initProxy()
    }

    private fun initProxy() = PermissionProxyImpl(this)

    override fun onVerifyPermissionGranted(permission: String, requestCode: Int) {
    }

    override fun onVerifyPermissionRevoked(permission: String, requestCode: Int) {
    }

    override fun onAgreePermissionExplanation(permission: String, requestCode: Int) {
        mPermissionProxy.requestPermission(permission, requestCode)
    }

    override fun onDisagreePermissionExplanation(permission: String, requestCode: Int) {
        val msg: String = when {
            permission === Manifest.permission.CAMERA -> {
              getString(R.string.cannot_use_camera_without_perm)
            }
            permission === Manifest.permission.READ_EXTERNAL_STORAGE -> {
              getString(R.string.cannot_read_storage_without_perm)
            }
            else -> "not implemented yet"
        }
        Toast.makeText(activity.applicationContext, msg, Toast.LENGTH_SHORT).show()
    }
}