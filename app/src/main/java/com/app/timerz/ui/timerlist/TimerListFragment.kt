package com.app.timerz.ui.timerlist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.app.timerz.R
import com.app.timerz.databinding.FragmentTimerListBinding

class TimerListFragment : Fragment() {

    lateinit var binding: FragmentTimerListBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_timer_list, container, false)
        return binding.root
    }
}