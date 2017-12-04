package com.badlogic.masaki.passwordmanagementsample.room

import android.arch.persistence.room.*
import android.graphics.Bitmap
import android.os.Parcel
import android.os.Parcelable
import java.util.*

/**
 * Created by masaki on 2017/11/12.
 */
@Entity(
        indices = arrayOf(
                Index(value = "accountName")
        )
)
data class Account(@PrimaryKey(autoGenerate = true)
                   var accountId: Int? = null,
                   var accountName: String? = null,
                   var password: String? = null,
                   @ForeignKey(
                           entity = User::class,
                           parentColumns = arrayOf("userId"),
                           childColumns = arrayOf("userId"),
                           onDelete = ForeignKey.CASCADE,
                           onUpdate = ForeignKey.CASCADE
                   )
                   var userId: String? = null,
                   @ForeignKey(
                           entity = Category::class,
                           parentColumns = arrayOf("categoryId"),
                           childColumns = arrayOf("categoryId"),
                           onDelete = ForeignKey.SET_NULL,
                           onUpdate = ForeignKey.SET_NULL
                   )
                   var categoryId: Int? = null,
                   @ForeignKey(
                           entity = Image::class,
                           parentColumns = arrayOf("imageId"),
                           childColumns = arrayOf("iconId"),
                           onDelete = ForeignKey.SET_NULL,
                           onUpdate = ForeignKey.SET_NULL
                   )
                   var iconId: Int? = null,
                   var comment: String? = null,
                   var createdAt: Date? = null,
                   var updatedAt: Date? = null) : Parcelable {

    override fun writeToParcel(dest: Parcel?, flags: Int) {
        dest?.apply {
            var hasValue = accountId != null
            writeByte(if (hasValue) 1 else 0)
            if (hasValue) writeInt(accountId!!)

            hasValue = accountName != null
            writeByte(if (hasValue) 1 else 0)
            if(hasValue) writeString(accountName)

            hasValue = password != null
            writeByte(if (hasValue) 1 else 0)
            if(hasValue) writeString(password)

            hasValue = userId != null
            writeByte(if (hasValue) 1 else 0)
            if(hasValue) writeString(userId!!)

            hasValue = categoryId != null
            writeByte(if (hasValue) 1 else 0)
            if(hasValue) writeInt(categoryId!!)

            hasValue = iconId != null
            writeByte(if (hasValue) 1 else 0)
            if(hasValue) writeInt(iconId!!)

            hasValue = comment != null
            writeByte(if (hasValue) 1 else 0)
            if(hasValue) writeString(comment!!)

            hasValue = createdAt != null
            writeByte(if (hasValue) 1 else 0)
            if(hasValue) writeLong(createdAt!!.time)

            hasValue = updatedAt != null
            writeByte(if (hasValue) 1 else 0)
            if(hasValue) writeLong(updatedAt!!.time)
        }
    }

    override fun describeContents() = 0

    companion object {
        @JvmField
        val CREATOR: Parcelable.Creator<Account> = object : Parcelable.Creator<Account> {
            override fun createFromParcel(source: Parcel): Account = source.run {
                var accountId: Int? = null
                var accountName: String? = null
                var password: String? = null
                var userId: String? = null
                var categoryId: Int? = null
                var iconId: Int? = null
                var comment: String? = null
                var createdAt: Date? = null
                var updatedAt: Date? = null

                if (hasValue(source)) {
                    accountId = readInt()
                }

                if (hasValue(source)) {
                    accountName = readString()
                }

                if (hasValue(source)) {
                    password = readString()
                }

                if (hasValue(source)) {
                    userId = readString()
                }

                if (hasValue(source)) {
                    categoryId = readInt()
                }

                if (hasValue(source)) {
                    iconId = readInt()
                }

                if (hasValue(source)) {
                    userId = readString()
                }

                if (hasValue(source)) {
                    iconId = readInt()
                }

                if (hasValue(source)) {
                    comment = readString()
                }

                if (hasValue(source)) {
                    createdAt = Date(readLong())
                }

                if (hasValue(source)) {
                    updatedAt = Date(readLong())
                }

                return Account(accountId, accountName, password, userId, categoryId, iconId, comment, createdAt, updatedAt)
            }

            private fun hasValue(parcel: Parcel): Boolean = parcel.readByte() !== 0.toByte()

            override fun newArray(size: Int): Array<Account?> {
                return arrayOfNulls(size)
            }
        }

        const val INVALID_ID = 0

        fun isValidId(id: Int?): Boolean {
            return when {
                id == null -> false
                id <= INVALID_ID -> false
                else -> true
            }
        }
    }
}