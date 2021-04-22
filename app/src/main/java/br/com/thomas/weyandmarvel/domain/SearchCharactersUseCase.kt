package br.com.thomas.weyandmarvel.domain

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import br.com.thomas.weyandmarvel.data.repository.IRepositoryService
import br.com.thomas.weyandmarvel.data.repository.SearchCharacterRemoteMediator
import br.com.thomas.weyandmarvel.db.CharacterDatabase
import br.com.thomas.weyandmarvel.model.ResultItem
import kotlinx.coroutines.flow.Flow

class SearchCharactersUseCase(
    private val repositoryService: IRepositoryService,
    private val database: CharacterDatabase
) : ISearchCharactersUseCase {

    override fun getCharactersByName(query: String) : Flow<PagingData<ResultItem.Character>> {
        val dbQuery = "%${query.replace(' ', '%')}%"
        val pagingSourceFactory = { database.characterDao().charactersByName(dbQuery) }

        @OptIn(ExperimentalPagingApi::class)
        return Pager(
            config = PagingConfig(pageSize = NETWORK_PAGE_SIZE, enablePlaceholders = false),
            remoteMediator = SearchCharacterRemoteMediator(
                repositoryService,
                database,
                query
            ),
            pagingSourceFactory = pagingSourceFactory
        ).flow
    }

    companion object {
        private const val NETWORK_PAGE_SIZE = 50
    }
}