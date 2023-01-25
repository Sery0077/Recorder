package sery.vlasenko.recorder.ui.listRecord

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import sery.vlasenko.recorder.database.RecordDatabaseDAO

class ListRecordViewModelFactory(
    private val dataSource: RecordDatabaseDAO
) : ViewModelProvider.Factory {
    @Suppress("unchecked_cast")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ListRecordViewModel::class.java)) {
            return ListRecordViewModel(dataSource) as T
        }
        throw IllegalStateException("Unknown ViewModel Class $modelClass")
    }
}