package com.app.timerz.ui.timerlist

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.app.timerz.data.local.database.entity.Timer
import com.app.timerz.data.local.repo.TimerRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.functions.Function
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.schedulers.Schedulers
import timber.log.Timber
import java.util.ArrayList
import java.util.concurrent.Callable
import javax.inject.Inject


@HiltViewModel
class TimerListViewModel @Inject constructor(private val timerRepo: TimerRepo) : ViewModel() {

    private val _isInputValid: MutableLiveData<Boolean> = MutableLiveData()
    private val _isItemCreated: MutableLiveData<Boolean> = MutableLiveData()
    private val _isItemEdited: MutableLiveData<Boolean> = MutableLiveData()
    private val _errorMessage: MutableLiveData<String> = MutableLiveData()
    private var compositeDisposable = CompositeDisposable()

    fun getTimersList(): LiveData<List<Timer>> {
        return timerRepo.getAllTimers()
    }

    fun validateTimerInput(timerDuration: String, title: String?) {
        if (isValidTimerTitle(title) && isValidTimerValue(timerDuration)) {
            _isInputValid.value = true
            return
        }

        _isInputValid.value = false
    }

    private fun isValidTimerValue(timerDuration: String?): Boolean {
        //todo: change to use a return when ->{} statement
        if (timerDuration == null || timerDuration.isEmpty() || timerDuration == "00:00:00") {
            return false
        }

        return true
    }

    private fun isValidTimerTitle(title: String?): Boolean {
        //todo: change to use a return when ->{} statement
        if (title == null || title.isEmpty()) {
            return false
        }

        return true
    }

    fun inputValidationResult(): LiveData<Boolean> {
        return _isInputValid
    }

    fun isItemCreated(): LiveData<Boolean> {
        return _isItemCreated
    }

    fun isItemUpdated(): LiveData<Boolean> {
        return _isItemEdited
    }

    fun errorMessage(): LiveData<String> {
        return _errorMessage
    }

    fun createNewTimer(timerItem: Timer) {
        compositeDisposable.add(
            timerRepo.createTimer(timerItem)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    { rowCount -> onItemCreatedSuccessfully(rowCount) },
                    { error -> handleError(error) }
                )
        )
    }

    fun updateTimer(timerItem: Timer) {
        compositeDisposable.add(
            Single
                .fromCallable { timerRepo.editTimer(timerItem) }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    { rowUpdated -> onItemUpdatedSuccessfully(rowUpdated) },
                    { error -> handleError(error) }
                )
        )
    }

    private fun onItemUpdatedSuccessfully(rowUpdated: Int) {
        Timber.d("timer updated with row count : $rowUpdated")

        if (rowUpdated > 0) {
            _isItemEdited.value = true
            return
        }

        _isItemEdited.value = false
    }

    private fun handleError(error: Throwable) {
        _errorMessage.value = error.localizedMessage
    }

    private fun onItemCreatedSuccessfully(rowCount: Long) {
        Timber.d("created timer row count : $rowCount")

        if (rowCount > 0) {
            _isItemCreated.value = true
            return
        }

        _isItemCreated.value = false
    }

    fun deleteTimer(id: Int) {
       /* compositeDisposable.add(
            Single.fromCallable { timerRepo.deleteTimer(id) }

        )*/
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.clear()
    }

}