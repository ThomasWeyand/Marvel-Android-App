package br.com.thomas.weyandmarvel.data.repository.keyAuthentication

import br.com.thomas.weyandmarvel.BuildConfig
import okhttp3.Interceptor
import okhttp3.Response

class KeyAuthenticator : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val ts = System.currentTimeMillis().toString()
        val hash = HashHelper.generateHash(ts + BuildConfig.PRIVATE_KEY + BuildConfig.PUBLIC_KEY)

        val currentRequest = chain.request()
        val url = currentRequest.url().newBuilder()
            .addQueryParameter(BuildConfig.QUERY_NAME_TIMESTAMP, ts)
            .addQueryParameter(BuildConfig.QUERY_NAME_APIKEY, BuildConfig.PUBLIC_KEY)
            .addQueryParameter(BuildConfig.QUERY_NAME_HASH, hash)
            .build()

        val newRequest = currentRequest.newBuilder().url(url).build()

        return chain.proceed(newRequest)
    }
}