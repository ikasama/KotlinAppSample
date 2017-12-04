package com.badlogic.masaki.passwordmanagementsample.room.converter

import android.arch.persistence.room.TypeConverter
import java.util.*


/**
 * Created by masaki on 2017/11/12.
 */
class DateConverter {
    @TypeConverter
    fun fromTimestamp(value: Long?): Date? {
        return if (value == null) null else Date(value)
    }

    @TypeConverter
    fun dateToTimestamp(date: Date?): Long? {
        return date?.time
    }
}