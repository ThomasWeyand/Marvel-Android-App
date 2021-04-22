package br.com.thomas.weyandmarvel.data.repository

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import br.com.thomas.weyandmarvel.data.base.Result
import br.com.thomas.weyandmarvel.data.model.ResultWrapper
import br.com.thomas.weyandmarvel.db.CharacterDatabase
import br.com.thomas.weyandmarvel.db.SearchCharacterOffset
import br.com.thomas.weyandmarvel.domain.mapFromResponse
import br.com.thomas.weyandmarvel.model.ResultItem

@OptIn(ExperimentalPagingApi::class)
class SearchCharacterRemoteMediator(
    private val repositoryService: IRepositoryService,
    private val database: CharacterDatabase,
    private val query: String
) : RemoteMediator<Int, ResultItem.Character>() {

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, ResultItem.Character>
    ): MediatorResult {
        val pageOffet = when (loadType) {
            LoadType.REFRESH -> {
                val offsetValue = getOffsetValueClosestToCurrentPosition(state)
                val value = offsetValue?.nextOffset?.minus(OFFSET_PAGE_VALUE)
                if (value != null && value >= 0)
                    value
                else
                    START_OFFSET
            }
            LoadType.PREPEND -> {
                val offsetValue = getOffsetValueForFirstItem(state)
                val prevOffset = offsetValue?.prevOffset
                if (prevOffset == null) {
                    return MediatorResult.Success(endOfPaginationReached = offsetValue != null)
                }
                prevOffset
            }
            LoadType.APPEND -> {
                val offsetValue = getOffsetValueForLastItem(state)
                val nextOffset = offsetValue?.nextOffset
                if (nextOffset == null) {
                    return MediatorResult.Success(endOfPaginationReached = offsetValue != null)
                }
                nextOffset
            }
        }
        val apiResult = repositoryService.searchCharactersByName(pageOffet, query)

        when (apiResult) {
            is Result.Success -> {
                val data = apiResult.data.data
                val endOfPaginationReached = data.results.isEmpty()

                database.withTransaction {

                    if (loadType == LoadType.REFRESH) {
                        database.searchOffsetDao().clearRemoteOffset()
                    }

                    val prevOffset =
                        if (data.offset == START_OFFSET) null else pageOffet - OFFSET_PAGE_VALUE
                    val nextOffset =
                        if (endOfPaginationReached) null else pageOffet + OFFSET_PAGE_VALUE
                    val characterList = getCharactersListAfterConsult(items = data.results)
                    val offsetValues = characterList.map {
                        SearchCharacterOffset(
                            id = it.id,
                            prevOffset = prevOffset,
                            nextOffset = nextOffset
                        )
                    }
                    database.searchOffsetDao().insertAll(offsetValues)
                    database.characterDao().insertAll(characterList)
                }

                return MediatorResult.Success(endOfPaginationReached = endOfPaginationReached)
            }
            is Result.Error -> {
                return MediatorResult.Error(apiResult.error)
            }
        }
    }

    private suspend fun getCharactersListAfterConsult(items: List<ResultWrapper.CharacterResponse>): List<ResultItem.Character> {
        return items.map {
            it.mapFromResponse(
                database.characterDao().getItemById(itemId = it.id)?.isFavorite ?: false
            )
        }
    }

    private suspend fun getOffsetValueForLastItem(state: PagingState<Int, ResultItem.Character>): SearchCharacterOffset? {
        return state.pages.lastOrNull { it.data.isNotEmpty() }?.data?.lastOrNull()
            ?.let { character ->
                database.searchOffsetDao().getOffsetById(character.id)
            }
    }

    private suspend fun getOffsetValueForFirstItem(state: PagingState<Int, ResultItem.Character>): SearchCharacterOffset? {
        return state.pages.firstOrNull { it.data.isNotEmpty() }?.data?.firstOrNull()
            ?.let { character ->
                database.searchOffsetDao().getOffsetById(character.id)
            }
    }

    private suspend fun getOffsetValueClosestToCurrentPosition(
        state: PagingState<Int, ResultItem.Character>
    ): SearchCharacterOffset? {
        return state.anchorPosition?.let { position ->
            state.closestItemToPosition(position)?.id?.let { characterId ->
                database.searchOffsetDao().getOffsetById(characterId)
            }
        }
    }

    companion object {
        private const val START_OFFSET = 0
        private const val OFFSET_PAGE_VALUE = 50
    }
}