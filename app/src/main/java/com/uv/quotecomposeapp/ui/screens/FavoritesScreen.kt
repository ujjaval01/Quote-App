package com.uv.quotecomposeapp.ui.screens

import android.content.*
import android.net.Uri
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.uv.quotecomposeapp.viewmodel.QuoteViewModel

@Composable
fun FavoritesScreen(
    viewModel: QuoteViewModel,
    navController: NavController
) {

    val context = LocalContext.current
    val favorites by viewModel.favorites.observeAsState(emptyList())


    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {

        Text(
            text = "Your Favorites ❤️",
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(20.dp))

        if (favorites.isEmpty()) {

            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "No favorites yet 😔\nStart liking quotes!",
                    style = MaterialTheme.typography.bodyLarge
                )
            }

        } else {

            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {

                items(favorites, key = { it.text }) { quote ->

                    Card(
                        shape = RoundedCornerShape(24.dp),
                        elevation = CardDefaults.cardElevation(6.dp),
                        modifier = Modifier.fillMaxWidth()
                            .clickable(){
                                val encodedText =
                                    Uri.encode(quote.text)

                                val encodedAuthor =
                                    Uri.encode(quote.author)

                                navController.navigate(
                                    "detail/$encodedText/$encodedAuthor"
                                )
                            }
                    ) {

                        Box(
                            modifier = Modifier
                                .background(
                                    Brush.verticalGradient(
                                        colors = listOf(
                                            MaterialTheme.colorScheme.primary.copy(alpha = 0.1f),
                                            MaterialTheme.colorScheme.surface
                                        )
                                    )
                                )
                                .padding(20.dp)
                        ) {

                            Column {

                                Text(
                                    text = "“${quote.text}”",
                                    fontSize = 18.sp
                                )

                                Spacer(modifier = Modifier.height(12.dp))

                                Text(
                                    text = "— ${quote.author}",
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.primary
                                )

                                Spacer(modifier = Modifier.height(16.dp))

                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {

                                    // ❤️ Remove
                                    IconButton(
                                        onClick = {
                                            viewModel.removeFavorite(quote)
                                        }
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.Delete,
                                            contentDescription = null,
                                            tint = MaterialTheme.colorScheme.error
                                        )
                                    }


                                    Row {

                                        // 📤 Share
                                        IconButton(
                                            onClick = {
                                                shareQuote(
                                                    context,
                                                    "${quote.text} - ${quote.author}"
                                                )
                                            }
                                        ) {
                                            Icon(
                                                imageVector = Icons.Default.Share,
                                                contentDescription = null
                                            )
                                        }

                                        // 📋 Copy
                                        IconButton(
                                            onClick = {
                                                copyToClipboard(
                                                    context,
                                                    "${quote.text} - ${quote.author}"
                                                )
                                            }
                                        ) {
                                            Icon(
                                                imageVector = Icons.Default.ContentCopy,
                                                contentDescription = null
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

private fun copyToClipboard(context: Context, text: String) {
    val clipboard =
        context.getSystemService(Context.CLIPBOARD_SERVICE)
                as ClipboardManager

    val clip = ClipData.newPlainText("quote", text)
    clipboard.setPrimaryClip(clip)

    Toast.makeText(context, "Copied", Toast.LENGTH_SHORT).show()
}


