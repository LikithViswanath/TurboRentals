package com.example.turborentals.alarmDisbatcher

import android.app.Notification.DEFAULT_ALL
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.NotificationManager.IMPORTANCE_HIGH
import android.app.PendingIntent.getActivity
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Context.NOTIFICATION_SERVICE
import android.content.Intent
import android.content.Intent.FLAG_ACTIVITY_CLEAR_TASK
import android.content.Intent.FLAG_ACTIVITY_NEW_TASK
import android.graphics.Color.RED
import android.os.Build.VERSION.SDK_INT
import android.os.Build.VERSION_CODES.O
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationCompat.PRIORITY_MAX
import androidx.work.*
import com.example.turborentals.R
import com.example.turborentals.data.BookingDetails
import com.example.turborentals.ui.profile.ProfileActivity
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.tasks.await

class AlarmReceiver : BroadcastReceiver() {

    companion object {
        const val NOTIFICATION_ID = "turboRental_notification_id"
        const val NOTIFICATION_NAME = "turboRental"
        const val NOTIFICATION_CHANNEL = "turboRental_channel_01"
    }

    override fun onReceive(p0: Context?, p1: Intent?) {

        val phoneNumber = p1?.getStringExtra("phoneNumber")
        val rentalType = p1?.getStringExtra("rentalType")
        val bookingStatus = p1?.getStringExtra("bookingStatus")

        Log.v("Coroutine","$phoneNumber $rentalType $bookingStatus")

        Log.v("Coroutine","Broadcasted has be initiated")

        val updateStatusData = Data.Builder()
            .putString("phoneNumber",phoneNumber)
            .putString("rentalType",rentalType)
            .build()
        val updateDataBaseWorker = OneTimeWorkRequestBuilder<UpdateDataBaseWorker>().setInputData(updateStatusData).build()

        WorkManager.getInstance(p0!!).enqueue(updateDataBaseWorker)
        sendNotification(p0,bookingStatus!!)
    }

    private fun sendNotification(context: Context,bookingStatus: String){

        val id = System.currentTimeMillis()

        val applicationContext = context.applicationContext
        val intent = Intent(applicationContext, ProfileActivity::class.java)
        intent.flags = FLAG_ACTIVITY_NEW_TASK or FLAG_ACTIVITY_CLEAR_TASK
        intent.putExtra(NOTIFICATION_ID,id)

        val notificationManager =
            applicationContext.getSystemService(NOTIFICATION_SERVICE) as NotificationManager

        val titleNotification = "turboRentals"
        var subtitleNotification : String? = null
        when(bookingStatus){
            "Running"->{
                subtitleNotification = "Collect"
            }
            "Upcoming"->{
                subtitleNotification = "Deliver"
            }
        }

        val pendingIntent = getActivity(applicationContext, 0, intent, 0)
        val notification = NotificationCompat.Builder(applicationContext, NOTIFICATION_CHANNEL)
            .setSmallIcon(R.drawable.ic_camera)
            .setContentTitle(titleNotification)
            .setContentText(subtitleNotification)
            .setDefaults(DEFAULT_ALL).setContentIntent(pendingIntent).setAutoCancel(true)

        notification.priority = PRIORITY_MAX

        if (SDK_INT >= O) {
            notification.setChannelId(NOTIFICATION_CHANNEL)

            val channel =
                NotificationChannel(NOTIFICATION_CHANNEL,NOTIFICATION_NAME, IMPORTANCE_HIGH)

            channel.enableLights(true)
            channel.lightColor = RED
            channel.enableVibration(true)
            channel.vibrationPattern = longArrayOf(100, 200, 300, 400, 500, 400, 300, 200, 400)
            notificationManager.createNotificationChannel(channel)
        }

        notificationManager.notify(id.toInt(), notification.build())
    }

    class UpdateDataBaseWorker(appContext: Context, params: WorkerParameters) : CoroutineWorker(appContext, params) {
        override suspend fun doWork(): Result {

            val phoneNumber = inputData.getString("phoneNumber")
            val rentalType = inputData.getString("rentalType")

            val data = Firebase.firestore.collection("${Firebase.auth.currentUser?.uid}").document(rentalType!!)
                .collection("Bookings").document(phoneNumber!!).get().await().toObject(BookingDetails::class.java)

            var updatedStatus : String? = null

            when(data?.bookingStatus){
                "Running"->{
                    updatedStatus = "Collect"
                }
                "Upcoming"->{
                    updatedStatus = "Delivery"
                }
            }

            Firebase.firestore.runBatch { batch->
                batch.update(Firebase.firestore.collection("${Firebase.auth.currentUser?.uid}").document(rentalType)
                    .collection("Bookings").document(phoneNumber),
                    "bookingStatus",updatedStatus)
            }.await()

            return Result.success()

        }
    }

}

