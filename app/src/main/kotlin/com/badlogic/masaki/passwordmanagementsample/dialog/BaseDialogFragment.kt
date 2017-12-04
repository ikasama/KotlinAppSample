package com.badlogic.masaki.passwordmanagementsample.dialog

import android.app.Dialog
import android.os.Bundle
import android.support.v4.app.DialogFragment

/**
 * Created by masaki on 2017/11/01.
 */
open abstract class BaseDialogFragment: DialogFragment() {
    open abstract fun prepareDialog(): Dialog

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        super.onCreateDialog(savedInstanceState)
        retainInstance = true
        return prepareDialog()
    }

    override fun onDestroy() {
        if (dialog !== null && retainInstance) {
            dialog.setDismissMessage(null)
        }
        super.onDestroy()
    }
}