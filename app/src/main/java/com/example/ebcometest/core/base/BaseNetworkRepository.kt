package com.example.ebcometest.core.base


import com.example.ebcometest.R
import com.example.ebcometest.core.model.NetworkResult
import com.example.ebcometest.model.dto.ResultDto
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.onCompletion
import okhttp3.ResponseBody
import org.json.JSONObject
import retrofit2.HttpException
import java.io.EOFException
import java.io.IOException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import javax.net.ssl.SSLHandshakeException

open class BaseNetworkRepository {

    protected fun <T> handleResult(apiCall: suspend () -> ResultDto<T>) =
        flow<NetworkResult<T>> {
            emit(NetworkResult.Loading())
            emit(NetworkResult.Success(apiCall.invoke().messages!!))
        }.catch {

            emit(handleErrors(exception = it))
        }.onCompletion {
            emit(NetworkResult.Loaded())
        }


    private fun <T> handleErrors(exception: Throwable): NetworkResult<T> {
        return when (exception) {
            is EOFException -> NetworkResult.InternalError(R.string.eofException)
            is SSLHandshakeException ->NetworkResult. InternalError(R.string.sslException)
            is SocketTimeoutException -> NetworkResult.InternalError(R.string.internetConnection)
            is UnknownHostException -> NetworkResult.InternalError(R.string.undefineException)
            is IOException ->NetworkResult.InternalError(R.string.internetConnection)
            is HttpException -> handleHttpErrors(exception)
            else -> {
                NetworkResult. InternalError(R.string.undefineException)
            }
        }
    }

    private fun <T> handleHttpErrors(exception: HttpException) : NetworkResult<T>{
        return when(exception.code()){
            500 -> NetworkResult.InternalError(R.string.serverConnection)
            401 -> NetworkResult.InternalError(R.string.forbidden)
            else ->{
                NetworkResult.NetworkError(getErrorMessage(exception.response()?.errorBody()))
            }
        }
    }

    private fun getErrorMessage(errorBody: ResponseBody?): String {
        return try {
            val jsonObject = JSONObject(errorBody?.string() ?: "")
            when {
                jsonObject.has("message") -> {
                    jsonObject.getString("message")
                }
                else -> {
                    ""
                }
            }
        } catch (e: Exception) {
            ""
        }
    }
}