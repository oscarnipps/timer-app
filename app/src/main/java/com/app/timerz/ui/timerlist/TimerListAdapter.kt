package com.app.timerz.ui.timerlist

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.app.timerz.R
import com.app.timerz.data.local.database.entity.Timer
import com.app.timerz.databinding.TimerListItemBinding
import kotlinx.android.synthetic.main.timer_list_item.view.*
import timber.log.Timber

class TimerListAdapter(
    private var timerItemListener: TimerItemListener
) : ListAdapter<Timer, TimerListAdapter.TimerListViewHolder>(Diff()) {

    interface TimerItemListener {
        fun onStartTimerClicked(timerItem: Timer)

        fun onEditTimerClicked(timerItem: Timer)

        fun onDeleteTimerClicked(timerItem: Timer)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TimerListViewHolder {
        val binding = DataBindingUtil.inflate<TimerListItemBinding>(
            LayoutInflater.from(parent.context),
            R.layout.timer_list_item,
            parent,
            false
        )

        return TimerListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TimerListViewHolder, position: Int) {
        val currentItem = getItem(position)
        holder.bind(currentItem)
    }

    inner class TimerListViewHolder(var binding: TimerListItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        init {
            binding.start.setOnClickListener {
                timerItemListener.onStartTimerClicked(getItem(absoluteAdapterPosition))
            }

            binding.edit.setOnClickListener {
                timerItemListener.onEditTimerClicked(getItem(absoluteAdapterPosition))
            }

            binding.delete.setOnClickListener {
                timerItemListener.onDeleteTimerClicked(getItem(absoluteAdapterPosition))
            }
        }

        fun bind(item: Timer) {
            binding.timerName.text = item.title
            binding.timerValue.text = item.timerValue
        }

    }

    class Diff : DiffUtil.ItemCallback<Timer>() {
        override fun areItemsTheSame(oldItem: Timer, newItem: Timer): Boolean {
            return oldItem.id  == newItem.id
        }

        override fun areContentsTheSame(oldItem: Timer, newItem: Timer): Boolean {
            return oldItem.title == newItem.title && oldItem.timerValue == newItem.timerValue
        }
    }

}