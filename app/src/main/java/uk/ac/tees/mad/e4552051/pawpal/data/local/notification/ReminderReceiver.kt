package uk.ac.tees.mad.e4552051.pawpal.data.local.notification

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import uk.ac.tees.mad.e4552051.pawpal.R
import android.util.Log

class ReminderReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {

        Log.d("ReminderReceiver", "onReceive called with intent: $intent")

        val title = intent.getStringExtra("title") ?: "Pet Reminder"
        val message = intent.getStringExtra("message") ?: "You have a reminder"
        val notificationId = intent.getIntExtra("notificationId", 0)

        Log.d("ReminderReceiver", "title=$title, message=$message, id=$notificationId")

        val notification = NotificationCompat.Builder(context, ReminderNotifications.CHANNEL_ID)
            .setSmallIcon(R.drawable.outline_notifications_24)
            .setContentTitle(title)
            .setContentText(message)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setAutoCancel(true)
            .build()

        if (NotificationManagerCompat.from(context).areNotificationsEnabled()) {
            Log.d("ReminderReceiver", "Calling notify()")
            NotificationManagerCompat.from(context).notify(notificationId, notification)
        }
    }
}
//class ReminderReceiver : BroadcastReceiver() {
//
//    override fun onReceive(context: Context, intent: Intent) {
//        val title = intent.getStringExtra("title") ?: "Pet reminder"
//        val message = intent.getStringExtra("message") ?: "You have a pet reminder!"
//
//        val notificationId = intent.getIntExtra("notificationId", System.currentTimeMillis().toInt())
//
//        val notification = NotificationCompat.Builder(context, ReminderNotifications.CHANNEL_ID)
//            .setSmallIcon(R.drawable.outline_notifications_24)
//            .setContentTitle(title)
//            .setContentText(message)
//            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
//            .setAutoCancel(true)
//            .build()
//
//        if (NotificationManagerCompat.from(context).areNotificationsEnabled()) {
//            NotificationManagerCompat.from(context).notify(notificationId, notification)
//        }
//    }
//}