package com.app.timerz.ui.timerlist

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.app.timerz.R
import com.app.timerz.data.local.database.entity.Timer
import com.app.timerz.databinding.TimerListItemBinding
import kotlinx.android.synthetic.main.timer_list_item.view.*
import timber.log.Timber

class TimerListAdapter(
    private var timerItemListener: TimerItemListener
) : RecyclerView.Adapter<TimerListAdapter.TimerListViewHolder>() {

    private var timerList: ArrayList<Timer> = ArrayList()

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

    fun setData(newTimerList: List<Timer>) {
        Timber.d("timer list : $newTimerList")

        val diffUtilCallBack = TimerListDiffUtilCallback(timerList, newTimerList)

        val diffResult = DiffUtil.calculateDiff(diffUtilCallBack)

        timerList.clear()

        timerList.addAll(newTimerList)

        diffResult.dispatchUpdatesTo(this)
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
            Timber.d("timer title : ${item.title}")

            //set the data on the current view
            binding.timerName.text = item.title
            binding.timerValue.text = item.timerValue
        }

    }

    inner class TimerListDiffUtilCallback(private val oldList: List<Timer>, private val newList: List<Timer>) :
        DiffUtil.Callback() {

        override fun getOldListSize(): Int {
            return oldList.size
        }

        override fun getNewListSize(): Int {
            return newList.size
        }

        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldList[oldItemPosition].id == newList[newItemPosition].id
        }

        //use visual elements to define equality
        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            val oldItem = oldList[oldItemPosition]
            val newItem = newList[newItemPosition]
            return oldItem.title == newItem.title
        }

    }

}