package sery.vlasenko.recorder.ui.listRecord

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import sery.vlasenko.recorder.database.RecordDatabase
import sery.vlasenko.recorder.databinding.FragmentListRecordBinding

class ListRecordFragment : Fragment() {

    private val viewModel by viewModels<ListRecordViewModel> {
        ListRecordViewModelFactory(
            RecordDatabase.getInstance(requireActivity().application).recordDatabaseDAO
        )
    }

    private lateinit var viewDataBinding: FragmentListRecordBinding

    private lateinit var listAdapter: ListRecordAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewDataBinding = FragmentListRecordBinding.inflate(
            inflater,
            container,
            false
        ).apply {
            viewModel = this@ListRecordFragment.viewModel
        }

        return viewDataBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewDataBinding.lifecycleOwner = this.viewLifecycleOwner

        setupListAdapter()
    }

    private fun setupListAdapter() {
        val viewModel = viewDataBinding.viewModel
        if (viewModel != null) {
            listAdapter = ListRecordAdapter(viewModel)
            viewDataBinding.recyclerView.adapter = listAdapter
        } else {
            throw IllegalStateException("ViewMode not initialized when attempting to set up adapter")
        }
    }
}