package com.uv.quotecomposeapp.ui.screens

import android.content.Context
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.Icon
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.vector.ImageVector
import kotlinx.coroutines.delay


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ModalBottomSheet(){

    val context = LocalContext.current
    val prefs = context.getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    var showOnboarding by rememberSaveable {
        mutableStateOf(prefs.getBoolean("first_launch", true))
    }

    if (showOnboarding) {

        val sheetState = rememberModalBottomSheetState(
            skipPartiallyExpanded = true
        )

        ModalBottomSheet(
            sheetState = sheetState,
            onDismissRequest = {
                prefs.edit().putBoolean("first_launch", false).apply()
                showOnboarding = false
            },
            dragHandle = null,
            shape = RoundedCornerShape(topStart = 32.dp, topEnd = 32.dp)
        ) {

            var startAnim by remember { mutableStateOf(false) }

            val alphaAnim by animateFloatAsState(
                targetValue = if (startAnim) 1f else 0f,
                animationSpec = tween(600),
                label = ""
            )

            val slideAnim by animateDpAsState(
                targetValue = if (startAnim) 0.dp else 30.dp,
                animationSpec = tween(600),
                label = ""
            )

            LaunchedEffect(Unit) {
                startAnim = true

                //auto skip onboarding box after 6 sec
//                delay(6000)
//                if(showOnboarding){
//                    prefs.edit().putBoolean("first_launch", false).apply()
//                    showOnboarding = false
//                }
            }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp)
                    .padding(top = 12.dp, bottom = 24.dp)
                    .alpha(alphaAnim)
                    .offset(y = slideAnim)
            ) {

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "Welcome 👋",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(6.dp))

                Text(
                    text = "A simple space for daily inspiration and thoughtful quotes.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                )

                Spacer(modifier = Modifier.height(24.dp))

                OnboardingFeature(
                    icon = Icons.Default.FavoriteBorder,
                    title = "Save What You Love",
                    description = "Double tap any quote to add it to your personal favorites."
                )

                Spacer(modifier = Modifier.height(18.dp))

                OnboardingFeature(
                    icon = Icons.Default.Image,
                    title = "Share Beautifully",
                    description = "Export quotes as elegant images and share them instantly."
                )

                Spacer(modifier = Modifier.height(18.dp))

                OnboardingFeature(
                    icon = Icons.Default.Notifications,
                    title = "Daily Reminders",
                    description = "Get fresh inspiration delivered at 8 AM and 8 PM."
                )

                Spacer(modifier = Modifier.height(28.dp))

                Button(
                    onClick = {
                        prefs.edit().putBoolean("first_launch", false).apply()
                        showOnboarding = false
                    },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(18.dp)
                ) {
                    Text("Start Exploring ✨")
                }

                Spacer(modifier = Modifier.height(6.dp))

                Text(
                    text = "Skip",
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        .clickable {
                            prefs.edit().putBoolean("first_launch", false).apply()
                            showOnboarding = false
                        },
                    color = MaterialTheme.colorScheme.primary
                )

            }
        }
    }

}
@Composable
fun OnboardingFeature(
    icon: ImageVector,
    title: String,
    description: String
) {
    Row(
        verticalAlignment = Alignment.Top
    ) {

        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.primary,
            modifier = Modifier.size(26.dp)
        )

        Spacer(modifier = Modifier.width(14.dp))

        Column {

            Text(
                text = title,
                fontWeight = FontWeight.SemiBold
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = description,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.65f)
            )
        }
    }
}
