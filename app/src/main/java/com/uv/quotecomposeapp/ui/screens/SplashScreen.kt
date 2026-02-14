package com.uv.quotecomposeapp.ui.screens

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.uv.quotecomposeapp.loader.WaveDotsLoader
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(onNavigate: () -> Unit) {

    var startAnimation by remember { mutableStateOf(false) }

    val alphaAnim by animateFloatAsState(
        targetValue = if (startAnimation) 1f else 0f,
        animationSpec = tween(1200),
        label = ""
    )

    val scaleAnim by animateFloatAsState(
        targetValue = if (startAnimation) 1f else 0.8f,
        animationSpec = tween(1200),
        label = ""
    )

    var showLoader by remember { mutableStateOf(true) }
    LaunchedEffect(true) {
        startAnimation = true
        delay(1500)
        onNavigate()
        showLoader = false

    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    listOf(
                        Color(0xFF0F2027),
                        Color(0xFF203A43),
                        Color(0xFF2C5364)
                    )
                )
            ),
        contentAlignment = Alignment.Center
    ) {

        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Text(
                text = "Daily Quotes",
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                modifier = Modifier
                    .alpha(alphaAnim)
                    .scale(scaleAnim)
            )

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = "Start Your Day Inspired ✨",
                fontSize = 14.sp,
                color = Color.White.copy(alpha = 0.8f),
                modifier = Modifier.alpha(alphaAnim)
            )

            Spacer(modifier = Modifier.height(22.dp))
            if(showLoader){
                WaveDotsLoader()
            }
        }
    }
}
