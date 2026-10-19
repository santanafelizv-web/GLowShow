package com.sanvalero.glowshop.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface CustomerDao {
    @Query("SELECT * FROM customers WHERE active = 1 ORDER BY name COLLATE NOCASE")
    fun observeActive(): Flow<List<CustomerEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun save(customer: CustomerEntity): Long

    @Query("UPDATE customers SET active = 0 WHERE id = :id")
    suspend fun deactivate(id: Long)
}
