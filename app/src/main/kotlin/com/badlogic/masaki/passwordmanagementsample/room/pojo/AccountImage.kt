package com.badlogic.masaki.passwordmanagementsample.room.pojo

import android.arch.persistence.room.Ignore
import android.graphics.Bitmap
import android.os.Parcel
import android.os.Parcelable
import com.badlogic.masaki.passwordmanagementsample.util.readBoolean
import com.badlogic.masaki.passwordmanagementsample.util.writeBoolean
import java.util.*

/**
 * Created by masaki on 2017/11/25.
 */
data class AccountImage(
        var accountId: Int? = null,
        var accountName: String? = null,
        var password: String? = null,
        var userId: String? = null,
        var categoryId: Int? = null,
        var iconId: Int? = null,
        var comment: String? = null,
        var url: String? = null,
        var createdAt: Date? = null,
        var updatedAt: Date? = null,
        @Ignore
        var bitmap: Bitmap? = null,
        @Ignore
        var updatesIcon: Boolean = false,
        @Ignore
        var updatesAccount: Boolean = false
) : Parcelable {
        override fun describeContents() = 0

        companion object {
                @JvmField
                val CREATOR: Parcelable.Creator<AccountImage> = object : Parcelable.Creator<AccountImage> {
                        override fun createFromParcel(source: Parcel): AccountImage = source.run {
                                var accountId: Int? = null
                                var accountName: String? = null
                                var password: String? = null
                                var userId: String? = null
                                var categoryId: Int? = null
                                var iconId: Int? = null
                                var comment: String? = null
                                var url: String? = null
                                var createdAt: Date? = null
                                var updatedAt: Date? = null
                                var bitmap: Bitmap? = null
                                var updatesIcon = false
                                var updatesAccount = false

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
                                        comment = readString()
                                }

                                if (hasValue(source)) {
                                        url = readString()
                                }

                                if (hasValue(source)) {
                                        createdAt = Date(readLong())
                                }

                                if (hasValue(source)) {
                                        updatedAt = Date(readLong())
                                }

                                if (hasValue(source)) {
                                        bitmap = readParcelable(Bitmap::class.java.classLoader)
                                }

                                if (hasValue(source)) {
                                        updatesIcon = readBoolean()
                                }

                                if (hasValue(source)) {
                                        updatesAccount = readBoolean()
                                }
                                return AccountImage(accountId, accountName, password, userId,
                                        categoryId, iconId, comment, url, createdAt, updatedAt,
                                        bitmap, updatesIcon, updatesAccount)
                        }

                        private fun hasValue(parcel: Parcel): Boolean = parcel.readByte() !== 0.toByte()

                        override fun newArray(size: Int): Array<AccountImage?> {
                                return arrayOfNulls(size)
                        }
                }
        }

        override fun writeToParcel(dest: Parcel, flags: Int) {
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
                        if(hasValue) writeString(userId)

                        hasValue = categoryId != null
                        writeByte(if (hasValue) 1 else 0)
                        if(hasValue) writeInt(categoryId!!)

                        hasValue = iconId != null
                        writeByte(if (hasValue) 1 else 0)
                        if(hasValue) writeInt(iconId!!)

                        hasValue = comment != null
                        writeByte(if (hasValue) 1 else 0)
                        if(hasValue) writeString(comment!!)

                        hasValue = url != null
                        writeByte(if (hasValue) 1 else 0)
                        if(hasValue) writeString(url!!)

                        hasValue = createdAt != null
                        writeByte(if (hasValue) 1 else 0)
                        if(hasValue) writeLong(createdAt!!.time)

                        hasValue = updatedAt != null
                        writeByte(if (hasValue) 1 else 0)
                        if(hasValue) writeLong(updatedAt!!.time)

                        hasValue = bitmap != null
                        writeByte(if (hasValue) 1 else 0)
                        if(hasValue) writeValue(bitmap)

                        hasValue = updatesIcon != null
                        writeByte(if (hasValue) 1 else 0)
                        if(hasValue) writeBoolean(updatesIcon!!)

                        hasValue = updatesAccount != null
                        writeByte(if (hasValue) 1 else 0)
                        if(hasValue) writeBoolean(updatesAccount!!)
                }
        }
}