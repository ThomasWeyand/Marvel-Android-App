package br.com.thomas.weyandmarvel.data.di

import androidx.annotation.NonNull
import br.com.thomas.weyandmarvel.BuildConfig
import br.com.thomas.weyandmarvel.data.MarvelApi
import br.com.thomas.weyandmarvel.data.repository.IRepositoryService
import br.com.thomas.weyandmarvel.data.repository.RepositoryService
import br.com.thomas.weyandmarvel.data.repository.keyAuthentication.KeyAuthenticator
import okhttp3.OkHttpClient
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

val networkModule = module {
    single { repositoryService(get()) }
    single { marvelApi(get()) }
    single { provideRetrofit(get()) }
    single { provideOkhttpClient() }
}


fun repositoryService(api: MarvelApi): IRepositoryService {
    return RepositoryService(api = api)
}


fun marvelApi(@NonNull retrofit: Retrofit): MarvelApi {
    return retrofit.create(MarvelApi::class.java)
}


fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit {

    return Retrofit.Builder()
        .baseUrl(BuildConfig.BASE_URL)
        .client(okHttpClient)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
}


fun provideOkhttpClient(
): OkHttpClient =
    OkHttpClient.Builder()
        .addInterceptor(KeyAuthenticator())
        .build()
