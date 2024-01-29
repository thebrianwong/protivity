package com.thebrianwong.protivity.classes

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.media.AudioAttributes
import android.media.RingtoneManager
import android.net.Uri
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat.getSystemService
import com.thebrianwong.protivity.MainActivity
import com.thebrianwong.protivity.R

class NotificationUtils(private val context: Context) {
    private var _openAppIntent: PendingIntent? = null
    private var _notification: Notification? = null

    private fun createIntent() {
        val intent = Intent(context, MainActivity::class.java)
            .apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK and Intent.FLAG_ACTIVITY_CLEAR_TASK
            }
        val pendingIntent: PendingIntent =
            PendingIntent.getActivity(context, 10, intent, PendingIntent.FLAG_MUTABLE)
        this._openAppIntent = pendingIntent
    }

    private fun createNotification(
        alarmEnabled: Boolean,
        vibrateEnabled: Boolean
    ) {
        val notificationBuilder =
            NotificationCompat.Builder(context, "protivityChannel$alarmEnabled$vibrateEnabled")
                .setSmallIcon(R.drawable.baseline_timer_24)
                .setContentTitle("Protivity")
                .setContentText("Time's Up!")
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setAutoCancel(true)
                .setFullScreenIntent(this._openAppIntent, true)

        val alarmSound: Uri? = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM)
        val alarmAudioAttributes =
            AudioAttributes.Builder().setUsage(AudioAttributes.USAGE_ALARM).build()

        val vibPattern = longArrayOf(
            0, 500, 50, 100, 50, 500, 50, 100, 50, 500,
            1000, 500, 50, 100, 50, 500, 50, 100, 50, 500,
            1000, 500, 50, 100, 50, 500, 50, 100, 50, 500
        )
        val notificationChannel = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel(
                "protivityChannel$alarmEnabled$vibrateEnabled",
                "Protivity Timer",
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = "The notification for when the Protivity timer is up."
                vibrationPattern = vibPattern
                setSound(
                    if (alarmEnabled) alarmSound else null,
                    alarmAudioAttributes
                )
                enableVibration(vibrateEnabled)
            }
        } else {
            null
        }

        val notificationManager: NotificationManager =
            getSystemService(context, NotificationManager::class.java) as NotificationManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O && notificationChannel != null) {
            notificationManager.createNotificationChannel(notificationChannel)
        }
        this._notification = notificationBuilder.build()
    }

    fun changeNotificationChannels(
        alarmEnabled: Boolean,
        vibrateEnabled: Boolean
    ) {
        createIntent()
        createNotification(alarmEnabled, vibrateEnabled)
    }

    fun dispatchNotification() {
        with(NotificationManagerCompat.from(context)) {
            notify(10, _notification!!)
        }
    }
}
