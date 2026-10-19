package com.sanvalero.glowshop.data.local

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [ProductEntity::class, CustomerEntity::class], version = 1, exportSchema = false)
abstract class GlowShopDatabase : RoomDatabase() {
    abstract fun productDao(): ProductDao
    abstract fun customerDao(): CustomerDao
}
