package br.com.gms.radiosonline.mediaplayer

import android.content.ComponentName
import android.content.Context
import android.support.v4.media.MediaBrowserCompat
import android.support.v4.media.MediaMetadataCompat
import android.support.v4.media.session.MediaControllerCompat
import android.support.v4.media.session.PlaybackStateCompat
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import br.com.gms.radiosonline.domain.model.RadioModel
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class MediaPlayerServiceConnection @Inject constructor(
    @ApplicationContext context: Context
) {

    lateinit var mediaController: MediaControllerCompat

    private val _isConnected: MutableLiveData<Boolean> = MutableLiveData(false)
    val isConnected: LiveData<Boolean> get() = _isConnected

    private val _playbackStateCompat: MutableLiveData<PlaybackStateCompat?> = MutableLiveData(null)
    val playbackStateCompat: LiveData<PlaybackStateCompat?> get() = _playbackStateCompat

    private val _currentPlayingMedia: MutableLiveData<MediaMetadataCompat?> = MutableLiveData(null)
    val currentPlayingMedia: LiveData<MediaMetadataCompat?> get() = _currentPlayingMedia

    private val mediaControllerCompat: MediaControllerCompat.Callback =
        object : MediaControllerCompat.Callback() {

            override fun onPlaybackStateChanged(state: PlaybackStateCompat?) {
                super.onPlaybackStateChanged(state)
                _playbackStateCompat.value = state
            }

            override fun onMetadataChanged(metadata: MediaMetadataCompat?) {
                super.onMetadataChanged(metadata)
                _currentPlayingMedia.value = metadata
            }

            override fun onSessionDestroyed() {
                super.onSessionDestroyed()
                mediaBrowserServiceCallback.onConnectionSuspended()
            }
        }

    private val mediaBrowserServiceCallback: MediaBrowserCompat.ConnectionCallback =
        object : MediaBrowserCompat.ConnectionCallback() {
            override fun onConnected() {
                mediaController = MediaControllerCompat(context, mediaBrowser.sessionToken).apply {
                    registerCallback(mediaControllerCompat)
                    _currentPlayingMedia.value = metadata
                    _isConnected.value = true
                }
            }

            override fun onConnectionFailed() {
                super.onConnectionFailed()
                _isConnected.value = false
            }

            override fun onConnectionSuspended() {
                super.onConnectionSuspended()
                _isConnected.value = false
            }
        }

    val mediaBrowser = MediaBrowserCompat(
        context,
        ComponentName(context, MediaPlayerService::class.java),
        mediaBrowserServiceCallback,
        null

    ).apply {
        connect()
    }


    fun play(radio: RadioModel) {
        with(mediaController) {
            if (radio.id == _currentPlayingMedia.value?.description?.mediaId) {
                if (playbackState.state == PlaybackStateCompat.STATE_PLAYING) {
                    transportControls.pause()
                } else {
                    transportControls.play()
                }
            } else {
                transportControls.playFromMediaId(
                    radio.id,
                    null
                )
            }
        }
    }

    fun pause() {
        mediaController.transportControls.pause()
    }

    fun stop() {
        mediaController.transportControls.stop()
    }

    fun subscribe(callback: MediaBrowserCompat.SubscriptionCallback) {
        mediaBrowser.subscribe(MediaPlayerService.ROOT_ID, callback)
    }

    fun unSubscribe(callback: MediaBrowserCompat.SubscriptionCallback) {
        mediaBrowser.unsubscribe(MediaPlayerService.ROOT_ID, callback)
    }

}