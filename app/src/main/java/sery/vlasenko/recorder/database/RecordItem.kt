package sery.vlasenko.recorder.database

import androidx.databinding.BindingConversion
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.concurrent.TimeUnit

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

@BindingConversion
fun convertLengthToString(length: Long): String {
    val minutes = TimeUnit.MILLISECONDS.toMinutes(length)
    val seconds = (TimeUnit.MILLISECONDS.toSeconds(length)
            - TimeUnit.MINUTES.toSeconds(minutes))

    return String.format("%02d:%02d", minutes, seconds)
}
