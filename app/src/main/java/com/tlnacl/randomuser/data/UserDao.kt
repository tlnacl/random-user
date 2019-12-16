package com.tlnacl.randomuser.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface UserDao {
    @Query("SELECT * FROM user")
    suspend fun getAll(): List<User>

    @Insert
    suspend fun insertAll(users: List<User>)

    @Query("SELECT * FROM user WHERE user MATCH :query")
    suspend fun searchAllUser(query: String): List<User>
}