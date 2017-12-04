package com.badlogic.masaki.passwordmanagementsample.dialog

import android.app.Dialog
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AlertDialog
import com.badlogic.masaki.passwordmanagementsample.R
import com.badlogic.masaki.passwordmanagementsample.constants.ExtraCode.EXTRA_ACCOUNT
import com.badlogic.masaki.passwordmanagementsample.room.pojo.AccountImage

/**
 * Created by masaki on 2017/11/19.
 */
class DeleteAccountDialogFragment : BaseDialogFragment() {
    companion object {
        @JvmStatic
        fun newInstance(target: Fragment, account: AccountImage, requestCode: Int): DeleteAccountDialogFragment {
            val f = DeleteAccountDialogFragment()
            f.setTargetFragment(target, requestCode)
            val args = Bundle()
            args.putParcelable(EXTRA_ACCOUNT, account)
            f.arguments = args
            return f
        }
    }

    override fun prepareDialog(): Dialog {
        val intent = if (arguments != null) Intent().putExtras(arguments) else null
        val listener = DialogInterface.OnClickListener { _, which ->
            dismiss()
            targetFragment?.let { it.onActivityResult(targetRequestCode, which, intent) }
        }
        return AlertDialog.Builder(activity)
                .setTitle(R.string.confirmation)
                .setMessage(R.string.confirm_delete_account)
                .setPositiveButton(getString(R.string.ok), listener)
                .setNegativeButton(getString(R.string.cancel), listener)
                .create()
    }
}