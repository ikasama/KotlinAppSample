package com.badlogic.masaki.passwordmanagementsample.room

import android.arch.persistence.room.*
import io.reactivex.Single

/**
 * Created by masaki on 2017/11/12.
 */
@Dao
interface UserDao {
    @Insert(onConflict = OnConflictStrategy.FAIL)
    fun insert(user: User): Long

    @Update(onConflict = OnConflictStrategy.FAIL)
    fun update(user: User): Int

    @Delete
    fun delete(user: User): Int

    @Query("SELECT COUNT(*) AS 'result' " +
            "FROM User " +
            "WHERE ${User.COL_USER_ID} = :userId " +
            "AND password = :password")
    fun authenticate(userId: String, password: String): Single<Int>

    @Query("SELECT COUNT(*) FROM User")
    fun hasRecord(): Int

    @Query("SELECT * FROM USER")
    fun findAll(): Single<List<User>>
}