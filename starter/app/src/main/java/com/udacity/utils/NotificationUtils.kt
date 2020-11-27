package com.udacity.utils

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import com.udacity.DetailActivity
import com.udacity.R


private val NOTIFICATION_ID = 0
private val REQUEST_CODE = 0
private val FLAGS = 0

/**
 * Builds and delivers the notification.
 *
 * @param context, activity context.
 */
fun NotificationManager.sendNotification(messageBody: String, applicationContext: Context) {
    // Create the content intent to open the appropriate activity.
    val contentIntent = Intent(applicationContext, DetailActivity::class.java)

    // Pending Intent is used to open the app.
    val contentPendingIntent = PendingIntent.getActivity(applicationContext, NOTIFICATION_ID, contentIntent, PendingIntent.FLAG_UPDATE_CURRENT)

    // The first thing we do is to create the intent for the notification which launches the main activity.
    val notificationBuilder = NotificationCompat.Builder(applicationContext, applicationContext.getString(R.string.githubRepo_notification_channel_id))


    // The second thing we need to do is the following
    // Set the title, text, and ion to the notificationBuilder
            // Maybe setup a small icon if its available?
            // We could use the github image :D
            .setSmallIcon(R.drawable.ic_assistant_black_24dp)
            .setContentTitle("Download Complete")
            .setContentText(messageBody)
            .setContentIntent(contentPendingIntent)
            .setAutoCancel(true)

    //Deliver the notification
    notify(NOTIFICATION_ID, notificationBuilder.build())
}

