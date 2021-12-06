package com.app.timerz.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.app.timerz.data.SingleLiveEvent
import timber.log.Timber

class HomeViewModel (private val savedStateHandle: SavedStateHandle) : ViewModel() {

    private var _isValidTimerValue: MutableLiveData<Boolean> = MutableLiveData()
    private var _timerValue: String? = savedStateHandle.get<String>("value")

    fun validateTimerValue(value: String) {
        Timber.d("timer duration : $value")

        savedStateHandle["value"] = value

        if (value.isEmpty() || value == "00:00:00") {
            _isValidTimerValue.value = false
            return
        }

        _isValidTimerValue.value = true
    }

    fun isTimerValueValid(): LiveData<Boolean> {
        return _isValidTimerValue
    }

    fun getTimerValue(): String? {
        return _timerValue
    }

    fun setTimerValue(item : String) {
        savedStateHandle["value"] = item
    }
}