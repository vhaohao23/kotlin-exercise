package com.example.lab10.data

import kotlinx.coroutines.flow.Flow

class ProductRepository(private val productDao: ProductDao) {
    fun getAllProducts(): Flow<List<Product>> = productDao.getAllProducts()
    fun getProduct(id: Int): Flow<Product> = productDao.getProduct(id)
    fun searchProducts(query: String): Flow<List<Product>> = productDao.searchProducts(query)

    suspend fun insert(product: Product) = productDao.insert(product)
    suspend fun update(product: Product) = productDao.update(product)
    suspend fun delete(product: Product) = productDao.delete(product)
}
