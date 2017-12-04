package com.badlogic.masaki.passwordmanagementsample.util

/**
 * Created by masaki on 2017/10/25.
 */

fun assertion(isValid: Boolean, message: String? = null): Boolean {
    when (isValid) {
        false -> throw AssertionError(message ?: "Assertion failed.")
        else -> return true
    }
}