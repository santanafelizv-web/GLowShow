package com.sanvalero.glowshop.data

import com.sanvalero.glowshop.data.local.ProductDao
import com.sanvalero.glowshop.data.local.ProductEntity
import com.sanvalero.glowshop.data.local.CustomerDao
import com.sanvalero.glowshop.data.local.CustomerEntity
import kotlinx.coroutines.flow.Flow

class ProductRepository(private val dao: ProductDao) {
    fun products(): Flow<List<ProductEntity>> = dao.observeActive()
    fun lowStock(): Flow<List<ProductEntity>> = dao.observeLowStock()
    suspend fun save(product: ProductEntity) = dao.save(product)
    suspend fun deactivate(id: Long) = dao.deactivate(id)
}

class CustomerRepository(private val dao: CustomerDao) {
    fun customers(): Flow<List<CustomerEntity>> = dao.observeActive()
    suspend fun save(customer: CustomerEntity) = dao.save(customer)
    suspend fun deactivate(id: Long) = dao.deactivate(id)
}
