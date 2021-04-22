package br.com.thomas.weyandmarvel.data.repository

import br.com.thomas.weyandmarvel.data.base.Result
import br.com.thomas.weyandmarvel.data.model.ResultDataWrapperResponse
import br.com.thomas.weyandmarvel.data.model.ResultWrapper

interface IRepositoryService {
    suspend fun getCharactersList(offset: Int): Result<ResultDataWrapperResponse<ResultWrapper.CharacterResponse>>
    suspend fun searchCharactersByName(
        offset: Int,
        query: String
    ): Result<ResultDataWrapperResponse<ResultWrapper.CharacterResponse>>
}