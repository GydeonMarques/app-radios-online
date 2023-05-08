package br.com.gms.radiosonline.mediaplayer

import android.net.Uri
import android.os.Bundle
import android.support.v4.media.MediaBrowserCompat
import android.support.v4.media.MediaBrowserCompat.MediaItem.FLAG_PLAYABLE
import android.support.v4.media.MediaDescriptionCompat
import android.support.v4.media.MediaMetadataCompat
import br.com.gms.radiosonline.data.model.remote.ResultModel
import br.com.gms.radiosonline.data.repository.remote.RemoteRadioStationsRepository
import br.com.gms.radiosonline.domain.model.RadioModel
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.source.ConcatenatingMediaSource
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.upstream.cache.CacheDataSource
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject


class RadioStationMediaSource @Inject constructor(
    private val repository: RemoteRadioStationsRepository,
) {

    companion object {
        const val EXTRA_MEDIA_ITEM = "EXTRA_MEDIA_ITEM"
    }

    var radioStationMetaData: MediaMetadataCompat? = null

    private val _radioStationsMediaItem = MutableStateFlow<MediaBrowserCompat.MediaItem?>(null)
    val radioStationsMediaItem: StateFlow<MediaBrowserCompat.MediaItem?> get() = _radioStationsMediaItem

    suspend fun findFromMediaId(mediaId: String) {
        repository.getRadioStationById(mediaId).collect { result ->
            if (result is ResultModel.Success) {
                result.data?.let {
                    buildMetaData(it)
                    buildMediaItem(it)
                }
            }
        }
    }

    private fun buildMediaItem(radio: RadioModel) {
        _radioStationsMediaItem.update {
            MediaBrowserCompat.MediaItem(
                MediaDescriptionCompat.Builder()
                    .setTitle(radio.name)
                    .setMediaId(radio.id)
                    .setSubtitle(radio.description)
                    .setDescription(radio.description)
                    .setIconUri(Uri.parse(radio.logoUrl))
                    .setMediaUri(Uri.parse(radio.streamUrl))
                    .setExtras(Bundle().apply { putParcelable(EXTRA_MEDIA_ITEM, radio) })
                    .build(),
                FLAG_PLAYABLE
            )
        }
    }

    private fun buildMetaData(radio: RadioModel) {
        radioStationMetaData = MediaMetadataCompat.Builder()
            .putString(MediaMetadataCompat.METADATA_KEY_TITLE, radio.name)
            .putString(MediaMetadataCompat.METADATA_KEY_MEDIA_ID, radio.id)
            .putString(MediaMetadataCompat.METADATA_KEY_AUTHOR, radio.name)
            .putString(MediaMetadataCompat.METADATA_KEY_GENRE, radio.category)
            .putString(MediaMetadataCompat.METADATA_KEY_MEDIA_URI, radio.streamUrl)
            .putString(MediaMetadataCompat.METADATA_KEY_DISPLAY_ICON_URI, radio.logoUrl)
            .putString(MediaMetadataCompat.METADATA_KEY_DISPLAY_SUBTITLE, radio.description)
            .build()
    }

    fun buildMediaSource(dataSource: CacheDataSource.Factory): ConcatenatingMediaSource {
        return ConcatenatingMediaSource().apply {
            radioStationMetaData?.description?.mediaUri?.let {
                addMediaSource(
                    ProgressiveMediaSource
                        .Factory(dataSource)
                        .createMediaSource(MediaItem.fromUri(it))
                )
            }
        }
    }
}

fun MediaDescriptionCompat.extractRadioModel(): RadioModel? {
    return extras?.getParcelable<RadioModel>(RadioStationMediaSource.EXTRA_MEDIA_ITEM)
}
