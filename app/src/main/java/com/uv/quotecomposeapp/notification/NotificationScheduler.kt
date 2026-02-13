package com.uv.quotecomposeapp.notification

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import java.util.*

fun scheduleDailyNotifications(context: Context) {

    val alarmManager =
        context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

    scheduleAlarm(context, alarmManager, 8, 0, 100)
    scheduleAlarm(context, alarmManager, 20, 0, 101)
}

private fun scheduleAlarm(
    context: Context,
    alarmManager: AlarmManager,
    hour: Int,
    minute: Int,
    requestCode: Int
) {

    val intent = Intent(context, DailyQuoteReceiver::class.java)

    val pendingIntent = PendingIntent.getBroadcast(
        context,
        requestCode,
        intent,
        PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
    )

    val calendar = Calendar.getInstance().apply {
        set(Calendar.HOUR_OF_DAY, hour)
        set(Calendar.MINUTE, minute)
        set(Calendar.SECOND, 0)

        // test(notification in 1 min after fresh installing)
//        add(Calendar.MINUTE, 1)


        if (before(Calendar.getInstance())) {
            add(Calendar.DAY_OF_MONTH, 1)
        }
    }


    alarmManager.setRepeating(
        AlarmManager.RTC_WAKEUP,
        calendar.timeInMillis,
        AlarmManager.INTERVAL_DAY,
        pendingIntent
    )



}
