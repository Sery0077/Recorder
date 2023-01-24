package sery.vlasenko.recorder.utils.services

import android.app.Notification
import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.media.MediaRecorder
import android.os.Build
import android.os.IBinder
import android.util.Log
import android.widget.Toast
import androidx.core.app.NotificationCompat
import kotlinx.coroutines.*
import sery.vlasenko.recorder.R
import sery.vlasenko.recorder.database.RecordDatabase
import sery.vlasenko.recorder.database.RecordDatabaseDAO
import sery.vlasenko.recorder.database.RecordItem
import sery.vlasenko.recorder.ui.MainActivity
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

class RecordService : Service() {

    private var mFileName: String? = null
    private var mFilePath: String? = null
    private var mCountRecords: Int? = null

    private var mRecorder: MediaRecorder? = null

    private var mStartingTimeMillis: Long = 0
    private var mElapsedMillis: Long = 0

    private var mDatabase: RecordDatabaseDAO? = null

    private val mJob = Job()
    private val mUiScope = CoroutineScope(Dispatchers.Main + mJob)

    companion object {
        var isRunning = false
    }

    override fun onBind(intent: Intent): IBinder? {
        return null
    }

    override fun onCreate() {
        super.onCreate()
        mDatabase = RecordDatabase.getInstance(application).recordDatabaseDAO
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        mCountRecords = intent?.extras?.getInt("COUNT")
        startRecording()
        isRunning = true
        return START_NOT_STICKY
    }

    private fun startRecording() {
        setFileNameAndPath()

        mRecorder = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S)
            MediaRecorder(this)
        else
            MediaRecorder()

        mRecorder?.run {
            setAudioSource(MediaRecorder.AudioSource.MIC)
            setOutputFormat(MediaRecorder.OutputFormat.MPEG_4)
            setOutputFile(mFilePath)
            setAudioEncoder(MediaRecorder.AudioEncoder.AAC)
            setAudioChannels(1)
            setAudioEncodingBitRate(192_000)
        }

        try {
            mRecorder?.prepare()
            mRecorder?.start()

            mStartingTimeMillis = System.currentTimeMillis()
            startForeground(1, createNotification())
        } catch (e: IOException) {
            Log.e("RecordService", e.toString())
        }
    }

    private fun createNotification(): Notification {
        val mBuilder: NotificationCompat.Builder =
            NotificationCompat.Builder(
                applicationContext,
                getString(R.string.notification_channel_id)
            )
                .setSmallIcon(R.drawable.ic_mic)
                .setContentTitle(getString(R.string.app_name))
                .setContentText(getString(R.string.nofication_recording))
                .setOngoing(true)

        mBuilder.setContentIntent(
            PendingIntent.getActivities(
                applicationContext, 0, arrayOf(
                    Intent(applicationContext, MainActivity::class.java)
                ), 0
            )
        )

        return mBuilder.build()
    }

    private fun setFileNameAndPath() {
        var count = 0
        var f: File
        val dateTime = SimpleDateFormat(
            "yyyy_MM_dd_HH_mm_ss",
            Locale.getDefault()
        ).format(System.currentTimeMillis())

        do {
            mFileName = (getString(R.string.default_file_name)) + "_" + dateTime + count + ".mp4"
            mFilePath = application.getExternalFilesDir(null)?.absolutePath + "/$mFileName"

            count++

            f = File(mFilePath)
        } while (f.exists() && !f.isDirectory)
    }

    private fun stopRecording() {
        val recordItem = RecordItem()

        mRecorder?.stop()
        mElapsedMillis = System.currentTimeMillis() - mStartingTimeMillis
        mRecorder?.release()

        Toast.makeText(
            this,
            getString(R.string.toast_recording_finish),
            Toast.LENGTH_SHORT
        ).show()

        recordItem.run {
            name = mFileName.toString()
            filePath = mFilePath.toString()
            length = mElapsedMillis
            time = System.currentTimeMillis()
        }

        mRecorder = null

        try {
            mUiScope.launch {
                withContext(Dispatchers.IO) {
                    mDatabase?.insert(recordItem)
                }
            }
        } catch (e: Exception) {
            Log.e("RecordService", "exception", e)
        }
    }

    override fun onDestroy() {
        if (mRecorder != null) {
            stopRecording()
        }
        isRunning = false
        super.onDestroy()
    }
}