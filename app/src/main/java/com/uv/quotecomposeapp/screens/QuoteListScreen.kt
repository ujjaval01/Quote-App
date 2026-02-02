package com.uv.quotecomposeapp.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.uv.quotecomposeapp.model.Quote

@Composable
fun QuoteListScreen(
    data: Array<Quote>,
    onClick: (Quote) -> Unit
) {
    Box {
        Column {

            Text(
                text = "Quotes App",
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .padding(top = 24.dp, bottom = 8.dp)
                    .fillMaxWidth(),
                style = MaterialTheme.typography.titleMedium,
                fontFamily = FontFamily.Monospace
            )

            QuoteList(
                data = data,
                onClick = { quote ->
                    onClick(quote)
                }
            )
        }
    }
}
