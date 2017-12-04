package com.badlogic.masaki.passwordmanagementsample.room

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import java.util.*

/**
 * Created by masaki on 2017/11/12.
 */
@Entity
data class Image(@PrimaryKey(autoGenerate = true)
                 var imageId: Int? = null,
                 var url: String,
                 var createdAt: Date? = null,
                 var updatedAt: Date? = null)
