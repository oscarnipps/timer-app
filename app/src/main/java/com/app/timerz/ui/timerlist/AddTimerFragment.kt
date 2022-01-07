package com.app.timerz.ui.timerlist

import android.app.Dialog
import android.os.Bundle
import android.provider.SyncStateContract.Helpers.update
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.NumberPicker
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.app.timerz.R
import com.app.timerz.data.Resource
import com.app.timerz.data.local.database.entity.Timer
import com.app.timerz.databinding.FragmentAddTimerBinding
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*

@AndroidEntryPoint
class AddTimerFragment : BottomSheetDialogFragment() {

    private val args: AddTimerFragmentArgs by navArgs()
    private val viewModel: TimerListViewModel by viewModels()
    private lateinit var binding: FragmentAddTimerBinding
    private lateinit var hourPicker: NumberPicker
    private lateinit var minutePicker: NumberPicker
    private lateinit var secondsPicker: NumberPicker
    private var hourDuration = "00"
    private var minuteDuration = "00"
    private var secondDuration = "00"
    private lateinit var hourPickerValues: Array<String>
    private lateinit var minutePickerValues: Array<String>
    private lateinit var secondPickerValues: Array<String>
    private var timerItem: Timer? = null


    companion object {
        private const val MAX_HOUR_VALUE = 100
        private const val MAX_MINUTE_VALUE = 59
        private const val MAX_SECOND_VALUE = 59
        private const val DATE_PATTERN = "yyyy-MM-dd hh:mm:ss"
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        getPickerValues()
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        return BottomSheetDialog(requireContext(),R.style.ThemeOverlay_Timerz_BottomSheetDialog)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_add_timer, container, false)

        hourPicker = binding.hourPicker

        minutePicker = binding.minutePicker

        secondsPicker = binding.secondsPicker

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.createTimer.setOnClickListener {
            viewModel.validateTimerInput(
                getTimerDuration(),
                binding.timerTitle.text.toString()
            )
        }

        viewModel.inputValidationResult().observe(viewLifecycleOwner, { isValidInput ->
            if (isValidInput) {
                saveTimer()
                return@observe
            }

            Toast.makeText(requireContext(), "invalid inputs", Toast.LENGTH_SHORT).show()
        })

        viewModel.databaseEvent().observe(viewLifecycleOwner, databaseEventObserver())

        timerItem = args.timerItem

        setUpTimerPicker()
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
                    Toast.makeText(requireContext(), result.messageResId!!, Toast.LENGTH_SHORT).show()
                    findNavController().popBackStack()
                    dismiss()
                }
            }
        }
    }

    private fun saveTimer() {
        if (timerItem != null) {
            editExistingTimer()
            return
        }

        createNewTimer()
    }

    private fun setUpTimerPicker() {
        if (timerItem != null) {
            setUpTimerValue()
            binding.createTimer.text = getString(R.string.update)
        }

        setUpHourPicker()

        setUpMinutePicker()

        setUpSecondPicker()
    }

    private fun createNewTimer() {
        viewModel.createNewTimer(getTimerItem())
    }

    private fun editExistingTimer() {
        timerItem?.title = binding.timerTitle.text.toString().trim()

        timerItem?.updatedAt =
            LocalDateTime.now().format(DateTimeFormatter.ofPattern(DATE_PATTERN))

        timerItem?.timerValue = getTimerDuration()

        viewModel.updateTimer(timerItem!!)
    }

    private fun getTimerItem(): Timer {
        return Timer(
            binding.timerTitle.text.toString().trim(),
            getTimerDuration(),
            LocalDateTime.now().format(DateTimeFormatter.ofPattern(DATE_PATTERN))
        )
    }

    private fun setUpTimerValue() {
        val hourMinuteSecond = timerItem!!.timerValue.split(":")

        hourDuration = hourMinuteSecond[0]

        minuteDuration = hourMinuteSecond[1]

        secondDuration = hourMinuteSecond[2]

        binding.timerTitle.setText(timerItem!!.title)
    }

    private fun getPickerValues() {
        hourPickerValues = getPickerValues(MAX_HOUR_VALUE).toTypedArray()

        minutePickerValues = getPickerValues(MAX_MINUTE_VALUE).toTypedArray()

        secondPickerValues = getPickerValues(MAX_SECOND_VALUE).toTypedArray()
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

    private fun getPickerValues(maxValue: Int): MutableList<String> {
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