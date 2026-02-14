package com.uv.quotecomposeapp.ui.screens

import android.content.*
import android.net.Uri
import android.widget.Toast
import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.pullrefresh.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.*
import androidx.compose.ui.draw.scale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.uv.quotecomposeapp.loader.DotsLoader
import com.uv.quotecomposeapp.viewmodel.QuoteViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterialApi::class, ExperimentalAnimationApi::class)
@Composable
fun QuotesScreen(
    category: String?,
    viewModel: QuoteViewModel,
    navController: NavController
) {

    val listState = rememberLazyListState()
    val favorites by viewModel.favorites.observeAsState(emptyList())
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()

    var refreshKey by remember { mutableStateOf(0) }
    var refreshing by remember { mutableStateOf(false) }

    var searchQuery by remember { mutableStateOf("") }
    var isSearchExpanded by remember { mutableStateOf(false) }

    // ---------------- FILTER LOGIC ----------------

    val filteredQuotes = remember(
        viewModel.allQuotes,
        category,
        refreshKey,
        searchQuery
    ) {

        val baseList =
            if (category.isNullOrEmpty())
                viewModel.allQuotes
            else
                viewModel.allQuotes.filter { it.category == category }

        val searchedList =
            if (searchQuery.isBlank())
                baseList
            else
                baseList.filter {
                    it.text.contains(searchQuery, true)
                            ||
                            it.author.contains(searchQuery, true)
                }

        searchedList.shuffled()
    }

    LaunchedEffect(searchQuery) {
        listState.scrollToItem(0)
    }

    val pullRefreshState = rememberPullRefreshState(
        refreshing = refreshing,
        onRefresh = {
            refreshing = true
            coroutineScope.launch {
                delay(600)
                refreshKey++
                listState.scrollToItem(0)
                refreshing = false
            }
        }
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {

        // ---------------- HEADER ----------------

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {

            if (!isSearchExpanded) {

                Text(
                    text = "All Quotes ❤️",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.ExtraBold,
                    style = MaterialTheme.typography.headlineLarge
                )

                Spacer(modifier = Modifier.weight(1f))

                IconButton(
                    onClick = { isSearchExpanded = true }
                ) {
                    Icon(Icons.Default.Search, contentDescription = null)
                }

            } else {

                AnimatedVisibility(
                    visible = isSearchExpanded,
                    enter = expandHorizontally() + fadeIn(),
                    exit = shrinkHorizontally() + fadeOut()
                ) {

                    OutlinedTextField(
                        value = searchQuery,
                        onValueChange = { searchQuery = it },
                        modifier = Modifier.fillMaxWidth(),
                        placeholder = { Text("Search author or keyword...") },
                        singleLine = true,
                        shape = RoundedCornerShape(20.dp),
                        leadingIcon = {
                            Icon(Icons.Default.Search, null)
                        },
                        trailingIcon = {
                            IconButton(
                                onClick = {
                                    searchQuery = ""
                                    isSearchExpanded = false
                                }
                            ) {
                                Icon(Icons.Default.Close, null)
                            }
                        }
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        if (viewModel.allQuotes.isEmpty()) {

            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                DotsLoader()
            }

        } else {

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .pullRefresh(pullRefreshState)
            ) {

                if (filteredQuotes.isEmpty()) {

                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "No matching quotes found 😔",
                            style = MaterialTheme.typography.bodyLarge
                        )
                    }

                } else {

                    LazyColumn(
                        state = listState,
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {

                        items(
                            items = filteredQuotes,
                            key = { it.text }
                        ) { quote ->

                            val isLiked =
                                favorites.any { it.text == quote.text }

                            val scale by animateFloatAsState(
                                targetValue = if (isLiked) 1.3f else 1f,
                                animationSpec = spring(
                                    dampingRatio = Spring.DampingRatioMediumBouncy
                                )
                            )

                            Card(
                                shape = RoundedCornerShape(24.dp),
                                elevation = CardDefaults.cardElevation(6.dp),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable {

                                        val encodedText =
                                            Uri.encode(quote.text)

                                        val encodedAuthor =
                                            Uri.encode(quote.author)

                                        navController.navigate(
                                            "detail/$encodedText/$encodedAuthor"
                                        )
                                    }
                            ) {

                                Column(
                                    modifier = Modifier.padding(20.dp)
                                ) {

                                    Text(
                                        text = "“${quote.text}”",
                                        fontSize = 18.sp,
                                        lineHeight = 26.sp
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
                                        horizontalArrangement =
                                            Arrangement.SpaceBetween
                                    ) {

                                        Row {

                                            IconButton(
                                                onClick = {
                                                    val fullText =
                                                        "${quote.text}\n\n— ${quote.author}"
                                                    copyToClipboard(
                                                        context,
                                                        fullText
                                                    )
                                                }
                                            ) {
                                                Icon(
                                                    Icons.Default.ContentCopy,
                                                    null
                                                )
                                            }

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
                                                    null
                                                )
                                            }
                                        }

                                        IconButton(
                                            onClick = {
                                                viewModel.toggleFavorite(quote)
                                            }
                                        ) {
                                            Icon(
                                                imageVector =
                                                    if (isLiked)
                                                        Icons.Default.Favorite
                                                    else
                                                        Icons.Default.FavoriteBorder,
                                                contentDescription = null,
                                                tint =
                                                    if (isLiked)
                                                        MaterialTheme.colorScheme.primary
                                                    else
                                                        MaterialTheme.colorScheme.onSurface,
                                                modifier = Modifier.scale(scale)
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                }

                PullRefreshIndicator(
                    refreshing = refreshing,
                    state = pullRefreshState,
                    modifier = Modifier.align(Alignment.TopCenter)
                )
            }
        }
    }
}

fun shareQuote(context: Context, text: String) {
    val intent = Intent(Intent.ACTION_SEND)
    intent.type = "text/plain"
    intent.putExtra(Intent.EXTRA_TEXT, text)
    context.startActivity( Intent.createChooser(intent, "Share via")
    )
}
