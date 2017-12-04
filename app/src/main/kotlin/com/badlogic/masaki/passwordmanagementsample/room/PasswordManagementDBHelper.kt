package com.badlogic.masaki.passwordmanagementsample.room

import android.arch.persistence.room.Room
import android.content.Context
import com.badlogic.masaki.passwordmanagementsample.room.PasswordManagementDatabase.Companion.DB_NAME

/**
 * Created by masaki on 2017/11/12.
 */
object PasswordManagementDBHelper {
    private lateinit var mDatabase: PasswordManagementDatabase

    fun init(context: Context) {
        mDatabase = Room.databaseBuilder(context, PasswordManagementDatabase::class.java, DB_NAME).build()
    }
    fun database(context: Context): PasswordManagementDatabase {
        if (mDatabase == null) {
            init(context)
        }
        return mDatabase
    }
}