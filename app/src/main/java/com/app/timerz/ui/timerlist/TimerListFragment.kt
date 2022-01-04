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
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber
import android.app.ActivityManager
import android.media.MediaPlayer
import android.media.audiofx.BassBoost
import android.widget.Toast

import androidx.core.content.ContextCompat.getSystemService
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.Observer
import com.app.timerz.data.Resource
import android.media.RingtoneManager


@AndroidEntryPoint
class TimerListFragment : Fragment(), TimerListAdapter.TimerItemListener {

    private lateinit var binding: FragmentTimerListBinding
    private lateinit var timerAdapter: TimerListAdapter
    private lateinit var recyclerView: RecyclerView
    private val timerViewModel: TimerListViewModel by viewModels()

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

       /* val navBackStackEntry = findNavController().getBackStackEntry(R.id.timerListFragment)

        val observer = LifecycleEventObserver { source, event ->
            if (event == Lifecycle.Event.ON_RESUME && navBackStackEntry.savedStateHandle.contains("key")) {
                val result = navBackStackEntry.savedStateHandle.get<Int>("key")
                Timber.d("create timer with row count : $result")
                Toast.makeText(requireContext(), "created : $result", Toast.LENGTH_SHORT).show()
            }
        }

        navBackStackEntry.lifecycle.addObserver(observer)

        viewLifecycleOwner.lifecycle.addObserver(LifecycleEventObserver { source, event ->
            if (event == Lifecycle.Event.ON_DESTROY) {
                navBackStackEntry.lifecycle.removeObserver(observer)
            }
        })*/

        timerAdapter = TimerListAdapter(this)

        recyclerView = binding.timerListRecyclerview

        recyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = timerAdapter
        }

        binding.createTimer.setOnClickListener {
            val action =
                TimerListFragmentDirections.actionTimerListFragmentToAddTimerFragment(null)

            findNavController().navigate(action)

            //playNotificationSound()
        }

        binding.deleteTimers.setOnClickListener {
            timerViewModel.deleteAllTimers()
        }

        timerViewModel.getTimersList().observe(viewLifecycleOwner, { timerList ->
            Timber.d("timers : $timerList")

            if (!timerList.isEmpty()) {
                binding.emptyState.visibility = View.GONE
                timerAdapter.submitList(timerList.toList())
                timerAdapter.notifyDataSetChanged()
                return@observe
            }

            binding.emptyState.visibility = View.VISIBLE
        })

        timerViewModel.databaseEvent().observe(viewLifecycleOwner, databaseEventObserver())

    }

    private fun databaseEventObserver(): Observer<Resource<Int>> {
        return Observer { result ->
            when (result.status) {

                Resource.Status.LOADING -> {
                    //todo : show progress bar
                }

                Resource.Status.ERROR -> {
                    val messageResId = result.messageResId ?: R.string.error_message
                    Toast.makeText(requireContext(), messageResId, Toast.LENGTH_SHORT).show()
                }

                Resource.Status.SUCCESS -> {
                    Timber.d("res id ${result.messageResId}")
                    Toast.makeText(requireContext(), result.messageResId!!, Toast.LENGTH_SHORT)
                        .show()
                }
            }
        }
    }

    override fun onStartTimerClicked(timerItem: Timer) {
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
        timerViewModel.deleteTimer(timerItem.id)
    }


    private fun isServiceRunning(serviceClass: Class<*>): Boolean {
        val manager = getSystemService(requireContext(), serviceClass) as ActivityManager?

        for (service in manager!!.getRunningServices(Int.MAX_VALUE)) {
            if (serviceClass.name == service.service.className) {
                return true
            }

        }
        return false
    }
}