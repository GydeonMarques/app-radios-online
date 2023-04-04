package br.com.gms.radiosonline.mediaplayer

import android.net.Uri
import android.os.Bundle
import android.support.v4.media.MediaBrowserCompat
import android.support.v4.media.MediaBrowserCompat.MediaItem.FLAG_PLAYABLE
import android.support.v4.media.MediaDescriptionCompat
import android.support.v4.media.MediaMetadataCompat
import br.com.gms.radiosonline.data.model.RadioResponseModel
import br.com.gms.radiosonline.data.model.ResultModel
import br.com.gms.radiosonline.data.model.toModel
import br.com.gms.radiosonline.data.repository.remote.RemoteRadioStationsRepository
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.source.ConcatenatingMediaSource
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.upstream.cache.CacheDataSource
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject


class RadioStationMediaSource @Inject constructor(
    private val repository: RemoteRadioStationsRepository,
) {

    companion object {
        const val EXTRA_MEDIA_ITEM = "EXTRA_MEDIA_ITEM"
    }

    private val _radioStationsMediaItem = MutableStateFlow<MutableList<MediaBrowserCompat.MediaItem>>(mutableListOf())
    val radioStationsMediaItem: StateFlow<MutableList<MediaBrowserCompat.MediaItem>> get() = _radioStationsMediaItem

    var radioStationMetaData: List<MediaMetadataCompat> = emptyList()

    suspend fun load() {
        repository.getRadioStations().collect {
            if (it is ResultModel.Success) {
                buildMetaData(it.data)
                buildMediaItem(it.data)
            }
        }
    }

    fun indexOf(item: MediaMetadataCompat?): Int {
        return radioStationMetaData.indexOf(item)
    }

    fun search(query: String): MediaMetadataCompat? {
        return radioStationMetaData.find {
                    it.description.title?.contains(query, ignoreCase = true) != null ||
                    it.description.subtitle?.contains(query, ignoreCase = true) != null ||
                    it.description.description?.contains(query, ignoreCase = true) != null
        }
    }

    fun filter(query: String): List<MediaBrowserCompat.MediaItem>{
        return _radioStationsMediaItem.value.filter {
                    it.description.title?.contains(query, ignoreCase = true) != null ||
                    it.description.subtitle?.contains(query, ignoreCase = true) != null ||
                    it.description.description?.contains(query, ignoreCase = true) != null
        }
    }

    fun findIndex(index: Int): MediaMetadataCompat? {
        return if (index >= 0 && index <= radioStationMetaData.size) {
            radioStationMetaData[index]
        } else null
    }

    fun findFromUri(uri: Uri): MediaMetadataCompat? {
        return radioStationMetaData.find { it.description.mediaUri == uri }
    }

    fun findFromMediaId(mediaId: String): MediaMetadataCompat? {
        return radioStationMetaData.find { it.description.mediaId == mediaId }
    }

    private fun buildMediaItem(list: List<RadioResponseModel>) {
        _radioStationsMediaItem.value = list.map { radio ->
            MediaBrowserCompat.MediaItem(
                MediaDescriptionCompat.Builder()
                    .setTitle(radio.name)
                    .setMediaId(radio.id)
                    .setSubtitle(radio.description)
                    .setDescription(radio.description)
                    .setIconUri(Uri.parse(radio.logoUrl))
                    .setMediaUri(Uri.parse(radio.streamUrl))
                    .setExtras(Bundle().apply { putParcelable(EXTRA_MEDIA_ITEM, radio.toModel()) })
                    .build(),
                FLAG_PLAYABLE
            )
        }.toMutableList()
    }

    private fun buildMetaData(list: List<RadioResponseModel>) {
        radioStationMetaData = list.map { radio ->
            MediaMetadataCompat.Builder()
                .putString(MediaMetadataCompat.METADATA_KEY_TITLE, radio.name)
                .putString(MediaMetadataCompat.METADATA_KEY_MEDIA_ID, radio.id)
                .putString(MediaMetadataCompat.METADATA_KEY_AUTHOR, radio.name)
                .putString(MediaMetadataCompat.METADATA_KEY_GENRE, radio.category)
                .putString(MediaMetadataCompat.METADATA_KEY_MEDIA_URI, radio.streamUrl)
                .putString(MediaMetadataCompat.METADATA_KEY_DISPLAY_ICON_URI, radio.logoUrl)
                .putString(MediaMetadataCompat.METADATA_KEY_DISPLAY_SUBTITLE, radio.description)
                .build()
        }
    }

    fun buildMediaSource(dataSource: CacheDataSource.Factory): ConcatenatingMediaSource {
        return ConcatenatingMediaSource().apply {
            radioStationMetaData.forEach { item ->
                item.description.mediaUri?.let {
                    addMediaSource(
                        ProgressiveMediaSource
                            .Factory(dataSource)
                            .createMediaSource(MediaItem.fromUri(it))
                    )
                }
            }
        }
    }
}
