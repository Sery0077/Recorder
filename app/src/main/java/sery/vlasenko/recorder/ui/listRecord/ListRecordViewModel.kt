package sery.vlasenko.recorder.ui.listRecord

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import sery.vlasenko.recorder.database.RecordDatabaseDAO
import sery.vlasenko.recorder.utils.Event

class ListRecordViewModel(
    private val dataSource: RecordDatabaseDAO
) : ViewModel() {

    val records = dataSource.getAllRecords()

    private val _openRecordEvent = MutableLiveData<Event<String>>()
    val openRecordEvent: LiveData<Event<String>>
        get() = _openRecordEvent

    fun onRecordClick(filePath: String) {
        _openRecordEvent.value = Event(filePath)
    }
}