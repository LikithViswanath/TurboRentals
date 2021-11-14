package com.example.turborentals.alarmDisbatcher

import android.app.AlarmManager
import android.app.AlarmManager.AlarmClockInfo
import android.content.Context
import android.content.Intent
import android.app.PendingIntent
import android.os.Build

class AlarmScheduler(val context: Context, private val alarmManager: AlarmManager? ) {

    val id = System.currentTimeMillis().toInt()

    fun scheduleAnAlarm(alertTime : Long,phoneNumber : String,rentalType : String,bookingStatus : String){
        val intent = Intent(context,AlarmReceiver::class.java)
        intent.putExtra("phoneNumber",phoneNumber)
        intent.putExtra("rentalType",rentalType)
        intent.putExtra("bookingStatus",bookingStatus)

        val appIntent = PendingIntent.getBroadcast(context, id, intent, 0)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            alarmManager?.setAndAllowWhileIdle(AlarmManager.RTC_WAKEUP,alertTime,appIntent)
        }
    }

}