package com.example.skywise.domain.utils

import android.app.Activity
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import com.example.skywise.presentation.ui.main.MainActivity

object NotificationUtils {

    const val CHANNEL_ID = "22"
    lateinit var notificationManager: NotificationManager

    fun initialize(notificationManager: NotificationManager) {
        NotificationUtils.notificationManager = notificationManager
        createNotificationChannel()
    }

    private fun createNotificationChannel() {
        val name = "Skywise Alerts"
        val descriptionText = "Used For User-Set Weather Alerts"
        val importance = NotificationManager.IMPORTANCE_HIGH
        val channel = NotificationChannel(CHANNEL_ID, name, importance).apply {
            description = descriptionText
            enableVibration(true)
        }
        notificationManager.createNotificationChannel(channel)
    }

    fun makeNotification(
        context: Context,
        smallIcon: Int,
        notificationTitle: String,
        notificationContent: String,
        priority: Int = NotificationCompat.PRIORITY_HIGH
    ): Notification {

        return NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(smallIcon)
            .setContentTitle(notificationTitle)
            .setContentText(notificationContent)
            .setStyle(
                NotificationCompat
                    .BigTextStyle()
                    .bigText(notificationContent)
            ).setContentIntent(createPendingIntentToMainActivity(context))
            .setAutoCancel(true)
            .setPriority(priority).build()
    }

    private fun createPendingIntentToMainActivity(context: Context): PendingIntent {
        val intent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }


        return PendingIntent.getActivity(
            context, 0, intent, PendingIntent.FLAG_IMMUTABLE
        )
    }

    fun requestNotificationsPermission(activity: Activity) {
        PermissionsUtil.requestNotificationsPermission(activity)
    }
}