package sery.vlasenko.recorder.ui.listRecord

import androidx.lifecycle.ViewModel
import sery.vlasenko.recorder.database.RecordDatabaseDAO

class ListRecordViewModel(
    private val dataSource: RecordDatabaseDAO
) : ViewModel() {

    val records = dataSource.getAllRecords()


}