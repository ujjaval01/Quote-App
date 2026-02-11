package com.uv.quotecomposeapp.data

import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

object QuoteRepository {

    fun loadQuotes(context: Context): List<Quote> {

        val jsonString = context.assets.open("quotes.json")
            .bufferedReader()
            .use { it.readText() }

        val listType = object : TypeToken<List<Quote>>() {}.type

        return Gson().fromJson(jsonString, listType)
    }
}
