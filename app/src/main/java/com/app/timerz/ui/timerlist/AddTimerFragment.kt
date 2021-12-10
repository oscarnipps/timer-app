package com.app.timerz.ui.timerlist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.NumberPicker
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.navArgs
import com.app.timerz.R
import com.app.timerz.data.local.database.entity.Timer
import com.app.timerz.databinding.FragmentAddTimerBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import timber.log.Timber

class AddTimerFragment : BottomSheetDialogFragment() {

    private val args : AddTimerFragmentArgs by navArgs()
    private lateinit var binding : FragmentAddTimerBinding
    private lateinit var hourPicker: NumberPicker
    private lateinit var minutePicker: NumberPicker
    private lateinit var secondsPicker: NumberPicker
    private var hourDuration = "00"
    private var minuteDuration = "00"
    private var secondDuration = "00"
    private lateinit var hourPickerValues: Array<String>
    private lateinit var minutePickerValues: Array<String>
    private lateinit var secondPickerValues: Array<String>
    private var timerItem : Timer? = null
    
    companion object {
        private const val MAX_HOUR_VALUE = 100
        private const val MAX_MINUTE_VALUE = 59
        private const val MAX_SECOND_VALUE = 59
    }
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        getPickerValues()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_add_timer, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        timerItem = args.timerItem

        if (timerItem != null) {
            setUpTimerValue()
        }

    }

    private fun setUpTimerValue() {

        val hourMinuteSecond = timerItem!!.timerValue.split(":")

        hourDuration = hourMinuteSecond[0]

        minuteDuration = hourMinuteSecond[1]

        secondDuration = hourMinuteSecond[2]

        binding.timerTitle.setText(timerItem!!.title)
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

            value = secondPickerValues.indexOf(secondDuration)

            setOnValueChangedListener { numberPicker, oldValuePosPos, newValuePos ->
                secondDuration = secondPickerValues[newValuePos]

                Timber.d("second selected $secondDuration")

                
            }
        }
    }

    private fun setUpMinutePicker() {
        minutePicker.apply {
            minValue = 0

            maxValue = minutePickerValues.size - 1

            displayedValues = minutePickerValues

            value = minutePickerValues.indexOf(minuteDuration)

            setOnValueChangedListener { numberPicker, oldValuePos, newValuePos ->
                minuteDuration = minutePickerValues[newValuePos]

                Timber.d("minute selected $minuteDuration")

                
            }
        }
    }

    private fun setUpHourPicker() {
        hourPicker.apply {

            minValue = 0

            maxValue = hourPickerValues.size - 1

            value = hourPickerValues.indexOf(hourDuration)

            displayedValues = hourPickerValues

            setOnValueChangedListener { numberPicker, oldValuePos, newValuePos ->
                hourDuration = hourPickerValues[newValuePos]

                Timber.d("hour selected $hourDuration")

                
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

}