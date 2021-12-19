package com.app.timerz.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor () : ViewModel() {

    private var _isValidTimerValue: MutableLiveData<Boolean> = MutableLiveData()
    private var _previouslySetTimerValue = ""

    fun validateTimerValue(value: String) {
        Timber.d("timer duration : $value")

        _previouslySetTimerValue = value

        if (value.isEmpty() || value == "00:00:00") {
            _isValidTimerValue.value = false
            return
        }

        _isValidTimerValue.value = true
    }

    fun isTimerValueValid(): LiveData<Boolean> {
        return _isValidTimerValue
    }

    fun getPreviouslySetTimerValue(): String {
        return _previouslySetTimerValue
    }
}