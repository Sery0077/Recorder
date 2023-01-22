package sery.vlasenko.recorder

import androidx.room.Room
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import sery.vlasenko.recorder.database.RecordDatabase
import sery.vlasenko.recorder.database.RecordDatabaseDAO
import sery.vlasenko.recorder.database.RecordItem
import java.io.IOException

@RunWith(AndroidJUnit4::class)
class RecordDatabaseTest {
    private lateinit var recordDatabaseDAO: RecordDatabaseDAO
    private lateinit var database: RecordDatabase

    @Before
    fun createDb() {
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        database = Room.inMemoryDatabaseBuilder(context, RecordDatabase::class.java)
            .allowMainThreadQueries()
            .build()
        recordDatabaseDAO = database.recordDatabaseDAO
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        database.close()
    }

    @Test
    @Throws(Exception::class)
    fun testDatabase() {
        recordDatabaseDAO.insert(RecordItem())
        val getCount = recordDatabaseDAO.getCount()
        assert(getCount == 1)
    }
}