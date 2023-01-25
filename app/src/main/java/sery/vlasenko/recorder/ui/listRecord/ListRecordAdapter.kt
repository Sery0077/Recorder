package sery.vlasenko.recorder.ui.listRecord

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import sery.vlasenko.recorder.database.RecordItem
import sery.vlasenko.recorder.databinding.RecordItemBinding

class ListRecordAdapter(private val viewModel: ListRecordViewModel) :
    ListAdapter<RecordItem, ListRecordAdapter.RecordViewHolder>(
        RecordDiffCallback()
    ) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecordViewHolder =
        RecordViewHolder.from(parent)

    override fun onBindViewHolder(holder: RecordViewHolder, position: Int) {
        val item = getItem(position)

        holder.bind(viewModel, item)
    }

    class RecordViewHolder private constructor(private val binding: RecordItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(viewModel: ListRecordViewModel, item: RecordItem) {
            binding.viewModel = viewModel
            binding.record = item
        }

        companion object {
            fun from(parent: ViewGroup): RecordViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = RecordItemBinding.inflate(layoutInflater, parent, false)

                return RecordViewHolder(binding)
            }
        }
    }
}

class RecordDiffCallback : DiffUtil.ItemCallback<RecordItem>() {
    override fun areItemsTheSame(oldItem: RecordItem, newItem: RecordItem): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: RecordItem, newItem: RecordItem): Boolean {
        return oldItem == newItem
    }
}