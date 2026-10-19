package com.sanvalero.glowshop.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "customers")
data class CustomerEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val name: String,
    val phone: String,
    val email: String = "",
    val address: String = "",
    val document: String = "",
    val active: Boolean = true
)
