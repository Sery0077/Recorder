package sery.vlasenko.recorder.ui.listRecord

import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import sery.vlasenko.recorder.database.RecordItem

@BindingAdapter("app:items")
fun setItems(listView: RecyclerView, items: List<RecordItem>?) {
    items?.let {
        (listView.adapter as ListRecordAdapter).submitList(items)
    }
}