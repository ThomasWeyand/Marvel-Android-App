package br.com.thomas.weyandmarvel.domain

import androidx.paging.PagingData
import br.com.thomas.weyandmarvel.model.ResultItem
import kotlinx.coroutines.flow.Flow

interface ISearchCharactersUseCase {
    fun getCharactersByName(query: String) : Flow<PagingData<ResultItem.Character>>
}