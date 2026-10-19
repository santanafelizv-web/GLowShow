package com.sanvalero.glowshop.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface ProductDao {
    @Query("SELECT * FROM products WHERE active = 1 ORDER BY name COLLATE NOCASE")
    fun observeActive(): Flow<List<ProductEntity>>

    @Query("SELECT * FROM products WHERE active = 1 AND stock <= minimumStock ORDER BY name")
    fun observeLowStock(): Flow<List<ProductEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun save(product: ProductEntity): Long

    @Query("UPDATE products SET active = 0 WHERE id = :id")
    suspend fun deactivate(id: Long)
}
