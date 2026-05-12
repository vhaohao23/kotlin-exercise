package com.example.lab10.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.lab10.ui.ProductDetailViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductDetailScreen(
    viewModel: ProductDetailViewModel,
    productId: Int,
    onNavigateBack: () -> Unit
) {
    val formState = viewModel.formState
    val isNew = productId == -1

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(if (isNew) "Thêm sản phẩm" else "Chỉnh sửa sản phẩm") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Quay lại")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                )
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            OutlinedTextField(
                value = formState.name,
                onValueChange = viewModel::updateName,
                label = { Text("Tên sản phẩm *") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )
            OutlinedTextField(
                value = formState.category,
                onValueChange = viewModel::updateCategory,
                label = { Text("Danh mục *") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )
            OutlinedTextField(
                value = formState.price,
                onValueChange = viewModel::updatePrice,
                label = { Text("Giá (VNĐ) *") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                isError = formState.price.isNotBlank() && formState.price.toDoubleOrNull() == null,
                supportingText = {
                    if (formState.price.isNotBlank() && formState.price.toDoubleOrNull() == null)
                        Text("Giá không hợp lệ")
                }
            )
            OutlinedTextField(
                value = formState.quantity,
                onValueChange = viewModel::updateQuantity,
                label = { Text("Số lượng *") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                isError = formState.quantity.isNotBlank() && formState.quantity.toIntOrNull() == null,
                supportingText = {
                    if (formState.quantity.isNotBlank() && formState.quantity.toIntOrNull() == null)
                        Text("Số lượng không hợp lệ")
                }
            )
            OutlinedTextField(
                value = formState.description,
                onValueChange = viewModel::updateDescription,
                label = { Text("Mô tả") },
                modifier = Modifier.fillMaxWidth(),
                minLines = 3,
                maxLines = 5
            )

            Spacer(modifier = Modifier.height(8.dp))

            Button(
                onClick = { viewModel.saveProduct(onDone = onNavigateBack) },
                enabled = formState.isValid(),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
            ) {
                Text(if (isNew) "Thêm sản phẩm" else "Lưu thay đổi",
                    style = MaterialTheme.typography.titleMedium)
            }
        }
    }
}
