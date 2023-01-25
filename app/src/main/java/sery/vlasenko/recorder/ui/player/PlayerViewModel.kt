package sery.vlasenko.recorder.ui.player

import android.app.Application
import android.net.Uri
import androidx.lifecycle.*
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.upstream.DefaultDataSource

class PlayerViewModel(itemPath: String, app: Application) :
    AndroidViewModel(app),
    LifecycleEventObserver {

    private val _player = MutableLiveData<Player?>()
    val player: LiveData<Player?>
        get() = _player

    private var contentPosition = 0L
    private var playWhenReady = true
    var itemPath: String? = itemPath

    init {
        ProcessLifecycleOwner.get().lifecycle.addObserver(this)
    }

    override fun onStateChanged(source: LifecycleOwner, event: Lifecycle.Event) {
        when (event) {
            Lifecycle.Event.ON_START -> onStart()
            Lifecycle.Event.ON_STOP -> onStop()
            else -> {}
        }
    }

    private fun onStop() {
        releasePlayer()
    }

    private fun onStart() {
        setUpPlayer()
    }

    private fun setUpPlayer() {
        val dataSourceFactory = DefaultDataSource.Factory(getApplication())

        val mediaSource = ProgressiveMediaSource.Factory(dataSourceFactory)
            .createMediaSource(MediaItem.fromUri(Uri.parse(itemPath)))

        val player = ExoPlayer.Builder(getApplication()).build().apply {
            setMediaSource(mediaSource)
            seekTo(contentPosition)
            playWhenReady = this@PlayerViewModel.playWhenReady
            prepare()
        }

        _player.value = player
    }

    private fun releasePlayer() {
        val player = _player.value ?: return
        _player.value = null

        contentPosition = player.contentPosition
        playWhenReady = player.playWhenReady
        player.release()
    }

    override fun onCleared() {
        super.onCleared()
        releasePlayer()
        ProcessLifecycleOwner.get().lifecycle.removeObserver(this)
    }
}