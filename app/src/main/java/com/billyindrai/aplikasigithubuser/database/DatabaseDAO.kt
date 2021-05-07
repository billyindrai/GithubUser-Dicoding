package com.billyindrai.aplikasigithubuser.database

import android.database.Cursor
import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.billyindrai.aplikasigithubuser.User

@Dao
interface DatabaseDAO {
    @Insert
    suspend fun insert(user: User)

    @Query("delete from favorite where username = :username")
    suspend fun delete(username: String)

    @Query("select * from favorite ORDER BY name DESC")
    fun getAllUsers(): LiveData<List<User>>

    @Query("select * from favorite where username = :username")
    fun findUser(username: String) : LiveData<User>

    @Query("SELECT * FROM favorite ORDER BY name DESC")
    fun findUsers(): Cursor
}