package com.thebrianwong.protivity.classes

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat.getSystemService
import com.thebrianwong.protivity.MainActivity
import com.thebrianwong.protivity.R

class NotificationUtils(private val context: Context) {
    private val intent = Intent(context, MainActivity::class.java).apply {
        flags = Intent.FLAG_ACTIVITY_NEW_TASK and Intent.FLAG_ACTIVITY_CLEAR_TASK
    }
    private val pendingIntent: PendingIntent =
        PendingIntent.getActivity(context, 10, intent, PendingIntent.FLAG_MUTABLE)
    private val notificationBuilder = NotificationCompat.Builder(context, "protivityChannel")
        .setSmallIcon(R.drawable.baseline_timer_24)
        .setContentTitle("Protivity")
        .setContentText("Time's Up!")
        .setPriority(NotificationCompat.PRIORITY_HIGH)
        .setAutoCancel(true)
        .setDefaults(Notification.DEFAULT_VIBRATE)
        .setVibrate(longArrayOf(1000, 1000, 1000))
        .setFullScreenIntent(pendingIntent, true)
    private val notificationChannel = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        NotificationChannel(
            "protivityChannel",
            "Protivity Timer",
            NotificationManager.IMPORTANCE_HIGH
        ).apply { description = "The notification for when the Protivity timer is up." }
    } else {
        null
    }

    fun dispatchNotification() {
        val notificationManager: NotificationManager =
            getSystemService(context, NotificationManager::class.java) as NotificationManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O && notificationChannel != null) {
            notificationChannel.enableVibration(true)
            notificationManager.createNotificationChannel(notificationChannel)
        }
        val notification = notificationBuilder.build()

        with(NotificationManagerCompat.from(context)) {
            notify(10, notification)
        }
    }
}
