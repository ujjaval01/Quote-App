package com.uv.quotecomposeapp.viewmodel

import android.app.Application
import androidx.lifecycle.*
import androidx.work.*
import com.uv.quotecomposeapp.data.*
import com.uv.quotecomposeapp.data.datastore.ThemeDataStore
import com.uv.quotecomposeapp.data.local.*
import kotlinx.coroutines.launch
import java.util.Calendar
import java.util.concurrent.TimeUnit

class QuoteViewModel(application: Application)
    : AndroidViewModel(application) {

    // ---------------- Theme ----------------

    private val themeDataStore = ThemeDataStore(application)

    val isDarkMode = themeDataStore.darkModeFlow.asLiveData()

    fun toggleTheme(isDark: Boolean) {
        viewModelScope.launch {
            themeDataStore.saveTheme(isDark)
        }
    }

    // ---------------- Repository ----------------

    private val repository = QuoteRepository(application)

    val allQuotes = repository.loadQuotes(application)

    val favorites = repository.getFavorites().asLiveData()

    fun toggleFavorite(quote: Quote) {

        viewModelScope.launch {

            val entity = QuoteEntity(
                text = quote.text,
                author = quote.author,
                category = quote.category
            )

            val currentList = favorites.value ?: emptyList()

            if (currentList.any { it.text == quote.text }) {
                repository.removeFavorite(entity)
            } else {
                repository.addFavorite(entity)
            }
        }
    }

    fun removeFavorite(entity: QuoteEntity) {
        viewModelScope.launch {
            repository.removeFavorite(entity)
        }
    }

}
