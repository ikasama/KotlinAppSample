package com.badlogic.masaki.passwordmanagementsample.fragment

import android.Manifest
import android.app.Dialog
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.support.v7.app.AlertDialog
import com.badlogic.masaki.passwordmanagementsample.R
import com.badlogic.masaki.passwordmanagementsample.proxy.PermissionProxy

/**
 * Created by masaki on 2017/10/27.
 */
class PermissionDialogFragment: DialogFragment() {
    private var mPermissions: String? = null
    private var mRequestCode: Int? = null
    var mCallback: PermissionProxy.Callback? = null

    companion object {
        private val KEY_PERMISSION = "permission"
        private val KEY_REQUEST_CODE = "requestCode"

        @JvmStatic
        fun newInstance(permissions: String? = null,
                        requestCode: Int? = null,
                        callback: PermissionProxy.Callback) : PermissionDialogFragment {
            requireNotNull(permissions) {"permission must not be null."}
            requireNotNull(requestCode) {"requestCode must not be null."}

            val f = PermissionDialogFragment()
            val args = Bundle()
            args.putString(KEY_PERMISSION, permissions)
            args.putInt(KEY_REQUEST_CODE, requestCode!!)
            f.arguments = args
            f.mCallback = callback
            return f
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        retainInstance = true
        mPermissions = arguments.getString(KEY_PERMISSION)
        mRequestCode = arguments.getInt(KEY_REQUEST_CODE)

        if (mPermissions === Manifest.permission.CAMERA) {
            return AlertDialog.Builder(activity)
                    //.setTitle(getString(R.string.explanation))
                    .setMessage(getString(R.string.need_camera))
                    .setPositiveButton(getString(R.string.ok), { _, _ -> mCallback?.onAgreePermissionExplanation(mPermissions!!, mRequestCode!!) })
                    .setNegativeButton(getString(R.string.cancel), { _, _ -> mCallback?.onDisagreePermissionExplanation(mPermissions!!, mRequestCode!!) })
                    .create()
        } else {
            //暫定対処
            return AlertDialog.Builder(activity)
                    //.setTitle("Explanation")
                    .setMessage(getString(R.string.need_camera))
                    .setPositiveButton(getString(R.string.ok), { _, _ -> mCallback?.onAgreePermissionExplanation(mPermissions!!, mRequestCode!!) })
                    .setNegativeButton(getString(R.string.cancel), { _, _ -> })
                    .create()

        }
    }

    override fun onDestroy() {
        if (dialog != null && retainInstance) {
            dialog.setDismissMessage(null)
        }
        super.onDestroy()
    }
}