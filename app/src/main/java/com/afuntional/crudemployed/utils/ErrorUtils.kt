package com.afuntional.crudemployed.utils

import com.squareup.moshi.Moshi
import retrofit2.HttpException
import retrofit2.Response

data class ErrorResponse(val message: String)

fun parseErrorMessage(throwable: Throwable): String {
    return when (throwable) {
        is HttpException -> {
            val errorBody = throwable.response()?.errorBody()?.string()
            parseMessageFromBody(errorBody)
        }
        else -> throwable.message ?: "Error desconocido"
    }
}

fun parseErrorMessage(response: Response<*>): String {
    val errorBody = response.errorBody()?.string()
    return parseMessageFromBody(errorBody)
}

private fun parseMessageFromBody(jsonBody: String?): String {
    return try {
        val moshi = Moshi.Builder().build()
        val adapter = moshi.adapter(ErrorResponse::class.java)
        val errorResponse = adapter.fromJson(jsonBody ?: "")
        errorResponse?.message ?: "Error desconocido del servidor"
    } catch (e: Exception) {
        "Error al leer mensaje del servidor"
    }
}