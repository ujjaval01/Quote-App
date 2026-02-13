package com.uv.quotecomposeapp

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.core.content.ContextCompat
import androidx.core.view.WindowCompat
import com.uv.quotecomposeapp.notification.scheduleDailyNotifications
import com.uv.quotecomposeapp.ui.screens.SplashScreen
import com.uv.quotecomposeapp.ui.theme.QuoteComposeAppTheme
import com.uv.quotecomposeapp.utils.checkForAppUpdate
import com.uv.quotecomposeapp.viewmodel.QuoteViewModel

class MainActivity : ComponentActivity() {

    private val viewModel: QuoteViewModel by viewModels()

    // ✅ Modern Permission Launcher
    private val notificationPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted ->
            if (isGranted) {
                scheduleDailyNotifications(this)
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        WindowCompat.setDecorFitsSystemWindows(window, false)

        // 🔔 Handle Notification Permission
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {

            if (ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.POST_NOTIFICATIONS
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                scheduleDailyNotifications(this)
            } else {
                notificationPermissionLauncher.launch(
                    Manifest.permission.POST_NOTIFICATIONS
                )
            }

        } else {
            scheduleDailyNotifications(this)
        }

        setContent {

            val isDark by viewModel.isDarkMode.observeAsState(false)
            var showSplash by rememberSaveable { mutableStateOf(true) }

            QuoteComposeAppTheme(darkTheme = isDark) {
                checkForAppUpdate(this)
                if (showSplash) {
                    SplashScreen(onNavigate = { showSplash = false })
                } else{
                    MainScreen(viewModel)
                }
            }
        }
    }
}
