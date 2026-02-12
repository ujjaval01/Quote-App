package com.uv.quotecomposeapp
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.*
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import com.uv.quotecomposeapp.ui.BottomNavigationBar
import com.uv.quotecomposeapp.ui.screens.FavoritesScreen
import com.uv.quotecomposeapp.ui.screens.HomeScreen
import com.uv.quotecomposeapp.ui.screens.QuotesScreen
import com.uv.quotecomposeapp.ui.screens.SettingsScreen
import com.uv.quotecomposeapp.viewmodel.QuoteViewModel
import androidx.compose.runtime.getValue
import com.uv.quotecomposeapp.ui.screens.AboutScreen
import com.uv.quotecomposeapp.ui.screens.PrivacyPolicyScreen
import com.uv.quotecomposeapp.ui.screens.QuoteDetailScreen
import android.net.Uri



@Composable
fun MainScreen(viewModel1: QuoteViewModel) {

    val navController = rememberNavController()

    // ViewModel create hoga yaha
    val viewModel: QuoteViewModel = viewModel()
    val favorites by viewModel.favorites.observeAsState(emptyList())


    Scaffold(
        bottomBar = {
            BottomNavigationBar(
                navController = navController,
                favoriteCount = favorites.size   // ✅ yaha change
            )
        }
    ) { paddingValues ->

        NavHost(
            navController = navController,
            startDestination = "home",
            modifier = Modifier.padding(paddingValues)
        ) {

            composable("home") {
                HomeScreen(navController, viewModel)
            }

            composable("quotes") {
                QuotesScreen(null, viewModel, navController)
            }

            composable("quotes/{category}") { backStackEntry ->
                val category =
                    backStackEntry.arguments?.getString("category")
                QuotesScreen(category, viewModel, navController)
            }

            composable("favorites") {
                FavoritesScreen(viewModel, navController)
            }

            composable("settings") {
                SettingsScreen(
                    navController = navController,
                    viewModel = viewModel1
                )
            }

            // ✅ ADD THESE TWO
            composable("about") {
                AboutScreen(navController)
            }

            composable("privacy") {
                PrivacyPolicyScreen(navController)
            }

            // for quote full detail screen
            composable(
                "detail/{text}/{author}"
            ) { backStackEntry ->

                val text =
                    backStackEntry.arguments?.getString("text") ?: ""

                val author =
                    backStackEntry.arguments?.getString("author") ?: ""

                QuoteDetailScreen(

                    text = Uri.decode(text),
                    author = Uri.decode(author),
                    navController = navController
                )
            }


        }

    }
}
