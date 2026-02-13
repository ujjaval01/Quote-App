package com.uv.quotecomposeapp.ui.screens

import android.app.Activity
import android.content.Intent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.*
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavController
import com.uv.quotecomposeapp.viewmodel.QuoteViewModel
import com.uv.quotecomposeapp.utils.checkForAppUpdate
import com.uv.quotecomposeapp.utils.launchInAppReview

@Composable
fun SettingsScreen(
    navController: NavController,
    viewModel: QuoteViewModel
) {

    val isDarkMode by viewModel.isDarkMode.observeAsState(false)
    val context = LocalContext.current
    val activity = context as Activity

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
    ) {

        // Title
        Text(
            text = "Settings",
            style = MaterialTheme.typography.headlineLarge,
            fontWeight = FontWeight.ExtraBold
        )

        Spacer(modifier = Modifier.height(24.dp))

        // ---------------- Appearance ----------------

        Text(
            text = "Appearance",
            fontWeight = FontWeight.SemiBold
        )

        Spacer(modifier = Modifier.height(12.dp))

        Card(
            shape = RoundedCornerShape(20.dp),
            elevation = CardDefaults.cardElevation(4.dp),
            modifier = Modifier.fillMaxWidth()
        ) {

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {

                Row(verticalAlignment = Alignment.CenterVertically) {

                    Icon(
                        imageVector = Icons.Default.Palette,
                        contentDescription = null
                    )

                    Spacer(modifier = Modifier.width(12.dp))

                    Text("Dark Mode")
                }

                Switch(
                    checked = isDarkMode,
                    onCheckedChange = {
                        viewModel.toggleTheme(it)
                    }
                )
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        // ---------------- Information ----------------

        Text(
            text = "Information",
            fontWeight = FontWeight.SemiBold
        )

        Spacer(modifier = Modifier.height(12.dp))

        SettingItem(
            icon = Icons.Default.Info,
            title = "About App",
            subtitle = "App details and version info"
        ) {
            navController.navigate("about")
        }

        Spacer(modifier = Modifier.height(12.dp))

        SettingItem(
            icon = Icons.Default.PrivacyTip,
            title = "Privacy Policy",
            subtitle = "Read how we protect your data"
        ) {
            navController.navigate("privacy")
        }

        Spacer(modifier = Modifier.height(32.dp))

        // ---------------- Features ----------------

        Text(
            text = "Features",
            fontWeight = FontWeight.SemiBold
        )

        Spacer(modifier = Modifier.height(12.dp))

        SettingItem(
            icon = Icons.Default.StarRate,
            title = "Rate this App ⭐",
            subtitle = "Share your feedback on Play Store"
        ) {
            launchInAppReview(activity)
        }

        Spacer(modifier = Modifier.height(12.dp))

        SettingItem(
            icon = Icons.Default.Update,
            title = "Check for Updates",
            subtitle = "Make sure you have latest version"
        ) {
            checkForAppUpdate(activity)
        }

        Spacer(modifier = Modifier.height(12.dp))

        SettingItem(
            icon = Icons.Default.Share,
            title = "Share App",
            subtitle = "Share with your friends"
        ) {

            val intent = Intent(Intent.ACTION_SEND).apply {
                type = "text/plain"
                putExtra(
                    Intent.EXTRA_TEXT,
                    "Check out this amazing Daily Quotes app!\n\nhttps://play.google.com/store/apps/details?id=${activity.packageName}"
                )
            }

            activity.startActivity(
                Intent.createChooser(intent, "Share via")
            )
        }
    }
}


// ------------------------------------------------------------
// Reusable Setting Item
// ------------------------------------------------------------

@Composable
fun SettingItem(
    icon: ImageVector,
    title: String,
    subtitle: String? = null,
    onClick: () -> Unit
) {
    Card(
        shape = RoundedCornerShape(20.dp),
        elevation = CardDefaults.cardElevation(4.dp),
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
    ) {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            Icon(
                imageVector = icon,
                contentDescription = null
            )

            Spacer(modifier = Modifier.width(12.dp))

            Column {
                Text(
                    text = title,
                    fontWeight = FontWeight.Medium
                )

                subtitle?.let {
                    Text(
                        text = it,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                    )
                }
            }
        }
    }
}
