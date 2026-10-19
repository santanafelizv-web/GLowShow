package com.sanvalero.glowshop.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "products")
data class ProductEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val name: String,
    val brand: String,
    val category: String,
    val priceCents: Long,
    val stock: Int,
    val minimumStock: Int,
    val active: Boolean = true
)
