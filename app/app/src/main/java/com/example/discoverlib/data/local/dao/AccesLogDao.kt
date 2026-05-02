package com.example.discoverlib.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import com.example.discoverlib.data.local.entity.AccessLogEntity

@Dao
interface AccessLogDao {
    @Insert
    suspend fun insertLog(log: AccessLogEntity)
}