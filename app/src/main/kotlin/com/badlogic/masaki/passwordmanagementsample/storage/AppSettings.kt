package com.badlogic.masaki.passwordmanagementsample.storage

import android.content.Context
import android.content.SharedPreferences

/**
 * Created by masaki on 2017/10/27.
 */
object AppSettings {

    private val SHARED_PREFS_NAME = "password_management_sample_preferences"

    fun putString(context: Context?, key: String?, value: String?): Boolean {
        return try {
            getSharedPreferences(context)
                    ?.edit()
                    ?.putString(key, value)
                    ?.apply()
            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    fun getString(context: Context?, key: String?, defaultValue: String? = null): String? {
        return  getSharedPreferences(context)
                ?.getString(key, defaultValue)
    }

    private fun getSharedPreferences(context: Context?): SharedPreferences? {
        return context?.getSharedPreferences(SHARED_PREFS_NAME, Context.MODE_PRIVATE)
    }

    fun putBoolean(context: Context?, key: String, value: Boolean): Boolean? {
        return try {
            getSharedPreferences(context)
                    ?.edit()
                    ?.putBoolean(key, value)
                    ?.apply()
            true
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    fun getBoolean(context: Context?, key: String?, defaultValue: Boolean = false): Boolean? {
        return  getSharedPreferences(context)
                ?.getBoolean(key, defaultValue)
    }
}