package com.thebrianwong.protivity.classes

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat.getSystemService
import com.thebrianwong.protivity.MainActivity
import com.thebrianwong.protivity.R

@RequiresApi(Build.VERSION_CODES.TIRAMISU)
class NotificationUtils(private val context: Context) {
    private val intent = Intent(context, MainActivity::class.java).apply {
        flags = Intent.FLAG_ACTIVITY_NEW_TASK and Intent.FLAG_ACTIVITY_CLEAR_TASK
    }
    private val pendingIntent: PendingIntent =
        PendingIntent.getActivity(context, 10, intent, PendingIntent.FLAG_MUTABLE)
    private val builder = NotificationCompat.Builder(context, "timerUp")
        .setSmallIcon(R.drawable.baseline_timer_24)
        .setContentTitle("Title")
        .setContentText("Text")
        .setPriority(NotificationCompat.PRIORITY_HIGH)
        .setAutoCancel(true)
        .setDefaults(Notification.DEFAULT_VIBRATE)
        .setVibrate(longArrayOf(1000, 1000, 1000))
        .setFullScreenIntent(pendingIntent, true)
    private val notiChannel = NotificationChannel(
        "timerUp",
        "myTest",
        NotificationManager.IMPORTANCE_HIGH
    ).apply { description = "myDescription" }

    fun dispatchNotification() {
        val notiManager: NotificationManager =
            getSystemService(context, NotificationManager::class.java) as NotificationManager
        notiChannel.enableVibration(true)
        notiManager.createNotificationChannel(notiChannel)

        with(NotificationManagerCompat.from(context)) {
            val permissionUtils = PermissionUtils(context)
            if (permissionUtils.hasNotificationPermission()) {
                val notification = builder.build()
                notify(10, notification)
            }
        }

    }

}
