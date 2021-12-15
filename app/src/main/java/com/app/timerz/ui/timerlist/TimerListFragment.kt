package com.app.timerz.ui.timerlist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.app.timerz.R
import com.app.timerz.data.local.database.entity.Timer
import com.app.timerz.databinding.FragmentTimerListBinding
import timber.log.Timber

class TimerListFragment : Fragment(), TimerListAdapter.TimerItemListener {

    private lateinit var binding: FragmentTimerListBinding
    private lateinit var timerAdapter: TimerListAdapter
    private lateinit var recyclerView: RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_timer_list, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        timerAdapter = TimerListAdapter(
            listOf<Timer>(
                Timer(1, "Plank workout timer", "00:01:33", "",""),
                Timer(2, "workout timer", "00:06:30", "",""),
                Timer(3, "legs workout timer", "00:00:43", "","")
            ),
            this
        )

        recyclerView = binding.timerListRecyclerview

        recyclerView.apply {
            layoutManager = LinearLayoutManager(requireActivity())
            adapter = timerAdapter
        }

        binding.createTimer.setOnClickListener{
            val action =
                TimerListFragmentDirections.actionTimerListFragmentToAddTimerFragment(null)

            findNavController().navigate(action)
        }

    }

    override fun onStartTimerClicked(timerItem: Timer) {
        Timber.d("timer start")

        val action =
            TimerListFragmentDirections.actionTimerListFragmentToActiveTimerFragment(
                timerItem.timerValue,
                timerItem.title
            )

        findNavController().navigate(action)
    }

    override fun onEditTimerClicked(timerItem: Timer) {
        Timber.d("timer edit")

        val action =
            TimerListFragmentDirections.actionTimerListFragmentToAddTimerFragment(timerItem)

        findNavController().navigate(action)
    }

    override fun onDeleteTimerClicked(timerItem: Timer) {
        Timber.d("timer deleted")
    }
}