package com.uv.quotecomposeapp.data

import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.uv.quotecomposeapp.data.local.*
import kotlinx.coroutines.flow.Flow

class QuoteRepository(context: Context) {

    private val database = QuoteDatabase.getDatabase(context)
    private val dao = database.quoteDao()

    // JSON load
    fun loadQuotes(context: Context): List<Quote> {

        val jsonString = context.assets.open("quotes.json")
            .bufferedReader()
            .use { it.readText() }

        val listType = object : TypeToken<List<Quote>>() {}.type

        return Gson().fromJson(jsonString, listType)
    }

    // Room functions
    fun getFavorites(): Flow<List<QuoteEntity>> =
        dao.getAllFavorites()

    suspend fun addFavorite(entity: QuoteEntity) =
        dao.insertFavorite(entity)

    suspend fun removeFavorite(entity: QuoteEntity) =
        dao.deleteFavorite(entity)
}
