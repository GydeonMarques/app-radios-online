package br.com.gms.radiosonline.mediaplayer

import android.app.Notification

interface MediaPlayerNotificationListener {
    fun onNotificationCancelled(notificationId: Int, dismissedByUser: Boolean)
    fun onNotificationPosted(notificationId: Int, notification: Notification)
}