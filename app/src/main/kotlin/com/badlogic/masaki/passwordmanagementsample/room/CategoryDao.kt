package com.badlogic.masaki.passwordmanagementsample.room

import android.arch.persistence.room.*
import com.badlogic.masaki.passwordmanagementsample.room.pojo.CategoryImage
import io.reactivex.Single

/**
 * Created by masaki on 2017/11/12.
 */
@Dao
interface CategoryDao {
    @Insert(onConflict = OnConflictStrategy.FAIL)
    fun insert(category: Category): Long

    @Update(onConflict = OnConflictStrategy.FAIL)
    fun update(category: Category): Int

    @Delete
    fun delete(category: Category): Int

    @Query("DELETE FROM Category WHERE categoryId = :categoryId")
    fun deleteById(categoryId: Int): Int

    @Query("SELECT c.categoryId, c.categoryName, c.userId, c.iconId, i.url, c.createdAt, c.updatedAt " +
            "FROM Category as c LEFT OUTER JOIN Image as i " +
            "ON :iconId = i.imageId")
    fun findCategoryWithUrl(iconId: Int): Single<MutableList<CategoryImage>>

    @Query("SELECT c.categoryId, c.categoryName, c.userId, c.iconId, i.url, c.createdAt, c.updatedAt " +
            "FROM Category as c LEFT OUTER JOIN Image as i " +
            "ON c.iconId = i.imageId")
    fun findAllCategoryWithUrl(): Single<MutableList<CategoryImage>>

    @Query("SELECT c.categoryId, c.categoryName, c.userId, c.iconId, i.url, c.createdAt, c.updatedAt " +
            "FROM Category as c LEFT OUTER JOIN Image as i " +
            "ON c.iconId = i.imageId " +
            "WHERE c.userId = :userId")
    fun findAllCategoryWithUrlById(userId: String): Single<MutableList<CategoryImage>>
}