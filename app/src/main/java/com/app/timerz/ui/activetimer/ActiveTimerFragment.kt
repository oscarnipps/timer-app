package com.app.timerz.ui.activetimer

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.app.timerz.R
import com.app.timerz.databinding.FragmentActiveTimerBinding
import com.app.timerz.databinding.FragmentSettingsBinding
import timber.log.Timber

class ActiveTimerFragment : Fragment() {

    lateinit var binding: FragmentActiveTimerBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_active_timer, container, false)
        return binding.root
    }

    override fun onPause() {
        super.onPause()
        Timber.d("onPause")
    }

    override fun onResume() {
        super.onResume()
        Timber.d("onResume")
    }

    override fun onStop() {
        super.onStop()
        Timber.d("onStop")
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }
}