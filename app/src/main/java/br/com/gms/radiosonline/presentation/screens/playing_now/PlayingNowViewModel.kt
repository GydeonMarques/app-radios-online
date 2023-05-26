package br.com.gms.radiosonline.presentation.screens.playing_now

import android.support.v4.media.MediaBrowserCompat
import android.support.v4.media.MediaMetadataCompat
import android.support.v4.media.session.PlaybackStateCompat
import android.util.Log
import androidx.lifecycle.*
import br.com.gms.radiosonline.data.model.remote.ResultModel
import br.com.gms.radiosonline.domain.model.RadioModel
import br.com.gms.radiosonline.domain.usecase.RadioStationsFavoritesUseCase
import br.com.gms.radiosonline.domain.usecase.RadioStationsListUseCase
import br.com.gms.radiosonline.mediaplayer.MediaPlayerServiceConnection
import br.com.gms.radiosonline.mediaplayer.extractRadioModel
import br.com.gms.radiosonline.presentation.navigation.radioIdParam
import br.com.gms.radiosonline.presentation.screens.radio_stations.RadioStationsUiState
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
    private val favoritesUseCase: RadioStationsFavoritesUseCase,
    private val serviceConnection: MediaPlayerServiceConnection,
) : ViewModel() {


    private var _radioId: String? = null
    private var _currentMediaId: String? = null
    private var _currentMediaItem: RadioModel? = null

    private val _currentPlayingMedia: MutableLiveData<RadioModel?> = MutableLiveData()
    val currentPlayingMedia: LiveData<RadioModel?> get() = _currentPlayingMedia

    private val _playbackState: MutableLiveData<PlaybackStateCompat?> = MutableLiveData(null)
    val playbackState: LiveData<PlaybackStateCompat?> get() = _playbackState

    private var _playingNowUiState = MutableStateFlow<PlayingNowUiState>(PlayingNowUiState.Nothing)
    val playingNowUiState: StateFlow<PlayingNowUiState> get() = _playingNowUiState

    private val subscriptionCallback = object : MediaBrowserCompat.SubscriptionCallback() {
        override fun onChildrenLoaded(parentId: String, items: MutableList<MediaBrowserCompat.MediaItem>) {
            super.onChildrenLoaded(parentId, items)
            _currentMediaItem = items.map { it.description.extractRadioModel() }.firstOrNull()
            _playbackState.value = serviceConnection.mediaController.playbackState
            _currentMediaId = _currentMediaItem?.id
            updateTheCurrentPlaybackMediaOrStartANewPlayback()
        }
    }

    private val currentMediaMetadataObserver = Observer<MediaMetadataCompat?> {
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
        _radioId = state.get<String>(radioIdParam)
    }

    fun play(radioModel: RadioModel) {
        serviceConnection.play(radioModel)
    }

    fun pause() {
        serviceConnection.pause()
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


    fun addOrRemoveRadioStationFromFavorites(radioModel: RadioModel) {
        viewModelScope.launch {
            if (_playingNowUiState.value is PlayingNowUiState.Success) {

                val state = (_playingNowUiState.value as PlayingNowUiState.Success)
                val radioStation = radioModel.copy(isFavorite = !radioModel.isFavorite)
                val hasBeenAddedOrRemovedSuccessfully = favoritesUseCase.addOrRemoveRadioStationFromFavorites(radioStation)

                if (hasBeenAddedOrRemovedSuccessfully) {
                    _playingNowUiState.value = state.copy(
                        radioStation = radioStation
                    )
                }
            }
        }
    }

    private fun updateTheCurrentPlaybackMediaOrStartANewPlayback() {
        viewModelScope.launch {
            if (_radioId == null || (_currentMediaId != null && _radioId == _currentMediaId)) {
                _currentMediaItem?.let { _radioModel ->
                    _currentPlayingMedia.value = _radioModel
                    _playingNowUiState.update {
                        PlayingNowUiState.Success(radioStation = _radioModel)
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