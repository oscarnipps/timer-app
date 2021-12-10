package com.app.timerz.ui.activetimer

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.Bundle
import android.os.IBinder
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.app.timerz.R
import com.app.timerz.data.TimerService
import com.app.timerz.databinding.FragmentActiveTimerBinding
import timber.log.Timber

class ActiveTimerFragment : Fragment(), ServiceConnection {

    private lateinit var binding: FragmentActiveTimerBinding
    private val args: ActiveTimerFragmentArgs by navArgs()
    private var timerService: TimerService? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_active_timer, container, false)

        Timber.d("timer duration : ${args.timerDuration}")


        return binding.root
    }


    override fun onPause() {
        super.onPause()
        Timber.d("onPause")
    }

    override fun onResume() {
        super.onResume()

        val serviceIntent = Intent(requireActivity(), TimerService::class.java)

        serviceIntent.apply {
            putExtra("timer-value" ,"00:00:45")
        }

        requireActivity().bindService(serviceIntent, this, Context.BIND_AUTO_CREATE)
    }

    override fun onStop() {
        super.onStop()
        Timber.d("onStop")
    }

    override fun onDestroy() {
        super.onDestroy()

        Timber.d("onDestroy")

        requireActivity().unbindService(this)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onServiceConnected(componentName: ComponentName?, binder: IBinder?) {
        Timber.d("timer service connected")

        val timerServiceBinder = binder as TimerService.TimerServiceBinder

        timerService = timerServiceBinder.getTimerService()

        updateTimerUi()
    }

    override fun onServiceDisconnected(componentName: ComponentName?) {
        Timber.d("timer service disconnected")
    }

    private fun updateTimerUi() {
        timerService?.timerValueLiveData?.observe(viewLifecycleOwner,{
            binding.minutePicker.text = it
        })
    }

}