package com.app.timerz.ui.timerlist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.app.timerz.R
import com.app.timerz.data.local.database.entity.Timer
import com.app.timerz.databinding.FragmentTimerListBinding
import com.app.timerz.ui.home.HomeViewModel
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber
import android.app.ActivityManager
import android.content.Context
import android.content.Context.ACTIVITY_SERVICE
import android.widget.Toast
import androidx.core.content.ContextCompat

import androidx.core.content.ContextCompat.getSystemService
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import androidx.navigation.NavBackStackEntry


@AndroidEntryPoint
class TimerListFragment : Fragment(), TimerListAdapter.TimerItemListener {

    private lateinit var binding: FragmentTimerListBinding
    private lateinit var timerAdapter: TimerListAdapter
    private lateinit var recyclerView: RecyclerView
    private val timerViewModel : TimerListViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_timer_list, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val navBackStackEntry = findNavController().getBackStackEntry(R.id.timerListFragment)

        val observer = LifecycleEventObserver { source, event ->
            if (event == Lifecycle.Event.ON_RESUME && navBackStackEntry.savedStateHandle.contains("key")) {
                val result = navBackStackEntry.savedStateHandle.get<Int>("key")
                Timber.d("create timer with row count : $result")
                Toast.makeText(requireContext(),"created : $result",Toast.LENGTH_SHORT).show()
            }
        }

        navBackStackEntry.lifecycle.addObserver(observer)

        viewLifecycleOwner.lifecycle.addObserver(LifecycleEventObserver{source , event ->
            if (event == Lifecycle.Event.ON_DESTROY) {
                navBackStackEntry.lifecycle.removeObserver(observer)
            }
        })

       /* listOf(
            Timer(1, "Plank workout timer", "00:01:33", "",""),
            Timer(2, "workout timer", "00:06:30", "",""),
            Timer(3, "legs workout timer", "00:00:43", "","")
        )*/

        timerAdapter = TimerListAdapter(
            emptyList(),
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

        binding.deleteTimers.setOnClickListener{
            //timerViewModel.deleteAllTimers()
        }

        timerViewModel.getTimersList().observe(viewLifecycleOwner , {timerList ->
            Timber.d("timer list : $timerList")

            timerAdapter.setData(timerList)

        })

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

        //timerViewModel.deleteTimer(timerItem.id)
    }


    private fun isServiceRunning(serviceClass: Class<*>): Boolean {
        val manager = getSystemService(requireContext(),serviceClass) as ActivityManager?

        for (service in manager!!.getRunningServices(Int.MAX_VALUE)) {
            if (serviceClass.name == service.service.className) {
                return true
            }

        }
        return false
    }

/*    override fun onCreateTimerButtonClicked(id: Int) {
        findNavController().currentBackStackEntry?.savedStateHandle?.getLiveData<Int>("id")?.observe(viewLifecycleOwner,{
            Toast.makeText(requireContext(),"created" , Toast.LENGTH_SHORT).show()
        })
    }*/

}