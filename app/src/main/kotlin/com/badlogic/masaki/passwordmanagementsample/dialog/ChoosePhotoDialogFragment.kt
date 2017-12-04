package com.badlogic.masaki.passwordmanagementsample.dialog

import android.app.Dialog
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.support.v7.app.AlertDialog
import com.badlogic.masaki.passwordmanagementsample.R

/**
 * Created by masaki on 2017/11/01.
 */
class ChoosePhotoDialogFragment: DialogFragment() {
    open interface Callback {
        fun onBootCameraFromDialog()
        fun onBootGalleryFromDialog()
    }

    var mCallback: Callback? = null

    companion object {
        @JvmStatic
        fun newInstance(callback: Callback): ChoosePhotoDialogFragment {
            val fragment = ChoosePhotoDialogFragment()
            fragment.mCallback = callback
            return fragment
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        retainInstance = true
        return AlertDialog.Builder(activity)
                .setTitle(getString(R.string.set_icon))
                .setItems(arrayOf(getString(R.string.boot_camera),
                        getString(R.string.select_from_library))) { _, which ->
                    when (which) {
                        0 -> mCallback?.onBootCameraFromDialog()
                        1 -> mCallback?.onBootGalleryFromDialog()
                        else -> {}
                    }
                }
                .create()
    }

    override fun onDestroy() {
        if (dialog != null && retainInstance) {
            dialog.setDismissMessage(null)
        }
        super.onDestroy()
    }
}