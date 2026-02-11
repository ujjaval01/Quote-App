package com.uv.quotecomposeapp.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.compose.runtime.mutableStateListOf
import com.uv.quotecomposeapp.data.Quote
import com.uv.quotecomposeapp.data.QuoteRepository

class QuoteViewModel(application: Application) : AndroidViewModel(application) {

    val allQuotes = QuoteRepository.loadQuotes(application)

    val favorites = mutableStateListOf<Quote>()

    fun toggleFavorite(quote: Quote) {
        if (favorites.contains(quote)) {
            favorites.remove(quote)
        } else {
            favorites.add(quote)
        }
    }

    fun isFavorite(quote: Quote): Boolean {
        return favorites.contains(quote)
    }
}
