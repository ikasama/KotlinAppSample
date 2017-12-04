package com.badlogic.masaki.passwordmanagementsample.room

import android.arch.persistence.room.Database
import android.arch.persistence.room.RoomDatabase
import android.arch.persistence.room.TypeConverters
import com.badlogic.masaki.passwordmanagementsample.room.converter.DateConverter

/**
 * Created by masaki on 2017/11/12.
 */
@Database(
        entities = arrayOf(User::class, Category::class, Account::class, Image::class),
        version = 1,
        exportSchema = false
)
@TypeConverters(DateConverter::class)
abstract class PasswordManagementDatabase : RoomDatabase() {

    abstract fun userDao(): UserDao
    abstract fun categoryDao(): CategoryDao
    abstract fun accountDao(): AccountDao
    abstract fun imageDao(): ImageDao

    companion object {
        const val DB_NAME = "password_management"
    }
}