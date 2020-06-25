package ge.test.myapplication.network.models

import android.icu.lang.UCharacter.GraphemeClusterBreak.T


class Resource<T>(
    val status: Status? = null,
    val data: T? = null,
    val message: String? = null
) {

    companion object {

        fun <T> success(data: T?): Resource<T> {
            return Resource(Status.SUCCESS, data, null)
        }

        fun <T> error(msg: String?, data: T?): Resource<T> {
            return Resource(Status.ERROR, data, msg)
        }

        fun <T> loading(data: T): Resource<T> {
            return Resource(Status.LOADING, data, null)
        }

    }

    enum class Status {
        SUCCESS, ERROR, LOADING
    }
}