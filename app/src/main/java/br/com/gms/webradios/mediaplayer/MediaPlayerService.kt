package br.com.gms.webradios.mediaplayer

import android.app.Notification
import android.app.PendingIntent
import android.media.AudioAttributes
import android.media.AudioFocusRequest
import android.media.AudioManager
import android.media.session.PlaybackState
import android.os.Build
import android.os.Bundle
import android.support.v4.media.MediaBrowserCompat
import android.support.v4.media.MediaMetadataCompat
import android.support.v4.media.session.MediaSessionCompat
import android.support.v4.media.session.PlaybackStateCompat
import android.widget.Toast
import androidx.media.MediaBrowserServiceCompat
import br.com.gms.webradios.R
import br.com.gms.webradios.domain.usecase.RadioStationsListUseCase
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.PlaybackException
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.upstream.cache.CacheDataSource
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.*
import javax.inject.Inject


@AndroidEntryPoint
class MediaPlayerService : MediaBrowserServiceCompat(), MediaPlayerNotificationListener {

    companion object {
        const val ROOT_ID = "Rádio Online App"
    }

    private var playerIsRelease = false

    private var serviceIsRunning = false

    private lateinit var exoPlayer: ExoPlayer

    @Inject
    lateinit var userCase: RadioStationsListUseCase

    private lateinit var audioManager: AudioManager

    @Inject
    lateinit var mediaSource: RadioStationMediaSource

    private var radioToPlay: MediaMetadataCompat? = null

    private lateinit var mediaSession: MediaSessionCompat

    @Inject
    lateinit var dataSourceFactory: CacheDataSource.Factory

    private lateinit var audioFocusRequest: AudioFocusRequest

    private lateinit var playbackState: PlaybackStateCompat.Builder

    private lateinit var notificationManager: MediaPlayerNotificationManager

    private lateinit var audioFocusListener: AudioManager.OnAudioFocusChangeListener

    private val serviceScope by lazy { CoroutineScope(Dispatchers.Main + SupervisorJob()) }

    private val mediaSessionCallback = object : MediaSessionCompat.Callback() {

        override fun onPlayFromMediaId(mediaId: String?, extras: Bundle?) {
            super.onPlayFromMediaId(mediaId, extras)
            findFromMediaId(mediaId)
        }

        override fun onPause() {
            super.onPause()
            pause()
        }

        override fun onStop() {
            super.onStop()
            stop()
        }

        override fun onPlay() {
            super.onPlay()
            play()
        }
    }

    override fun onCreate() {
        super.onCreate()
        initializePlayer()
        initializeMediaSource()
        initializeMediaSession()
        initializeAudioManager()
        initializeNotificationManger()
    }

    override fun onDestroy() {
        stop()
        clearSession()
        super.onDestroy()
    }

    override fun onGetRoot(
        clientPackageName: String,
        clientUid: Int,
        rootHints: Bundle?
    ): BrowserRoot {
        return BrowserRoot(ROOT_ID, null)
    }

    override fun onLoadChildren(
        parentId: String,
        result: Result<MutableList<MediaBrowserCompat.MediaItem>>
    ) {
        result.detach()
        when (parentId) {
            ROOT_ID -> {
                mediaSource.radioStationsMediaItem.value?.let {
                    result.sendResult(mutableListOf(it))
                }
            }
            else -> Unit
        }
    }

    override fun onCustomAction(action: String, extras: Bundle?, result: Result<Bundle>) {
        super.onCustomAction(action, extras, result)
        if (action == ROOT_ID) {
            notifyChildrenChanged(ROOT_ID)
        }
    }


    private fun initializePlayer() {
        playerIsRelease = false
        exoPlayer = ExoPlayer.Builder(this)
            .build()
            .apply {
                setAudioAttributes(audioAttributes, false)
                setHandleAudioBecomingNoisy(false)
                addListener(object : Player.Listener {

                    override fun onPlaybackStateChanged(playbackState: Int) {
                        super.onPlaybackStateChanged(playbackState)
                        if (playbackState == PlaybackStateCompat.STATE_PLAYING) {
                            updatePlaybackState(playbackState)
                        }
                    }

                    override fun onPlayerError(error: PlaybackException) {

                        val message =
                            if (error.errorCode == PlaybackException.ERROR_CODE_IO_FILE_NOT_FOUND) {
                                R.string.error_media_not_found
                            } else {
                                R.string.generic_error
                            }

                        Toast.makeText(applicationContext, message, Toast.LENGTH_SHORT).show()
                        updatePlaybackState(PlaybackStateCompat.STATE_ERROR)
                    }
                })
            }
    }

    private fun initializeMediaSource() {
        serviceScope.launch {
            mediaSource.radioStationsMediaItem.collect {
                notifyChildrenChanged(ROOT_ID)
                play()
            }
        }
    }

    private fun initializeAudioManager() {
        audioManager = getSystemService(AUDIO_SERVICE) as AudioManager

        audioFocusListener = AudioManager.OnAudioFocusChangeListener { focusChange ->
            when (focusChange) {
                AudioManager.AUDIOFOCUS_GAIN -> {
                    mediaSession.controller.transportControls.play()
                    updatePlaybackState(PlaybackStateCompat.STATE_PLAYING)
                }
                AudioManager.AUDIOFOCUS_LOSS,
                AudioManager.AUDIOFOCUS_LOSS_TRANSIENT,
                AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK -> {
                    mediaSession.controller.transportControls.pause()
                    updatePlaybackState(PlaybackStateCompat.STATE_PAUSED)
                }
            }
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            audioFocusRequest = AudioFocusRequest.Builder(AudioManager.AUDIOFOCUS_GAIN).run {
                setAudioAttributes(AudioAttributes.Builder().run {
                    setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                    setUsage(AudioAttributes.USAGE_MEDIA)
                    build()
                })
                setOnAudioFocusChangeListener(audioFocusListener)
                setWillPauseWhenDucked(true)
                build()
            }
        }
    }

