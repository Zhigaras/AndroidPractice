package com.example.background_works

import android.app.*
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat

interface NotificationProvider {
    
    fun createNotification(
        context: Context,
        channelID: String,
        notificationText: String
    ): Notification {
        return NotificationCompat.Builder(context, channelID)
            .setSmallIcon(R.drawable.ic_notification)
            .setContentTitle("Alarm")
            .setContentText(notificationText)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setCategory(NotificationCompat.CATEGORY_MESSAGE)
            .setAutoCancel(true)
            .build()
    }
    
    fun getPendingIntent(context: Context, classz: Class<out Activity>): PendingIntent {
        val fullScreenIntent = Intent(context, classz)
        return PendingIntent.getActivity(
            context, 0, fullScreenIntent, PendingIntent.FLAG_IMMUTABLE
        )
    }
    
    fun createNotificationChannel(
        context: Context,
        channelID: String,
        channelName: String,
        channelDescription: String
    ) {
        val importance = NotificationManager.IMPORTANCE_HIGH
        val channel = NotificationChannel(channelID, channelName, importance)
        channel.description = channelDescription
        val notificationManager = getNotificationManager(context)
        notificationManager.createNotificationChannel(channel)
    }
    
    fun getNotificationManager(context: Context): NotificationManager {
        return context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
    }
    
    fun startNotification(context: Context, notificationText: String, notification: Notification) {
        getNotificationManager(context).notify(notificationText.hashCode(), notification)
    }
    
    fun makeChannelAndNotify(
        context: Context,
        channelID: String,
        notificationText: String,
        channelName: String,
        channelDescription: String
    ) {
        createNotificationChannel(context, channelID, channelName, channelDescription)
        val notification = createNotification(context, channelID, notificationText)
        startNotification(context, notificationText, notification)
    }
}