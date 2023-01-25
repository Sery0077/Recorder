package sery.vlasenko.recorder.ui.record

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import sery.vlasenko.recorder.R
import sery.vlasenko.recorder.database.RecordDatabaseDAO
import sery.vlasenko.recorder.databinding.FragmentRecordBinding
import sery.vlasenko.recorder.ui.MainActivity
import sery.vlasenko.recorder.utils.services.RecordService
import java.io.File


class RecordFragment : Fragment() {

    private val viewModel by viewModels<RecordViewModel>()

    private lateinit var mainActivity: MainActivity
    private var database: RecordDatabaseDAO? = null

    private lateinit var binding: FragmentRecordBinding

    private val requestPermission =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            if (isGranted) {
                onRecord(true)
                viewModel.startTimer()
            } else {
                Toast.makeText(
                    requireContext(),
                    getString(R.string.toast_record_permission),
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentRecordBinding.inflate(
            inflater,
            container,
            false
        ).apply {
            viewModel = this@RecordFragment.viewModel
        }

        mainActivity = activity as MainActivity

        createChannel(
            getString(R.string.notification_channel_id),
            getString(R.string.notification_channel_name)
        )

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.lifecycleOwner = this.viewLifecycleOwner

        setClickers()
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onStart() {
        if (!mainActivity.isServiceRunning()) {
            viewModel.resetTimer()
        } else {
            binding.btnPlay.setImageResource(R.drawable.ic_media_stop)
        }
        super.onStart()
    }

    private fun setClickers() {
        binding.btnPlay.setOnClickListener {
            if (ContextCompat.checkSelfPermission(
                    requireContext(),
                    Manifest.permission.RECORD_AUDIO
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                requestPermission.launch(Manifest.permission.RECORD_AUDIO)
            } else {
                if (mainActivity.isServiceRunning()) {
                    onRecord(false)
                    viewModel.stopTimer()
                } else {
                    onRecord(true)
                    viewModel.startTimer()
                }
            }
        }
    }

    private fun onRecord(start: Boolean) {
        val intent = Intent(activity, RecordService::class.java)

        if (start) {
            binding.btnPlay.setImageResource(R.drawable.ic_media_stop)

            Toast.makeText(
                requireContext(),
                getString(R.string.toast_recording_start),
                Toast.LENGTH_SHORT
            ).show()

            val folder =
                File(activity?.getExternalFilesDir(null)?.absolutePath.toString() + "/Recorder")

            if (!folder.exists()) {
                folder.mkdir()
            }

            activity?.startService(intent)
            activity?.window?.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        } else {
            binding.btnPlay.setImageResource(R.drawable.ic_mic)

            activity?.stopService(intent)
            activity?.window?.clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        }
    }

    private fun createChannel(channelId: String, channelName: String) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationChannel = NotificationChannel(
                channelId,
                channelName,
                NotificationManager.IMPORTANCE_DEFAULT
            ).apply {
                setShowBadge(false)
                setSound(null, null)
            }

            val notificationManager = requireActivity().getSystemService(
                NotificationManager::class.java
            )
            notificationManager.createNotificationChannel(notificationChannel)
        }
    }
}