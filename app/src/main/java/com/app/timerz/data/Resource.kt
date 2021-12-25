package com.app.timerz.data

class Resource<T>(val data: T?, val status: Status, val messageResId: Int?) {

    companion object {

        fun <T> loading(): Resource<T> {
            return Resource(null, Status.LOADING, null)
        }

        fun <T> error(messageResId: Int): Resource<T> {
            return Resource(null, Status.ERROR, messageResId)
        }

        fun <T> success(data: T, messageResId: Int): Resource<T> {
            return Resource(data, Status.SUCCESS, messageResId)
        }

    }

    enum class Status { SUCCESS, ERROR, LOADING }
}