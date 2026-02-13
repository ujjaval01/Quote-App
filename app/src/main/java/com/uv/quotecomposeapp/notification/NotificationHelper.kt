package com.uv.quotecomposeapp.notification

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.uv.quotecomposeapp.MainActivity
import com.uv.quotecomposeapp.R
import com.uv.quotecomposeapp.data.Quote

object NotificationHelper {

    private const val CHANNEL_ID = "daily_quote_channel"
    private const val CHANNEL_NAME = "Daily Quotes"

    fun showDailyQuote(context: Context) {

        val quote = getRandomQuote(context)

        createNotificationChannel(context)

        val intent = Intent(context, MainActivity::class.java)
        intent.flags =
            Intent.FLAG_ACTIVITY_NEW_TASK or
                    Intent.FLAG_ACTIVITY_CLEAR_TASK

        val pendingIntent = PendingIntent.getActivity(
            context,
            0,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or
                    PendingIntent.FLAG_IMMUTABLE
        )

        val notification = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle("Daily Quote 💡")
            .setContentText("“${quote.text}” — ${quote.author}")
            .setStyle(
                NotificationCompat.BigTextStyle()
                    .bigText("“${quote.text}”\n\n— ${quote.author}")
            )
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .build()

        val manager =
            context.getSystemService(Context.NOTIFICATION_SERVICE)
                    as NotificationManager

        manager.notify(System.currentTimeMillis().toInt(), notification)
    }

    private fun createNotificationChannel(context: Context) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            val channel = NotificationChannel(
                CHANNEL_ID,
                CHANNEL_NAME,
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = "Daily motivational quotes"
            }

            val manager =
                context.getSystemService(Context.NOTIFICATION_SERVICE)
                        as NotificationManager

            manager.createNotificationChannel(channel)
        }
    }

    private fun getRandomQuote(context: Context): Quote {

        val jsonString = context.assets
            .open("quotes.json")
            .bufferedReader()
            .use { it.readText() }

        val listType =
            object : TypeToken<List<Quote>>() {}.type

        val quotes: List<Quote> =
            Gson().fromJson(jsonString, listType)

        return quotes.random()
    }
}
