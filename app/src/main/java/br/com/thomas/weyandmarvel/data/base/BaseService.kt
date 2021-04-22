package br.com.thomas.weyandmarvel.data.base

import br.com.thomas.weyandmarvel.exceptions.GenericMessageException
import br.com.thomas.weyandmarvel.exceptions.NoInternetException
import br.com.thomas.weyandmarvel.exceptions.UnknownException
import retrofit2.HttpException
import retrofit2.Response
import java.net.SocketTimeoutException
import java.net.UnknownHostException

abstract class BaseService {
    suspend fun<T: Any> createCall(call: suspend () -> Response<T>) : Result<T> {

        val response: Response<T>
        try {
            response = call.invoke()
        }catch (t: Throwable){
            t.printStackTrace()
            return Result.Error(mapToNetworkError(t))
        }

        if (response.isSuccessful) {
            response.body()?.let {
                return Result.Success(it)
            }
        } else {
            Result.Error(GenericMessageException())
        }

        return Result.Error(HttpException(response))
    }

    private fun mapToNetworkError(t: Throwable): Exception {
        return when(t){
            is SocketTimeoutException
            -> SocketTimeoutException("Time out de conexão")
            is UnknownHostException
            -> NoInternetException()
            else
            -> UnknownException()

        }
    }

}