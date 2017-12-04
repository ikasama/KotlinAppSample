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
                Index(value = "categoryName", unique = true)
        )
)
data class Category(@PrimaryKey(autoGenerate = true)
                    var categoryId: Int? = null,
                    var categoryName: String? = null,
                    @ForeignKey(
                            entity = User::class,
                            parentColumns = arrayOf("userId"),
                            childColumns = arrayOf("userId"),
                            onDelete = ForeignKey.CASCADE,
                            onUpdate = ForeignKey.CASCADE
                    )
                    var userId: String? = null,
                    @ForeignKey(
                            entity = Image::class,
                            parentColumns = arrayOf("imageId"),
                            childColumns = arrayOf("iconId"),
                            onDelete = ForeignKey.SET_NULL,
                            onUpdate = ForeignKey.SET_NULL
                    )
                    var iconId: Int? = null,
                    var createdAt: Date? = null,
                    var updatedAt: Date? = null
                    ) : Parcelable {
    override fun writeToParcel(dest: Parcel?, flags: Int) {
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
        val CREATOR: Parcelable.Creator<Category> = object : Parcelable.Creator<Category> {
            override fun createFromParcel(source: Parcel): Category = source.run {
                var categoryId: Int? = null
                var categoryName: String? = null
                var userId: String? = null
                var iconId: Int? = null
                var createdAt: Date? = null
                var updatedAt: Date? = null

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
                    createdAt = Date(readLong())
                }

                if (hasValue(source)) {
                    updatedAt = Date(readLong())
                }

                return Category(categoryId, categoryName, userId, iconId, createdAt, updatedAt)
            }

            private fun hasValue(parcel: Parcel): Boolean = parcel.readByte() !== 0.toByte()

            override fun newArray(size: Int): Array<Category?> {
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