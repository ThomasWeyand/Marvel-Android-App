package br.com.thomas.weyandmarvel.data

import br.com.thomas.weyandmarvel.data.model.ResultDataWrapperResponse
import br.com.thomas.weyandmarvel.data.model.ResultWrapper
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface MarvelApi {

    @GET("characters")
    suspend fun fetchCharacters(
        @Query("limit") limit: Int = 50,
        @Query("offset") offset: Int
    ): Response<ResultDataWrapperResponse<ResultWrapper.CharacterResponse>>

    @GET("characters")
    suspend fun searchCharactersByName(
        @Query("nameStartsWith") nameQuery: String,
        @Query("limit") limit: Int = 50,
        @Query("offset") offset: Int
    ) : Response<ResultDataWrapperResponse<ResultWrapper.CharacterResponse>>

}