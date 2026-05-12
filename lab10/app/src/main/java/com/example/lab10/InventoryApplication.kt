package com.example.lab10

import android.app.Application
import com.example.lab10.data.InventoryDatabase
import com.example.lab10.data.ProductRepository

class InventoryApplication : Application() {
    val database by lazy { InventoryDatabase.getDatabase(this) }
    val repository by lazy { ProductRepository(database.productDao()) }
}
