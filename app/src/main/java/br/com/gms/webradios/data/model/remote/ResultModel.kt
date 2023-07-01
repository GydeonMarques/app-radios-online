package br.com.gms.webradios.data.model.remote

sealed class ResultModel<out T> {

    data class Success<out T>(val data: T) : ResultModel<T>()
    data class Failure(val throwable: Throwable) : ResultModel<Nothing>()

    override fun toString(): String {
        return when (this) {
            is Success<*> -> "Success [data = $data]"
            is Failure -> "Failure [throwable= $throwable]"
        }
    }
}