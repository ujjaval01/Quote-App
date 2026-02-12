package com.uv.quotecomposeapp

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.material3.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.core.view.WindowCompat
import com.uv.quotecomposeapp.viewmodel.QuoteViewModel

class MainActivity : ComponentActivity() {

    private val viewModel: QuoteViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        WindowCompat.setDecorFitsSystemWindows(window, false)

        // 🔔 Schedule notification once
        viewModel.scheduleDailyQuote()

        // 🔔 Android 13+ Notification Permission
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            requestPermissions(
                arrayOf(android.Manifest.permission.POST_NOTIFICATIONS),
                100
            )
        }

        setContent {

            val isDark by viewModel.isDarkMode.observeAsState(false)

            MaterialTheme(
                colorScheme =
                    if (isDark)
                        darkColorScheme()
                    else
                        lightColorScheme()
            ) {
                MainScreen(viewModel)
            }
        }
    }
}
