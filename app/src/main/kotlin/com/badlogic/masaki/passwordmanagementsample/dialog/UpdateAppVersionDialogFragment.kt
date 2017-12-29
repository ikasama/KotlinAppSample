package com.badlogic.masaki.passwordmanagementsample.dialog

import android.app.Dialog
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.support.v7.app.AlertDialog
import com.badlogic.masaki.passwordmanagementsample.R

/**
 * Created by masaki on 2017/11/19.
 */
class UpdateAppVersionDialogFragment : DialogFragment() {
    var mPositiveListener: OnPositiveButtonClickListener? = null
    var mNegativeListener:OnNegativeButtonClickListener? = null

    interface OnPositiveButtonClickListener {
        fun onDialogPositiveButtonClick()
    }

    interface OnNegativeButtonClickListener {
        fun onDialogNegativeButtonClick()
    }

    companion object {
        @JvmStatic
        fun newInstance(listener: OnPositiveButtonClickListener?) : UpdateAppVersionDialogFragment {
            val f = UpdateAppVersionDialogFragment()
            f.mPositiveListener = listener
            return f
        }

        @JvmStatic
        fun newInstance(positiveListener: OnPositiveButtonClickListener?,
                        negativeListener: OnNegativeButtonClickListener?) : UpdateAppVersionDialogFragment {
            val f = UpdateAppVersionDialogFragment()
            f.mPositiveListener = positiveListener
            f.mNegativeListener = negativeListener
            return f
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        retainInstance = true
        return AlertDialog.Builder(activity)
                .setTitle(getString(R.string.version_update))
                .setMessage(getString(R.string.confirm_version_update))
                .setPositiveButton(getString(R.string.ok), { _, _ -> mPositiveListener?.onDialogPositiveButtonClick() })
                .setNegativeButton(getString(R.string.cancel), {_, _ -> mNegativeListener?.onDialogNegativeButtonClick() })
                .create()
    }

    override fun onDestroy() {
        if (dialog != null && retainInstance) {
            dialog.setDismissMessage(null)
        }
        super.onDestroy()
    }
}