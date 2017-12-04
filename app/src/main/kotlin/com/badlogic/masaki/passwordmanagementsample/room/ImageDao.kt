package com.badlogic.masaki.passwordmanagementsample.room

import android.arch.persistence.room.*

/**
 * Created by masaki on 2017/11/12.
 */
@Dao
interface ImageDao {
    @Insert(onConflict = OnConflictStrategy.FAIL)
    fun insertImage(image: Image): Long

    @Update(onConflict = OnConflictStrategy.FAIL)
    fun update(image: Image): Int

    @Query("DELETE FROM Image WHERE imageId = :imageId")
    fun deleteById(imageId: Int): Int
}