package com.uv.quotecomposeapp
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.*
import androidx.compose.foundation.layout.padding
import androidx.compose.ui.Modifier
import com.uv.quotecomposeapp.ui.BottomNavigationBar
import com.uv.quotecomposeapp.ui.screens.FavoritesScreen
import com.uv.quotecomposeapp.ui.screens.HomeScreen
import com.uv.quotecomposeapp.ui.screens.QuotesScreen
import com.uv.quotecomposeapp.ui.screens.SettingsScreen
import com.uv.quotecomposeapp.viewmodel.QuoteViewModel

@Composable
fun MainScreen() {

    val navController = rememberNavController()

    // ViewModel create hoga yaha
    val viewModel: QuoteViewModel = viewModel()

    Scaffold(
        bottomBar = {
            BottomNavigationBar(navController)
        }
    ) { paddingValues ->

        NavHost(
            navController = navController,
            startDestination = "home",
            modifier = Modifier.padding(paddingValues)
        ) {

            // 🏠 Home
            composable("home") {
                HomeScreen(navController, viewModel)
            }

            // 📜 Quotes (All)
            composable("quotes") {
                QuotesScreen(null, viewModel)
            }

            // 📜 Quotes (Category Filter)
            composable("quotes/{category}") { backStackEntry ->
                val category =
                    backStackEntry.arguments?.getString("category")
                QuotesScreen(category, viewModel)
            }

            // ❤️ Favorites
            composable("favorites") {
                FavoritesScreen(viewModel)
            }

            // ⚙️ Settings
            composable("settings") {
                SettingsScreen()
            }
        }
    }
}
