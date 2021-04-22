package br.com.thomas.weyandmarvel.domain

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import br.com.thomas.weyandmarvel.data.repository.CharacterRemoteMediator
import br.com.thomas.weyandmarvel.data.repository.IRepositoryService
import br.com.thomas.weyandmarvel.db.CharacterDatabase
import br.com.thomas.weyandmarvel.model.ResultItem
import kotlinx.coroutines.flow.Flow

class GetCharactersUseCase(private val repositoryService: IRepositoryService, private val database: CharacterDatabase) :
    IGetCharactersUseCase {

    override fun getCharacters() : Flow<PagingData<ResultItem.Character>> {
        val pagingSourceFactory = { database.characterDao().getCharactersList() }

        @OptIn(ExperimentalPagingApi::class)
        return Pager(
            config = PagingConfig(pageSize = NETWORK_PAGE_SIZE, enablePlaceholders = false),
            remoteMediator = CharacterRemoteMediator(
                repositoryService,
                database
            ),
            pagingSourceFactory = pagingSourceFactory
        ).flow
    }

    companion object {
        private const val NETWORK_PAGE_SIZE = 50
    }
}