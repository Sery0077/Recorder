package sery.vlasenko.recorder.ui.player

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import com.google.android.exoplayer2.ui.PlayerControlView
import sery.vlasenko.recorder.R
import sery.vlasenko.recorder.databinding.FragmentPlayerBinding
import sery.vlasenko.recorder.utils.EventObserver

class PlayerFragment : DialogFragment() {

    companion object {
        const val TAG = "player_fragment"
        private const val ARG_ITEM_PATH = "record_item_path"

        fun newInstance(itemPath: String): PlayerFragment {
            val f = PlayerFragment()

            val b = Bundle().apply {
                putString(ARG_ITEM_PATH, itemPath)
            }
            f.arguments = b

            return f
        }
    }

    private lateinit var binding: FragmentPlayerBinding

    private val viewModel: PlayerViewModel by viewModels {
        PlayerViewModelFactory(
            itemPath,
            requireActivity().application
        )
    }

    private var itemPath: String = ""

    override fun onAttach(context: Context) {
        super.onAttach(context)
        itemPath = requireArguments().getString(ARG_ITEM_PATH, "")
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentPlayerBinding.inflate(inflater, container, false).apply {
            viewModel = this@PlayerFragment.viewModel
        }

        viewModel.player.observe(viewLifecycleOwner) {
            binding.playerView.player = it
        }

        return binding.root
    }
}