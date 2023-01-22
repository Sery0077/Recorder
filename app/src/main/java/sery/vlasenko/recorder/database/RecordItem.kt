package sery.vlasenko.recorder.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "recording_table")
data class RecordItem(
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0L,
    @ColumnInfo(name = "name")
    var name: String = "",
    @ColumnInfo(name = "filePath")
    var filePath: String = "",
    @ColumnInfo(name = "length")
    var length: Long = 0L,
    @ColumnInfo(name = "time")
    var time: Long = 0L
)
