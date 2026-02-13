package com.uv.quotecomposeapp.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Save
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.compose.ui.platform.LocalView
import android.view.View
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.viewinterop.AndroidView
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.graphics.luminance


@Composable
fun QuoteDetailScreen(
    text: String,
    author: String,
    navController: NavController
) {

    val context = LocalContext.current
    val view = LocalView.current
    val quoteView = remember { mutableStateOf<View?>(null) }


    val isDark = MaterialTheme.colorScheme.background.luminance() < 0.5f
    val bgGradient = if (isDark) {
        listOf(
            Color(0xFF0F2027),
            Color(0xFF203A43),
            Color(0xFF2C5364)
        )
    } else {
        listOf(
            Color(0xFFff9966),
            Color(0xFFff5e62)
        )
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.radialGradient(
                    colors = if (isDark)
                        listOf(Color(0xFF2C5364), Color(0xFF0F2027))
                    else
                        listOf(Color(0xFFFFE29F), Color(0xFFFF719A)),
                    radius = 900f
                ),
                shape = RoundedCornerShape(6.dp, 6.dp, 0.dp, 0.dp)
            )

            .padding(24.dp)
    ) {

        Column(
            modifier = Modifier.fillMaxSize()
        ) {

            // Top Bar
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {

                IconButton(onClick = { navController.popBackStack() }) {
                    Icon(Icons.Default.ArrowBack, contentDescription = null)
                }

                // THERE DOT(:) MENU BUTTON
//                IconButton(onClick = { /* future menu */ }) {
//                    Icon(Icons.Default.MoreVert, contentDescription = null)
//                }
            }

            Spacer(modifier = Modifier.height(60.dp))

            AndroidView(
                factory = { context ->
                    android.widget.FrameLayout(context).apply {
                        quoteView.value = this
                    }

                },
                modifier = Modifier.fillMaxWidth()
            ) { frameLayout ->

                frameLayout.removeAllViews()

                val composeView = androidx.compose.ui.platform.ComposeView(context).apply {
                    setContent {

                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(
                                    Brush.radialGradient(
                                        colors = if (isDark)
                                            listOf(Color(0xFF2C5364), Color(0xFF0F2027))
                                        else
                                            listOf(Color(0xFFFFE29F), Color(0xFFFF719A)),
                                        radius = 900f
                                    ),
                                    shape = RoundedCornerShape(28.dp)
                                )

                                .padding(24.dp)
                        ){

                            Column {

                                Text(
                                    text = "❝",
                                    fontSize = 60.sp,
                                    color = if (isDark)
                                        Color.White.copy(alpha = 0.4f)
                                    else
                                        Color.Black.copy(alpha = 0.4f)
                                )

                                Text(
                                    text = text,
                                    fontSize = 26.sp,
                                    lineHeight = 36.sp,
                                    fontWeight = FontWeight.Medium
                                )

                                Spacer(modifier = Modifier.height(14.dp))

                                Text(
                                    text = "❞",
                                    fontSize = 60.sp,
                                    color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.4f),
                                            modifier = Modifier.align(
                                        androidx.compose.ui.Alignment.End
                                    )
                                )


                                Text(
                                    text = author.uppercase(),
                                    fontSize = 12.sp,
                                    letterSpacing = 2.sp,
                                    color = if (isDark) Color.LightGray else Color.DarkGray
                                )
                            }
                        }
                    }
                }

                frameLayout.addView(composeView)
            }



            Spacer(modifier = Modifier.weight(1f))

            // Bottom Actions
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {

                IconButton(
                    onClick = {
                        val fullText = "$text\n\n— $author"
                        shareQuote(context, fullText)
                    }
                ) {
                    Icon(Icons.Default.Share, contentDescription = null)
                }


                //  save as image
                IconButton(
                    onClick = {
                        quoteView.value?.let {
                            saveScreenAsImage(context, it)
                        }
                    }
                ) {
                    Icon(Icons.Default.Save, contentDescription = null)
                }
            }
        }
    }
}

