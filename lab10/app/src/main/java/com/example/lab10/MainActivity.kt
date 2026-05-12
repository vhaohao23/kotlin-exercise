package com.example.lab10

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.*
import androidx.navigation.navArgument
import com.example.lab10.ui.ProductDetailViewModel
import com.example.lab10.ui.ProductListViewModel
import com.example.lab10.ui.screens.ProductDetailScreen
import com.example.lab10.ui.screens.ProductListScreen
import com.example.lab10.ui.theme.Lab10Theme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Lab10Theme {
                InventoryApp()
            }
        }
    }
}

@Composable
fun InventoryApp() {
    val app = androidx.compose.ui.platform.LocalContext.current.applicationContext as InventoryApplication
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "product_list") {
        composable("product_list") {
            val viewModel: ProductListViewModel = viewModel(
                factory = ProductListViewModel.factory(app.repository)
            )
            ProductListScreen(
                viewModel = viewModel,
                onAddProduct = { navController.navigate("product_detail/-1") },
                onProductClick = { id -> navController.navigate("product_detail/$id") }
            )
        }
        composable(
            route = "product_detail/{productId}",
            arguments = listOf(navArgument("productId") { type = NavType.IntType })
        ) { backStackEntry ->
            val productId = backStackEntry.arguments?.getInt("productId") ?: -1
            val viewModel: ProductDetailViewModel = viewModel(
                factory = ProductDetailViewModel.factory(app.repository, productId)
            )
            ProductDetailScreen(
                viewModel = viewModel,
                productId = productId,
                onNavigateBack = { navController.popBackStack() }
            )
        }
    }
}
