package sery.vlasenko.recorder.ui.player

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import sery.vlasenko.recorder.utils.UnknownViewModelClass

class PlayerViewModelFactory(
    private val itemPath: String,
    private val app: Application
): ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(PlayerViewModel::class.java)) {
            return PlayerViewModel(itemPath, app) as T
        }
        throw UnknownViewModelClass(modelClass.name)
    }
}