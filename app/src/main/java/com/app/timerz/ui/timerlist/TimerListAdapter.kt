package com.app.timerz.ui.timerlist

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.app.timerz.R
import com.app.timerz.data.local.database.entity.Timer
import com.app.timerz.databinding.TimerListItemBinding

class TimerListAdapter(
    private var timerList: List<Timer>,
    private var timerItemListener: TimerItemListener
) : RecyclerView.Adapter<TimerListAdapter.TimerListViewHolder>() {

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
        val currentItem = timerList[position]
        holder.bind(currentItem)
    }

    override fun getItemCount(): Int {
        return timerList.size
    }

    inner class TimerListViewHolder(var binding: TimerListItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        init {
            binding.start.setOnClickListener {
                timerItemListener.onStartTimerClicked(timerList[absoluteAdapterPosition])
            }

            binding.edit.setOnClickListener {
                timerItemListener.onEditTimerClicked(timerList[absoluteAdapterPosition])
            }

            binding.delete.setOnClickListener {
                timerItemListener.onDeleteTimerClicked(timerList[absoluteAdapterPosition])
            }
        }

        fun bind(item: Timer) {
            //set the data on the current view
        }

    }
}