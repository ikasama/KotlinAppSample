package com.badlogic.masaki.passwordmanagementsample.room

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import java.util.*

/**
 * Created by masaki on 2017/11/12.
 */
@Entity
data class User(@PrimaryKey(autoGenerate = false) var userId: String,
                var nickname: String? = null,
                var password: String? = null,
                var isLoggedIn: Int? = null,
                var createdAt: Date? = null,
                var updatedAt: Date? = null
                ) {

    companion object {
        const val COL_USER_ID: String = "userId"
    }
}