    private fun initializeMediaSession() {
        playbackState = PlaybackStateCompat.Builder().setActions(
            buildAllowedActions()
        )

        val sessionActivityPendingIntent =
            packageManager?.getLaunchIntentForPackage(packageName)?.let { sessionIntent ->
                PendingIntent.getActivity(
                    this,
                    0,
                    sessionIntent,
                    PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
                )
            }

        mediaSession = MediaSessionCompat(this, packageName).apply {
            setSessionActivity(sessionActivityPendingIntent)
            setPlaybackState(playbackState.build())
            setCallback(mediaSessionCallback)
            isActive = true
        }

        sessionToken = mediaSession.sessionToken

    }

    private fun initializeNotificationManger() {
        notificationManager = MediaPlayerNotificationManager(
            this,
            mediaSession.controller.sessionActivity,
            this,
        )
    }

    private fun buildAllowedActions(actionOnRemove: List<Long>? = null): Long {
        val actions = arrayListOf(
            PlaybackStateCompat.ACTION_PLAY,
            PlaybackStateCompat.ACTION_STOP,
            PlaybackStateCompat.ACTION_PAUSE,
            PlaybackStateCompat.ACTION_PLAY_PAUSE,
            PlaybackStateCompat.ACTION_PLAY_FROM_MEDIA_ID,
        )

        actionOnRemove?.let { actions.removeAll(it.toSet()) }

        return actions.reduce { acc, action -> (acc or action) }
    }

    private fun findFromMediaId(mediaId: String?) {
        serviceScope.launch {
            mediaId?.let {
                mediaSource.findFromMediaId(it)
            }
        }
    }

    private fun play() {
        val result = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            audioManager.requestAudioFocus(audioFocusRequest)
        } else {
            audioManager.requestAudioFocus(
                audioFocusListener,
                AudioManager.STREAM_MUSIC,
                AudioManager.AUDIOFOCUS_GAIN
            )
        }

        if (result == AudioManager.AUDIOFOCUS_REQUEST_GRANTED) {

            if (playerIsRelease) {
                initializePlayer()
            }

            exoPlayer.setMediaSource(mediaSource.buildMediaSource(dataSourceFactory))
            exoPlayer.prepare()
            exoPlayer.playWhenReady = true

            updatePlaybackState(PlaybackStateCompat.STATE_BUFFERING)
            updateCurrentMediaItem()
        }
    }


    private fun pause() {
        if (exoPlayer.playbackState == PlaybackStateCompat.STATE_PLAYING ||
            exoPlayer.playbackState == PlaybackStateCompat.STATE_BUFFERING ||
            exoPlayer.playbackState == PlaybackStateCompat.STATE_CONNECTING
        ) {
            exoPlayer.playWhenReady = false
            updatePlaybackState(PlaybackStateCompat.STATE_PAUSED)
            updateCurrentMediaItem()
        }
    }

    private fun stop() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            audioManager.abandonAudioFocusRequest(audioFocusRequest)
        } else {
            audioManager.abandonAudioFocus(audioFocusListener)
        }

        updatePlaybackState(PlaybackStateCompat.STATE_STOPPED)
        exoPlayer.playWhenReady = false
        exoPlayer.clearMediaItems()
        exoPlayer.release()

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            stopForeground(STOP_FOREGROUND_REMOVE)
        } else {
            stopSelf()
        }

        serviceIsRunning = false
        playerIsRelease = true
        serviceScope.cancel()

        stopSelf()

    }

    private fun clearSession() {
        mediaSession.isActive = false
        mediaSession.release()
    }

    private fun updateCurrentMediaItem() {
        mediaSource.radioStationMetaData?.let {
            radioToPlay = it
            mediaSession.setMetadata(it)
            notificationManager.sendNotification(it, exoPlayer)
        }
    }

    private fun updatePlaybackState(state: Int) {
        val allowedActions = when (state) {
            PlaybackStateCompat.STATE_BUFFERING,
            PlaybackStateCompat.STATE_PLAYING -> {
                val actionOnRemove = arrayListOf(
                    PlaybackStateCompat.ACTION_PLAY,
                )
                buildAllowedActions(actionOnRemove)
            }
            PlaybackStateCompat.STATE_NONE,
            PlaybackStateCompat.STATE_ERROR,
            PlaybackStateCompat.STATE_PAUSED,
            PlaybackStateCompat.STATE_STOPPED -> {

                val actionOnRemove = listOf(
                    PlaybackStateCompat.ACTION_STOP,
                    PlaybackStateCompat.ACTION_PAUSE,
                )

                buildAllowedActions(actionOnRemove)

            }
            else -> 0
        }

        mediaSession.setPlaybackState(
            playbackState
                .setState(state, PlaybackState.PLAYBACK_POSITION_UNKNOWN, 1.0f)
                .setActions(allowedActions)
                .build()
        )
    }

    override fun onNotificationCancelled(notificationId: Int, dismissedByUser: Boolean) {
        stop()
    }

    override fun onNotificationPosted(notificationId: Int, notification: Notification) {
        if (serviceIsRunning.not()) {
            serviceIsRunning = true
            startForeground(notificationId, notification)
        }
    }
}