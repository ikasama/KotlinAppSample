package com.badlogic.masaki.passwordmanagementsample.exception

/**
 * Created by masaki on 2017/10/27.
 */
class PMSqlException(override val message: String? = "sql error occurred") : Exception(message) {
}