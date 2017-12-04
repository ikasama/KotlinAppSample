package com.badlogic.masaki.passwordmanagementsample.util

import android.content.Context
import android.view.View
import android.widget.EditText
import com.mobsandgeeks.saripaar.ValidationError

/**
 * Created by masaki on 2017/10/27.
 */

tailrec fun showValidationError(context: Context, errors: MutableList<ValidationError>) {
    if (errors.isEmpty()) {
        return Unit
    } else {
        val error: ValidationError? = errors.removeAt(0)
        val view: View? = error?.view
        when(view) {
            is EditText -> view.error = error.getCollatedErrorMessage(context)
            else -> return Unit
        }
        showValidationError(context, errors)
    }
}
