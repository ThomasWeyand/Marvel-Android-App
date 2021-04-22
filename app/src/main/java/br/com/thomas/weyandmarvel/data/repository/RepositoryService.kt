package br.com.thomas.weyandmarvel.data.repository

import br.com.thomas.weyandmarvel.data.MarvelApi
import br.com.thomas.weyandmarvel.data.base.BaseService
import br.com.thomas.weyandmarvel.data.base.Result
import br.com.thomas.weyandmarvel.data.model.DataResponse
import br.com.thomas.weyandmarvel.data.model.ResultDataWrapperResponse
import br.com.thomas.weyandmarvel.data.model.ResultWrapper
import br.com.thomas.weyandmarvel.domain.mapFromResponse
import br.com.thomas.weyandmarvel.model.DataResult
import br.com.thomas.weyandmarvel.model.ResultItem

class RepositoryService(private val api: MarvelApi) : IRepositoryService, BaseService() {

    override suspend fun getCharactersList(offset: Int): Result<ResultDataWrapperResponse<ResultWrapper.CharacterResponse>> {
        return createCall { api.fetchCharacters(offset = offset) }
    }

    override suspend fun searchCharactersByName(
        offset: Int,
        query: String
    ): Result<ResultDataWrapperResponse<ResultWrapper.CharacterResponse>> {

        return createCall { api.searchCharactersByName(nameQuery = query, offset = offset) }

    }
}