package com.app.timerz.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.NumberPicker
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.app.timerz.R
import com.app.timerz.databinding.FragmentHomeBinding
import timber.log.Timber

class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding
    private val homeViewModel: HomeViewModel by viewModels()
    private lateinit var hourPicker: NumberPicker
    private lateinit var minutePicker: NumberPicker
    private lateinit var secondsPicker: NumberPicker
    private var hourDuration = "00"
    private var minuteDuration = "00"
    private var secondDuration = "00"
    private lateinit var hourPickerValues: Array<String>
    private lateinit var minutePickerValues: Array<String>
    private lateinit var secondPickerValues: Array<String>


    companion object {
        private const val MAX_HOUR_VALUE = 100
        private const val MAX_MINUTE_VALUE = 59
        private const val MAX_SECOND_VALUE = 59
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Timber.d("onCreate value : ${savedInstanceState?.getString("timer-value")}")

        getPickerValues()

        Timber.d("onSaveInstanceState")

        savedInstanceState?.putString("timer-value", getTimerDuration())
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false)

        binding.lifecycleOwner = viewLifecycleOwner

        binding.viewModel = homeViewModel

        hourPicker = binding.hourPicker

        minutePicker = binding.minutePicker

        secondsPicker = binding.secondsPicker

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        Timber.d("timer value from saved state handle : ${homeViewModel.getTimerValue()}")

        setUpHourPicker()

        setUpMinutePicker()

        setUpSecondPicker()

        binding.startTimerButton.setOnClickListener {
            homeViewModel.setTimerValue(getTimerDuration())

            val action =
                HomeFragmentDirections.actionHomeFragmentToActiveTimerFragment(getTimerDuration())

            findNavController().navigate(action)
        }

        homeViewModel.isTimerValueValid().observe(viewLifecycleOwner, { isTimerValueValid ->
            Timber.d("timer value valid : $isTimerValueValid")

            if (isTimerValueValid == false) {
                updateButtonBackground(R.color.color_button_disabled)
                return@observe
            }

            updateButtonBackground(R.color.color_button_enabled)
        })
    }

    private fun updateButtonBackground(backgroundColor: Int) {
        binding.startTimerButton.backgroundTintList =
            ContextCompat.getColorStateList(requireActivity(), backgroundColor)
    }

    private fun getPickerValues() {
        hourPickerValues = getHourPickerValues(MAX_HOUR_VALUE).toTypedArray()

        minutePickerValues = getHourPickerValues(MAX_MINUTE_VALUE).toTypedArray()

        secondPickerValues = getHourPickerValues(MAX_SECOND_VALUE).toTypedArray()
    }

    private fun setUpSecondPicker() {
        secondsPicker.apply {
            minValue = 0

            maxValue = secondPickerValues.size - 1

            displayedValues = secondPickerValues

            setOnValueChangedListener { numberPicker, oldValuePosPos, newValuePos ->
                secondDuration = secondPickerValues[newValuePos]

                Timber.d("second selected $secondDuration")

                homeViewModel.validateTimerValue(getTimerDuration())
            }
        }
    }

    private fun setUpMinutePicker() {
        minutePicker.apply {
            minValue = 0

            maxValue = minutePickerValues.size - 1

            displayedValues = minutePickerValues

            setOnValueChangedListener { numberPicker, oldValuePos, newValuePos ->
                minuteDuration = minutePickerValues[newValuePos]

                Timber.d("minute selected $minuteDuration")

                homeViewModel.validateTimerValue(getTimerDuration())
            }
        }
    }

    private fun setUpHourPicker() {
        hourPicker.apply {
            minValue = 0

            maxValue = hourPickerValues.size - 1

            displayedValues = hourPickerValues

            setOnValueChangedListener { numberPicker, oldValuePos, newValuePos ->
                hourDuration = hourPickerValues[newValuePos]

                Timber.d("hour selected $hourDuration")

                homeViewModel.validateTimerValue(getTimerDuration())
            }
        }
    }

    private fun getTimerDuration(): String {
        return "$hourDuration:$minuteDuration:$secondDuration"
    }

    private fun getHourPickerValues(maxValue: Int): MutableList<String> {
        val values = mutableListOf<String>()

        for (value in 0..maxValue) {

            if (value < 10) {
                values.add("0$value")
                continue
            }

            values.add("$value")
        }

        Timber.d("value size : ${values.size}")

        return values
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        Timber.d("onSaveInstanceState")
        outState.putString("timer-value", getTimerDuration())


    }

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)

        val value =savedInstanceState?.getString("timer-value")
        Timber.d("onViewStateRestored")
        Timber.d("onViewStateRestored value : $value")
    }

    override fun onPause() {
        super.onPause()
        Timber.d("onPause")
    }

    override fun onStop() {
        super.onStop()
        Timber.d("onStop")

    }

    override fun onDestroy() {
        super.onDestroy()
        Timber.d("onDestroy")
    }

}