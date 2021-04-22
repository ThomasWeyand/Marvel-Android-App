package br.com.thomas.weyandmarvel.search

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.viewModelScope
import androidx.paging.*
import br.com.thomas.weyandmarvel.presentation.utils.ClickCharacterType
import br.com.thomas.weyandmarvel.data.repository.IRepositoryService
import br.com.thomas.weyandmarvel.data.repository.SearchCharacterRemoteMediator
import br.com.thomas.weyandmarvel.db.CharacterDatabase
import br.com.thomas.weyandmarvel.domain.ISearchCharactersUseCase
import br.com.thomas.weyandmarvel.domain.IUpdateHeroeFavoriteUseCase
import br.com.thomas.weyandmarvel.model.CharacterClickData
import br.com.thomas.weyandmarvel.model.ResultItem
import br.com.thomas.weyandmarvel.presentation.search.SearchHeroeViewModel
import br.com.thomas.weyandmarvel.utils.TestCoroutine
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.junit.MockitoJUnitRunner
import java.util.*

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class SearchViewModelTest {

    @get:Rule
    val testInstantTaskExecutorRule: TestRule = InstantTaskExecutorRule()

    @get:Rule
    val testCoroutine = TestCoroutine()

    @Mock
    private lateinit var searchCharactersUseCase: ISearchCharactersUseCase

    @Mock
    private lateinit var updateHeroeFavoriteUseCase: IUpdateHeroeFavoriteUseCase

    @Mock
    private lateinit var repository: IRepositoryService

    @Mock
    private lateinit var database: CharacterDatabase

    private val pagingSourceFactory by lazy {
        {
            database.characterDao().charactersByName(UUID.randomUUID().toString())
        }
    }

    private val searchViewModel by lazy {
        SearchHeroeViewModel(
            searchCharactersUseCase,
            updateHeroeFavoriteUseCase
        )
    }


    @Test
    fun validate_updateItemOnDatabase() {
        testCoroutine.runBlockingTest {
            `when`(searchViewModel.characterSelected(buildCharacterDataModel())).then {
                searchViewModel.viewModelScope.launch {
                    verify(
                        updateHeroeFavoriteUseCase,
                        atLeastOnce()
                    ).updateDatabase(buildCharacterModel())
                }
            }
        }
    }

    @Test
    fun whenFetch_shouldReturn_characterPagingData_bySearchQuery() {
        testCoroutine.runBlockingTest {
            `when`(
                searchCharactersUseCase.getCharactersByName(
                    UUID.randomUUID().toString()
                )
            ).thenReturn(getCharacterpaginDataResult())
        }
    }

    private fun getCharacterpaginDataResult(): Flow<PagingData<ResultItem.Character>> {
        @OptIn(ExperimentalPagingApi::class)
        return Pager(
            config = PagingConfig(
                pageSize = 50,
                enablePlaceholders = false
            ),
            remoteMediator = SearchCharacterRemoteMediator(
                repository,
                database,
                UUID.randomUUID().toString()
            ),
            pagingSourceFactory = pagingSourceFactory
        ).flow

    }

    private fun buildCharacterModel(): ResultItem.Character {
        return ResultItem.Character(
            15642,
            "Spider-man",
            "Spider Hero",
            UUID.randomUUID().toString(),
            true
        )
    }

    private fun buildCharacterDataModel(): CharacterClickData {
        return CharacterClickData(
            character = buildCharacterModel(),
            clickType = ClickCharacterType.CHANGE_FAVORITE,
            transitionImage = null
        )
    }

}