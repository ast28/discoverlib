package com.example.discoverlib.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.discoverlib.data.local.entity.UserEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDao {
    @Query("SELECT * FROM users")
    fun getUsers() : Flow<List<UserEntity>>

    @Query("SELECT * FROM users where id = :userId")
    suspend fun getOneUser(userId: String) : UserEntity?

    @Insert
    suspend fun addUser(user: UserEntity)

    @Update
    suspend fun updateUser(user: UserEntity)

    @Query("DELETE FROM users WHERE id = :userId")
    suspend fun deleteUser(userId: String)


    @Query("SELECT COUNT(*) FROM users WHERE username = :username")
    suspend fun isUsernameInUse(username: String): Int
}