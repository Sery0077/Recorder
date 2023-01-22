package sery.vlasenko.recorder.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface RecordDatabaseDAO {
    @Insert
    fun insert(recordItem: RecordItem)

    @Update
    fun update(recordItem: RecordItem)

    @Query("SELECT * from recording_table WHERE id= :key")
    fun getRecord(key: Long?): RecordItem?

    @Query("DELETE FROM recording_table")
    fun deleteAll()

    @Query("DELETE FROM recording_table WHERE id= :key")
    fun deleteRecord(key: Long?)

    @Query("SELECT * FROM recording_table ORDER BY id DESC")
    fun getAllRecords(): LiveData<MutableList<RecordItem>>

    @Query("SELECT COUNT(*) FROM recording_table")
    fun getCount(): Int
}