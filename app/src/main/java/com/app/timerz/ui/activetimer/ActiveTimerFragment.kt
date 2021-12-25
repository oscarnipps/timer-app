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
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.app.timerz.R
import com.app.timerz.data.Constants
import com.app.timerz.data.TimerService
import com.app.timerz.databinding.FragmentActiveTimerBinding
import com.app.timerz.util.TimerUtil
import timber.log.Timber

class ActiveTimerFragment : Fragment(), ServiceConnection {

    private lateinit var binding: FragmentActiveTimerBinding
    private val args: ActiveTimerFragmentArgs by navArgs()
    private var timerService: TimerService? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_active_timer, container, false)

        Timber.d("timer duration : ${args.timerDuration}")

        return binding.root
    }


    override fun onStart() {
        super.onStart()

        startTimerService()
    }

    private fun startTimerService() {
        val serviceIntent = Intent(requireActivity(), TimerService::class.java)

        serviceIntent.apply {
            putExtra("timer-value", args.timerDuration)
            putExtra("timer-title", args.timerTitle)
            action = Constants.ACTION_START_TIMER
        }

        requireActivity().startService(serviceIntent)

        requireActivity().bindService(serviceIntent, this, Context.BIND_AUTO_CREATE)
    }


    private fun updateTimerButtonUi(color: Int, text: CharSequence) {
        binding.pause.backgroundTintList = ContextCompat.getColorStateList(requireActivity(), color)
        binding.pause.text = text
    }

    private fun cancelTimer() {
        timerService?.cancelTimer()
        findNavController().popBackStack()
    }

    private fun restartTimer() {
        startTimerService()
        binding.restart.visibility = View.GONE
        binding.activeTimerControlGroup.visibility = View.VISIBLE
    }

    private fun pauseTimer() {
        if (timerService?.isTimerPaused!!) {

            updateTimerButtonUi(R.color.timer_paused_color, resources.getText(R.string.pause))

            timerService?.resumeTimer()

            return
        }

        updateTimerButtonUi(R.color.button_default, resources.getText(R.string.resume))

        timerService?.pauseTimer()
    }

    override fun onDestroy() {
        super.onDestroy()

        Timber.d("onDestroy")

            requireActivity().unbindService(this)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.timerTitle.text = args.timerTitle

        binding.cancel.setOnClickListener { cancelTimer() }

        binding.pause.setOnClickListener { pauseTimer() }

        binding.restart.setOnClickListener { restartTimer() }
    }

    override fun onServiceConnected(component: ComponentName?, binder: IBinder?) {
        Timber.d("timer service connected")

        val timerServiceBinder = binder as TimerService.TimerServiceBinder

        timerService = timerServiceBinder.getTimerService()

        setUpTimerValueObserver()

        updateTimerUi()
    }

    override fun onServiceDisconnected(p0: ComponentName?) {
        Timber.d("timer service disconnected")
    }

    private fun setUpTimerValueObserver() {
        timerService?.timerValueLiveData?.observe(viewLifecycleOwner, { timerValue ->

            updateTimerProgressBar(timerValue)

            binding.timerValue.text = timerValue

            if (timerValue == "00:00:00") {
                binding.timerValue.text = args.timerDuration

                binding.restart.visibility = View.VISIBLE

                binding.activeTimerControlGroup.visibility = View.GONE

                binding.timerProgress.progress = 100
            }
        })
    }

    private fun updateTimerUi() {
        if (timerService?.isTimerPaused!!) {
            updateTimerButtonUi(R.color.button_default, resources.getText(R.string.resume))
            return
        }

        updateTimerButtonUi(R.color.timer_paused_color, resources.getText(R.string.pause))
    }

    private fun updateTimerProgressBar(timerValue: String) {
        val time = TimerUtil.getTimerValueInMilliseconds(timerValue).toFloat()

        val total = TimerUtil.getTimerValueInMilliseconds(args.timerDuration)

        binding.timerProgress.progress = (time / total * 100).toInt()
    }


}