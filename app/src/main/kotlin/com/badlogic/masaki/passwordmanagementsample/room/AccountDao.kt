package com.badlogic.masaki.passwordmanagementsample.room

import android.arch.persistence.room.*
import com.badlogic.masaki.passwordmanagementsample.room.pojo.AccountImage
import io.reactivex.Single

/**
 * Created by masaki on 2017/11/12.
 */
@Dao
interface AccountDao {

    @Query("""
        SELECT a.accountId, a.accountName, a.password, a.userId, a.categoryId,
               a.iconId, a.comment, i.url, a.createdAt, a.updatedAt
        FROM Account a LEFT OUTER JOIN Image i
        ON a.iconId = i.imageId
        WHERE a.userId = :userId
        """
    )
    fun findAll(userId: String): Single<MutableList<AccountImage>>

    @Query("""
        SELECT a.accountId, a.accountName, a.password, a.userId, a.categoryId,
               a.iconId, a.comment, i.url, a.createdAt, a.updatedAt
        FROM Account a LEFT OUTER JOIN Image i
        ON a.iconId = i.imageId
        WHERE a.userId = :userId
        AND a.categoryId = :categoryId
        """
    )
    fun findByCategoryId(userId: String, categoryId: Int): Single<MutableList<AccountImage>>

    @Insert(onConflict = OnConflictStrategy.FAIL)
    fun insert(account: Account): Long

    @Update(onConflict = OnConflictStrategy.FAIL)
    fun update(account: Account): Int

    @Delete
    fun delete(account: Account): Int

    @Query("""
        DELETE FROM Account WHERE accountId = :accountId
        """)
    fun deleteById(accountId: Int): Int
}