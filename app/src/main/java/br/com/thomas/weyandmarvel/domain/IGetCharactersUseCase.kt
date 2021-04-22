package br.com.thomas.weyandmarvel.domain

import androidx.paging.PagingData
import br.com.thomas.weyandmarvel.model.DataResult
import br.com.thomas.weyandmarvel.model.ResultItem
import kotlinx.coroutines.flow.Flow

interface IGetCharactersUseCase {
   fun getCharacters(): Flow<PagingData<ResultItem.Character>>
}