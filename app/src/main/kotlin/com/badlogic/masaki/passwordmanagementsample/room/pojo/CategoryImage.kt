package com.badlogic.masaki.passwordmanagementsample.room.pojo

import android.arch.persistence.room.Ignore
import android.graphics.Bitmap
import android.os.Parcel
import android.os.Parcelable
import com.badlogic.masaki.passwordmanagementsample.room.Category
import com.badlogic.masaki.passwordmanagementsample.util.readBoolean
import com.badlogic.masaki.passwordmanagementsample.util.writeBoolean
import java.util.*

/**
 * Created by masaki on 2017/11/18.
 */
data class CategoryImage(var categoryId: Int? = null,
                         var categoryName: String? = null,
                         var userId: String? = null,
                         var iconId: Int? = null,
                         var url: String? = null,
                         var createdAt: Date? = null,
                         var updatedAt: Date? = null,
                         @Ignore
                         var bitmap: Bitmap? = null,
                         @Ignore
                         var updatesIcon: Boolean = false,
                         @Ignore
                         var updatesCategory: Boolean = false
) : Parcelable {

    override fun describeContents() = 0

    companion object {
        @JvmField
        val CREATOR: Parcelable.Creator<CategoryImage> = object : Parcelable.Creator<CategoryImage> {
            override fun createFromParcel(source: Parcel): CategoryImage = source.run {
                var categoryId: Int? = null
                var categoryName: String? = null
                var userId: String? = null
                var iconId: Int? = null
                var url: String? = null
                var createdAt: Date? = null
                var updatedAt: Date? = null
                var bitmap: Bitmap? = null
                var updatesIcon = false
                var updatesCategory = false

                if (hasValue(source)) {
                    categoryId = readInt()
                }

                if (hasValue(source)) {
                    categoryName = readString()
                }

                if (hasValue(source)) {
                    userId = readString()
                }

                if (hasValue(source)) {
                    iconId = readInt()
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
                    updatesCategory = readBoolean()
                }
                return CategoryImage(categoryId, categoryName, userId, iconId, url, createdAt, updatedAt, bitmap, updatesIcon, updatesCategory)
            }

            private fun hasValue(parcel: Parcel): Boolean = parcel.readByte() !== 0.toByte()

            override fun newArray(size: Int): Array<CategoryImage?> {
                return arrayOfNulls(size)
            }
        }
    }

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest?.apply {
            var hasValue = categoryId != null
            writeByte(if (hasValue) 1 else 0)
            if (hasValue) writeInt(categoryId!!)

            hasValue = categoryName != null
            writeByte(if (hasValue) 1 else 0)
            if(hasValue) writeString(categoryName)

            hasValue = userId != null
            writeByte(if (hasValue) 1 else 0)
            if(hasValue) writeString(userId)

            hasValue = iconId != null
            writeByte(if (hasValue) 1 else 0)
            if(hasValue) writeInt(iconId!!)

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

            hasValue = updatesCategory != null
            writeByte(if (hasValue) 1 else 0)
            if(hasValue) writeBoolean(updatesCategory!!)
        }
    }
}
