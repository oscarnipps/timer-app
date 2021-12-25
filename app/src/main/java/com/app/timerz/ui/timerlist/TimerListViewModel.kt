package com.app.timerz.ui.timerlist

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.app.timerz.R
import com.app.timerz.data.Resource
import com.app.timerz.data.local.database.entity.Timer
import com.app.timerz.data.local.repo.TimerRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import timber.log.Timber
import javax.inject.Inject


@HiltViewModel
class TimerListViewModel @Inject constructor(private val timerRepo: TimerRepo) : ViewModel() {

    private val _isInputValid: MutableLiveData<Boolean> = MutableLiveData()
    private val _databaseEvent: MutableLiveData<Resource<Int>> = MutableLiveData()
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

    fun databaseEvent(): LiveData<Resource<Int>> {
        return _databaseEvent
    }

    fun createNewTimer(timerItem: Timer) {
        _databaseEvent.value = Resource.loading()

        compositeDisposable.add(
            timerRepo.createTimer(timerItem)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    { rowId -> onItemCreatedSuccessfully(rowId) },
                    { error -> handleError(error) }
                )
        )
    }

    fun updateTimer(timerItem: Timer) {
        _databaseEvent.value = Resource.loading()

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

    fun deleteTimer(id: Int) {
        _databaseEvent.value = Resource.loading()

        compositeDisposable.add(
            Single.fromCallable { timerRepo.deleteTimer(id) }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    { rowDeleted -> onItemDeletedSuccessfully(rowDeleted) },
                    { error -> handleError(error) }
                )
        )
    }

    fun deleteAllTimers() {
        _databaseEvent.value = Resource.loading()

        compositeDisposable.add(
            Single.fromCallable { timerRepo.deleteAllTimers() }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    { rowsDeleted -> onItemsDeletedSuccessfully(rowsDeleted) },
                    { error -> handleError(error) }
                )
        )
    }

    private fun handleError(error: Throwable) {
        Timber.e(error)
        _databaseEvent.value = Resource.error(R.string.error_message)
    }

    private fun onItemUpdatedSuccessfully(rowUpdated: Int) {
        Timber.d("timer updated with row count : $rowUpdated")

        if (rowUpdated > 0) {
            _databaseEvent.value = Resource.success(rowUpdated, R.string.timer_update_success)
            return
        }

        _databaseEvent.value = Resource.success(rowUpdated, R.string.timer_update_failure)
    }

    private fun onItemCreatedSuccessfully(rowId: Long) {
        Timber.d("created timer with row id : $rowId")

        if (rowId > 0) {
            _databaseEvent.value = Resource.success(rowId.toInt(), R.string.timer_creation_success)
            return
        }

        _databaseEvent.value =
            Resource.error(R.string.timer_creation_failure)
    }

    private fun onItemDeletedSuccessfully(rowDeleted: Int) {
        Timber.d("rows deleted : $rowDeleted")

        if (rowDeleted > 0) {
            _databaseEvent.value = Resource.success(rowDeleted, R.string.timer_deletion_success)
            return
        }

        _databaseEvent.value =
            Resource.error(R.string.timer_deletion_failure)
    }

    private fun onItemsDeletedSuccessfully(rowsDeleted: Int) {
        Timber.d("rows deleted : $rowsDeleted")

        if (rowsDeleted > 0) {
            _databaseEvent.value = Resource.success(rowsDeleted, R.string.timers_deletion_success)
            return
        }

        _databaseEvent.value =
            Resource.error(R.string.timers_deletion_failure)
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.clear()
    }

}