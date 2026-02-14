package com.uv.quotecomposeapp.ui.screens

import android.annotation.SuppressLint
import android.content.Context
import android.view.View
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Save
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.graphics.*
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.navigation.NavController
import android.app.Activity
import android.content.pm.ActivityInfo


@SuppressLint("ContextCastToActivity")
@Composable
fun QuoteDetailScreen(
    text: String,
    author: String,
    navController: NavController
) {

    val context = LocalContext.current
    val quoteView = remember { mutableStateOf<View?>(null) }


    val isDark = MaterialTheme.colorScheme.background.luminance() < 0.5f

    val backgroundBrush = Brush.radialGradient(
        colors = if (isDark)
            listOf(Color(0xFF1F1C2C), Color(0xFF000000))
        else
            listOf(Color(0xFFFFE29F), Color(0xFFFF719A)),
        radius = 900f
    )

    val iconColor = if (isDark) Color.White else Color.Black

    val activity = LocalContext.current as Activity

    DisposableEffect(Unit) {
        activity.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        onDispose {
            activity.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED
        }
    }


    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundBrush)
            .padding(24.dp)
    ) {

        Column(
            modifier = Modifier.fillMaxSize()
        ) {

            // 🔙 Top Bar
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Start
            ) {

                IconButton(onClick = { navController.popBackStack() }) {
                    Icon(
                        Icons.Default.ArrowBack,
                        contentDescription = "Back",
                        tint = iconColor
                    )
                }
            }

            Spacer(modifier = Modifier.height(40.dp))

            // 📌 Quote Card (Only this will be saved)
            AndroidView(
                factory = { context ->
                    android.widget.FrameLayout(context).apply {
                        quoteView.value = this
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            ) { frameLayout ->

                frameLayout.removeAllViews()

                val composeView = androidx.compose.ui.platform.ComposeView(context).apply {
                    setContent {

                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(
                                    Brush.linearGradient(
                                        colors = if (isDark)
                                            listOf(Color(0xFF2C3E50), Color(0xFF000000))
                                        else
                                            listOf(Color(0xFFff9966), Color(0xFFff5e62))
                                    ),
                                    shape = RoundedCornerShape(28.dp)
                                )
                                .padding(28.dp)
                        ) {

                            Column(
                                verticalArrangement = Arrangement.SpaceBetween,
                                modifier = Modifier.fillMaxSize()
                            ) {

                                Text(
                                    text = "❝",
                                    fontSize = 70.sp,
                                    color = Color.White.copy(alpha = 0.3f)
                                )

                                Text(
                                    text = text,
                                    fontSize = 26.sp,
                                    lineHeight = 36.sp,
                                    fontWeight = FontWeight.Medium,
                                    color = Color.White
                                )

                                Column {
                                    Text(
                                        text = "❞",
                                        fontSize = 70.sp,
                                        color = Color.White.copy(alpha = 0.3f),
                                        modifier = Modifier.align(Alignment.End)
                                    )

                                    Text(
                                        text = author.uppercase(),
                                        fontSize = 13.sp,
                                        letterSpacing = 2.sp,
                                        color = Color.White.copy(alpha = 0.9f)
                                    )
                                }
                            }
                        }
                    }
                }

                frameLayout.addView(composeView)
            }

            Spacer(modifier = Modifier.height(20.dp))

            // 🔥 Bottom Action Bar (Premium Glass Look)
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        Color.Black.copy(alpha = if (isDark) 0.3f else 0.15f),
                        RoundedCornerShape(20.dp)
                    )
                    .padding(horizontal = 24.dp, vertical = 12.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {

                IconButton(
                    onClick = {
                        val fullText = "$text\n\n— $author"
                        shareQuote(context, fullText)
                    }
                ) {
                    Icon(
                        Icons.Default.Share,
                        contentDescription = "Share",
                        tint = Color.White
                    )
                }

                IconButton(
                    onClick = {
                        quoteView.value?.let {
                            saveScreenAsImage(context, it)
                        }
                    }
                ) {
                    Icon(
                        Icons.Default.Save,
                        contentDescription = "Save",
                        tint = Color.White
                    )
                }
            }
        }
    }
}
