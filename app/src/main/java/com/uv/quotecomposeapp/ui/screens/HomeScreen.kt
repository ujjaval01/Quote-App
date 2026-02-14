package com.uv.quotecomposeapp.ui.screens

import android.content.Context
import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Cloud
import androidx.compose.material.icons.filled.EmojiEmotions
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.SelfImprovement
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.uv.quotecomposeapp.loader.DotsLoader
import com.uv.quotecomposeapp.loader.QuoteLoader
import kotlinx.coroutines.delay
import com.uv.quotecomposeapp.viewmodel.QuoteViewModel
import kotlin.random.Random

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    navController: NavController,
    viewModel: QuoteViewModel
) {
    val quotes = viewModel.allQuotes.shuffled().take(5)
    var currentIndex: Int by remember { mutableIntStateOf(0) }
    val context = LocalContext.current

    val prefs = context.getSharedPreferences("app_prefs", Context.MODE_PRIVATE)

    var showOnboarding by rememberSaveable {
        mutableStateOf(prefs.getBoolean("first_launch", true))
    }

//    if(quotes.isEmpty())
//        return

    LaunchedEffect(quotes.size) {
        if (quotes.isNotEmpty()) {
            while (true) {
                delay(3000)
                currentIndex = (currentIndex + 1) % quotes.size
            }
        }
    }


    val currentQuote = quotes[currentIndex]

    // Different gradients for slides
    val gradients = listOf(
        listOf(Color(0xFFff9966), Color(0xFFff5e62)),
        listOf(Color(0xFF36D1DC), Color(0xFF5B86E5)),
        listOf(Color(0xFFDA22FF), Color(0xFF9733EE)),
        listOf(Color(0xFF11998e), Color(0xFF38ef7d)),
        listOf(Color(0xFFFF512F), Color(0xFFDD2476))
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background,
                shape = RoundedCornerShape(topStart = 6.dp, topEnd = 6.dp))
            .padding(16.dp)


    ) {

        Row(verticalAlignment = Alignment.Top) {

            Icon(
                imageVector = Icons.Default.EmojiEmotions,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier
                    .size(36.dp)
                    .padding(top = 4.dp)
            )

            Spacer(modifier = Modifier.width(8.dp))

            Column {
                Text(
                    text = "Daily Quotes",
                    fontSize = 28.sp,
                    fontWeight = FontWeight.ExtraBold,
                    style = MaterialTheme.typography.headlineLarge
                )
                Text(
                    text = "Stay inspired every day ✨",
                    fontSize = 14.sp,
                    color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f)
                )
            }
        }


        Spacer(modifier = Modifier.height(24.dp))

        AnimatedContent(
            targetState = currentQuote,
            transitionSpec = {
                slideInHorizontally { it } + fadeIn() togetherWith
                        slideOutHorizontally { -it } + fadeOut()
            },
            label = ""
        ) { quote ->

            val infiniteTransition = rememberInfiniteTransition(label = "")
            val animatedOffset by infiniteTransition.animateFloat(
                initialValue = 0f,
                targetValue = 1000f,
                animationSpec = infiniteRepeatable(
                    animation = tween(6000, easing = LinearEasing)
                ),
                label = ""
            )

            val scale by animateFloatAsState(
                targetValue = 1f,
                animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy),
                label = ""
            )

            Card(
                shape = RoundedCornerShape(32.dp),
                elevation = CardDefaults.cardElevation(6.dp),
                        modifier = Modifier
                    .fillMaxWidth()
                    .height(230.dp)
                    .scale(scale)
            ) {

                Box(
                    modifier = Modifier
                        .background(
                            Brush.linearGradient(
                                colors = gradients[currentIndex % gradients.size],
                                start = Offset(animatedOffset, 0f),
                                end = Offset(animatedOffset + 800f, 800f)
                            )
                        )
                        .padding(28.dp)
                ) {

                    // 🌟 Big Faded Quote Mark
                    Text(
                        text = "“",
                        fontSize = 120.sp,
                        color = Color.White.copy(alpha = 0.15f),
                        modifier = Modifier.align(Alignment.TopStart)
                    )

                    Column(
                        verticalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier.fillMaxSize()
                    ) {

                        Column {

                            // 🔖 Badge
                            Box(
                                modifier = Modifier
                                    .background(
                                        Color.White.copy(alpha = 0.2f),
                                        RoundedCornerShape(50)
                                    )
                                    .padding(horizontal = 12.dp, vertical = 4.dp)
                                    .align(Alignment.End)

                            ) {
                                Text(
                                    text = "Daily Pick",
                                    color = Color.White,
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }

                            Spacer(modifier = Modifier.height(16.dp))

                            Text(
                                text = quote.text,
                                color = Color.White,
                                fontSize = 19.sp,
                                lineHeight = 28.sp,
                                fontWeight = FontWeight.SemiBold
                            )
                        }

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = "— ${quote.author}",
                                color = Color.White.copy(alpha = 0.9f),
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(top = 2.dp)
                            )

                            IconButton(
                                onClick = {
                                    val fullText =
                                        "${quote.text}\n\n— ${quote.author}"
                                    shareQuote(
                                        context,
                                        fullText
                                    )
                                }
                            ) {
                                Icon(
                                    Icons.Default.Share,
                                    contentDescription = "Share",
                                    tint = Color.White,
                                    modifier = Modifier.padding(bottom = 10.dp)
                                )
                            }
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Dots
        Row(
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxWidth()
        ) {
            repeat(quotes.size) { index ->
                Box(
                    modifier = Modifier
                        .padding(4.dp)
                        .size(if (index == currentIndex) 10.dp else 6.dp)
                        .background(
                            if (index == currentIndex)
                                gradients[currentIndex % gradients.size][1]
                            else
                                Color.Gray.copy(alpha = 0.3f),
                            RoundedCornerShape(50)
                        )
                )
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        Text(
            text = "Categories",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(16.dp))

        // ------------------Category card-------------------

        LazyVerticalGrid(
            modifier = Modifier.weight(1f),
            columns = GridCells.Fixed(2),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {

            items(
                items = categoriesUI,
                key = { it.name } // IMPORTANT for performance
            ) { category ->

                val gradient = remember(category) {
                    Brush.linearGradient(category.colors)
                }

                Card(
                    shape = RoundedCornerShape(20.dp),
                    elevation = CardDefaults.cardElevation(2.dp), // reduced
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(100.dp)
                        .clickable {
                            navController.navigate("quotes/${category.name}")
                        }
                ) {

                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(gradient)
                            .padding(16.dp)
                    ) {

                        Column(
                            modifier = Modifier.fillMaxSize(),
                            verticalArrangement = Arrangement.SpaceBetween
                        ) {

                            Icon(
                                imageVector = category.icon,
                                contentDescription = null,
                                tint = Color.White,
                                modifier = Modifier.size(26.dp)
                            )

                            Text(
                                text = category.name,
                                color = Color.White,
                                style = MaterialTheme.typography.bodyLarge,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                    }
                }
            }
        }

        // when the app launch first time show the onboarding screen
        ModalBottomSheet()

    }
}


// Category card
data class CategoryUI(
    val name: String,
    val icon: ImageVector,
    val colors: List<Color>
)

val categoriesUI = listOf(
    CategoryUI(
        "Motivation",
        Icons.Default.EmojiEvents,
        listOf(Color(0xFFFF512F), Color(0xFFDD2476))
    ),
    CategoryUI(
        "Love",
        Icons.Default.Favorite,
        listOf(Color(0xFFFF9A9E), Color(0xFFFAD0C4))
    ),
    CategoryUI(
        "Sad",
        Icons.Default.Cloud,
        listOf(Color(0xFF4B6CB7), Color(0xFF182848))
    ),
    CategoryUI(
        "Success",
        Icons.Default.Star,
        listOf(Color(0xFFF7971E), Color(0xFFFFD200))
    ),
    CategoryUI(
        "Life",
        Icons.Default.SelfImprovement,
        listOf(Color(0xFF11998E), Color(0xFF38EF7D))
    ),
    CategoryUI(
        "Emotional",
        Icons.Default.EmojiEmotions,
        listOf(Color(0xFF2196F3), Color(0xFFA950B8))
    )
)
