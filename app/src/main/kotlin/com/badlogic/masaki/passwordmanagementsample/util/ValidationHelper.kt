package com.badlogic.masaki.passwordmanagementsample.util

import android.content.Context
import android.view.View
import android.widget.EditText
import com.badlogic.masaki.passwordmanagementsample.R
import com.mobsandgeeks.saripaar.ValidationError
import com.mobsandgeeks.saripaar.rule.NotEmptyRule
import com.mobsandgeeks.saripaar.rule.PasswordRule

/**
 * Created by masaki on 2017/10/27.
 */

tailrec fun showValidationError(context: Context, errors: MutableList<ValidationError>) {
    if (errors.isEmpty()) {
        return Unit
    } else {
        val error: ValidationError? = errors.removeAt(0)
        val msg = error?.failedRules?.let {
            when {
                it.size >= 1 && it[0] is PasswordRule -> {
                    context.getString(R.string.invalid_password)
                }
                it.size >= 1 && it[0] is NotEmptyRule -> {
                    context.getString(R.string.field_required)
                }
                else -> {
                    error?.getCollatedErrorMessage(context)
                }
            }
        }
        val view: View? = error?.view
        when(view) {
            is EditText -> view.error = msg
            else -> return Unit
        }
        showValidationError(context, errors)
    }
}
