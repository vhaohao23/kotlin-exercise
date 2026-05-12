package com.example.lab10.ui

import androidx.compose.runtime.*
import androidx.lifecycle.*
import com.example.lab10.data.Product
import com.example.lab10.data.ProductRepository
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

data class ProductFormState(
    val name: String = "",
    val category: String = "",
    val price: String = "",
    val quantity: String = "",
    val description: String = ""
) {
    fun isValid() = name.isNotBlank() && category.isNotBlank() &&
            price.toDoubleOrNull() != null && quantity.toIntOrNull() != null

    fun toProduct(id: Int = 0) = Product(
        id = id,
        name = name.trim(),
        category = category.trim(),
        price = price.toDouble(),
        quantity = quantity.toInt(),
        description = description.trim()
    )
}

class ProductDetailViewModel(
    private val repository: ProductRepository,
    private val productId: Int
) : ViewModel() {
    var formState by mutableStateOf(ProductFormState())
        private set

    init {
        if (productId != -1) {
            viewModelScope.launch {
                val product = repository.getProduct(productId).filterNotNull().first()
                formState = ProductFormState(
                    name = product.name,
                    category = product.category,
                    price = product.price.toString(),
                    quantity = product.quantity.toString(),
                    description = product.description
                )
            }
        }
    }

    fun updateName(v: String) { formState = formState.copy(name = v) }
    fun updateCategory(v: String) { formState = formState.copy(category = v) }
    fun updatePrice(v: String) { formState = formState.copy(price = v) }
    fun updateQuantity(v: String) { formState = formState.copy(quantity = v) }
    fun updateDescription(v: String) { formState = formState.copy(description = v) }

    fun saveProduct(onDone: () -> Unit) {
        if (!formState.isValid()) return
        viewModelScope.launch {
            if (productId == -1) repository.insert(formState.toProduct())
            else repository.update(formState.toProduct(id = productId))
            onDone()
        }
    }

    companion object {
        fun factory(repository: ProductRepository, productId: Int): ViewModelProvider.Factory =
            object : ViewModelProvider.Factory {
                @Suppress("UNCHECKED_CAST")
                override fun <T : ViewModel> create(modelClass: Class<T>): T =
                    ProductDetailViewModel(repository, productId) as T
            }
    }
}
