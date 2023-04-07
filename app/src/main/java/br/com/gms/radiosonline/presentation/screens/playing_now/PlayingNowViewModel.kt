package br.com.gms.radiosonline.presentation.screens.playing_now

import android.support.v4.media.MediaBrowserCompat
import android.support.v4.media.MediaMetadataCompat
import android.support.v4.media.session.PlaybackStateCompat
import androidx.lifecycle.*
import br.com.gms.radiosonline.data.model.remote.ResultModel
import br.com.gms.radiosonline.domain.model.RadioModel
import br.com.gms.radiosonline.domain.usecase.RadioStationsListUseCase
import br.com.gms.radiosonline.mediaplayer.MediaPlayerServiceConnection
import br.com.gms.radiosonline.mediaplayer.extractRadioModel
import br.com.gms.radiosonline.presentation.navigation.radioIdParam
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PlayingNowViewModel @Inject constructor(
    private val state: SavedStateHandle,
    private val userCase: RadioStationsListUseCase,
    private val serviceConnection: MediaPlayerServiceConnection
) : ViewModel() {

    private var _radioId: String? = null
    private var _currentMediaId: String? = null
    private var _mediaItems: MutableList<RadioModel?> = mutableListOf()

    private val _currentPlayingMedia: MutableLiveData<RadioModel?> = MutableLiveData()
    val currentPlayingMedia: LiveData<RadioModel?> get() = _currentPlayingMedia

    private val _playbackState: MutableLiveData<PlaybackStateCompat?> = MutableLiveData(null)
    val playbackState: LiveData<PlaybackStateCompat?> get() = _playbackState

    private var _playingNowUiState = MutableStateFlow<PlayingNowUiState>(PlayingNowUiState.Nothing)
    val playingNowUiState: StateFlow<PlayingNowUiState> get() = _playingNowUiState

    private val subscriptionCallback = object : MediaBrowserCompat.SubscriptionCallback() {
        override fun onChildrenLoaded(parentId: String, items: MutableList<MediaBrowserCompat.MediaItem>) {
            super.onChildrenLoaded(parentId, items)
            _mediaItems = items.map { it.description.extractRadioModel() }.toMutableList()
            _playbackState.value = serviceConnection.mediaController.playbackState
            _radioId = state.get<String>(radioIdParam)
            updateTheCurrentPlaybackMediaOrStartANewPlayback()
        }
    }

    private val currentMediaMetadataObserver = Observer<MediaMetadataCompat?> {
        _radioId = null
        _currentMediaId = it?.description?.mediaId
        updateTheCurrentPlaybackMediaOrStartANewPlayback()
    }

    private val playbackStateObserver = Observer<PlaybackStateCompat?> { playback ->
        _playbackState.value = playback
    }

    init {
        serviceConnection.currentPlayingMedia.observeForever(currentMediaMetadataObserver)
        serviceConnection.playbackStateCompat.observeForever(playbackStateObserver)
        serviceConnection.subscribe(subscriptionCallback)
    }

    fun play(radioModel: RadioModel) {
        serviceConnection.play(radioModel)
    }

    fun pause() {
        serviceConnection.pause()
    }

    fun skipToNext() {
        serviceConnection.skipToNext()
    }

    fun skipToPrevious() {
        serviceConnection.skipToPrevious()
    }

    private fun getRadioStationById() {
        viewModelScope.launch {
            _radioId?.let {
                _playingNowUiState.update { PlayingNowUiState.Loading }
                userCase.getRadioStationById(it)
                    .collect { response ->
                        when (response) {
                            is ResultModel.Failure -> _playingNowUiState.update {
                                PlayingNowUiState.Failure(response.throwable)
                            }
                            is ResultModel.Success -> _playingNowUiState.update {
                                PlayingNowUiState.Success(
                                    radioStation = response.data
                                )
                            }
                        }
                    }
            }
        }
    }

    private fun updateTheCurrentPlaybackMediaOrStartANewPlayback() {
        viewModelScope.launch {
            if (_radioId == null || _radioId == _currentMediaId) {
                _mediaItems.find { it?.id == _currentMediaId }.also { radioModel ->
                    _currentPlayingMedia.value = radioModel
                    if (radioModel != null) {
                        _playingNowUiState.update {
                            PlayingNowUiState.Success(radioStation = radioModel)
                        }
                    }
                }
            } else {
                getRadioStationById()
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        serviceConnection.unSubscribe(subscriptionCallback)
        serviceConnection.playbackStateCompat.removeObserver(playbackStateObserver)
        serviceConnection.currentPlayingMedia.removeObserver(currentMediaMetadataObserver)
    }
}