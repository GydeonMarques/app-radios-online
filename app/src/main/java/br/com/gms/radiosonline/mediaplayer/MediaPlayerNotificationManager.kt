package br.com.gms.radiosonline.mediaplayer

import android.annotation.SuppressLint
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.support.v4.media.MediaDescriptionCompat
import android.support.v4.media.MediaMetadataCompat
import android.support.v4.media.session.PlaybackStateCompat
import android.widget.RemoteViews
import androidx.core.app.NotificationCompat
import androidx.core.graphics.drawable.toBitmap
import androidx.media.MediaBrowserServiceCompat
import androidx.media.session.MediaButtonReceiver
import br.com.gms.radiosonline.R
import br.com.gms.radiosonline.presentation.activities.MainActivity
import coil.imageLoader
import coil.request.ImageRequest
import com.google.android.exoplayer2.ExoPlayer


internal class MediaPlayerNotificationManager(
    private val context: Context,
    private val pendingIntent: PendingIntent,
    private val mediaPlayerNotificationListener: MediaPlayerNotificationListener
) {


    companion object {
        private const val NOTIFICATION_ID = 0X653143
        private const val CHANNEL_NAME_NOTIFICATION = "Notificar rádios em execução"
    }

    private lateinit var notification: Notification
    private var notificationManager: NotificationManager? = (context.getSystemService(MediaBrowserServiceCompat.NOTIFICATION_SERVICE) as? NotificationManager?)

    fun sendNotification(metadataCompat: MediaMetadataCompat?, player: ExoPlayer) {
        metadataCompat?.let {

            val data = it.description

            val notificationLayout = RemoteViews(context.packageName, R.layout.notification_small).apply {
                setupNotificationViews(this, data, player)
            }

            val notificationLayoutExpanded = RemoteViews(context.packageName, R.layout.notification_big).apply {
                setupNotificationViews(this, data, player)
            }

            notification = NotificationCompat.Builder(context, buildChannel()).run {
                setOngoing(true)
                setUsesChronometer(true)
                setContentIntent(pendingIntent)
                setSmallIcon(R.drawable.ic_wi_fi)
                setCustomContentView(notificationLayout)
                setCustomBigContentView(notificationLayoutExpanded)
                setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                setStyle(NotificationCompat.DecoratedCustomViewStyle())
                setDeleteIntent(
                    MediaButtonReceiver.buildMediaButtonPendingIntent(
                        context,
                        PlaybackStateCompat.ACTION_STOP
                    )
                )
                build()
            }

            notificationManager?.notify(NOTIFICATION_ID, notification)
            mediaPlayerNotificationListener.onNotificationPosted(
                NOTIFICATION_ID,
                notification
            )

        } ?: run {
            clearNotification()
        }
    }

    @SuppressLint("CheckResult")
    private fun setupNotificationViews(
        view: RemoteViews,
        data: MediaDescriptionCompat,
        player: ExoPlayer
    ) {

        val pendingIntent = PendingIntent.getActivity(
            context,
            0,
            Intent(context, MainActivity::class.java),
            PendingIntent.FLAG_IMMUTABLE
        )

        view.apply {

            setTextViewText(R.id.content_title, "${data.title}")
            setTextViewText(R.id.content_text, data.subtitle)
            setOnClickPendingIntent(R.id.notification_container, pendingIntent)

            setImageViewResource(
                R.id.content_previous, if (player.hasPreviousMediaItem()) {
                    R.drawable.ic_skip_previous
                } else {
                    R.drawable.ic_skip_previous_disable
                }
            )

            setImageViewResource(
                R.id.content_play_pause, if (player.playWhenReady) {
                    R.drawable.ic_play_circle
                } else {
                    R.drawable.ic_pause_circle
                }
            )

            setImageViewResource(
                R.id.content_next, if (player.hasNextMediaItem()) {
                    R.drawable.ic_skip_next
                } else {
                    R.drawable.ic_skip_next_disable
                }
            )

            setOnClickPendingIntent(
                R.id.content_previous, MediaButtonReceiver.buildMediaButtonPendingIntent(
                    context,
                    PlaybackStateCompat.ACTION_SKIP_TO_PREVIOUS
                )
            )

            setOnClickPendingIntent(
                R.id.content_play_pause, MediaButtonReceiver.buildMediaButtonPendingIntent(
                    context,
                    PlaybackStateCompat.ACTION_PLAY_PAUSE
                )
            )

            setOnClickPendingIntent(
                R.id.content_next, MediaButtonReceiver.buildMediaButtonPendingIntent(
                    context,
                    PlaybackStateCompat.ACTION_SKIP_TO_NEXT
                )
            )

            setOnClickPendingIntent(
                R.id.content_close, MediaButtonReceiver.buildMediaButtonPendingIntent(
                    context,
                    PlaybackStateCompat.ACTION_STOP
                )
            )

            context.imageLoader.enqueue(ImageRequest.Builder(context)
                .size(500)
                .data(data.iconUri)
                .crossfade(true)
                .target { drawable ->
                    setImageViewBitmap(R.id.icon, drawable.toBitmap())
                    notificationManager?.notify(NOTIFICATION_ID, notification)
                }
                .build())
        }
    }

    @Synchronized
    private fun buildChannel(): String {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            context.packageName.apply {
                notificationManager?.createNotificationChannel(
                    NotificationChannel(
                        this,
                        CHANNEL_NAME_NOTIFICATION,
                        NotificationManager.IMPORTANCE_LOW
                    )
                )
            }
        } else {
            CHANNEL_NAME_NOTIFICATION
        }
    }

    private fun clearNotification() {
        notificationManager?.apply {
            mediaPlayerNotificationListener.onNotificationCancelled(NOTIFICATION_ID, true)
            cancelAll()
        }
    }

}


