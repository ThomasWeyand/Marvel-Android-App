package br.com.thomas.weyandmarvel.home

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.viewModelScope
import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import br.com.thomas.weyandmarvel.presentation.utils.ClickCharacterType
import br.com.thomas.weyandmarvel.data.repository.CharacterRemoteMediator
import br.com.thomas.weyandmarvel.data.repository.IRepositoryService
import br.com.thomas.weyandmarvel.db.CharacterDatabase
import br.com.thomas.weyandmarvel.domain.IGetCharactersUseCase
import br.com.thomas.weyandmarvel.domain.IUpdateHeroeFavoriteUseCase
import br.com.thomas.weyandmarvel.model.CharacterClickData
import br.com.thomas.weyandmarvel.model.ResultItem
import br.com.thomas.weyandmarvel.presentation.home.HomeViewModel
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
class HomeViewModelTest {

    @get:Rule
    val testInstantTaskExecutorRule: TestRule = InstantTaskExecutorRule()

    @get:Rule
    val testCoroutine = TestCoroutine()

    @Mock
    private lateinit var getCharactersUseCase: IGetCharactersUseCase

    @Mock
    private lateinit var updateHeroeFavoriteUseCase: IUpdateHeroeFavoriteUseCase

    @Mock
    private lateinit var database: CharacterDatabase

    @Mock
    private lateinit var repository: IRepositoryService

    private val homeViewModel by lazy {
        HomeViewModel(
            getCharactersUseCase,
            updateHeroeFavoriteUseCase
        )
    }

    private val pagingSourceFactory = { database.characterDao().getCharactersList() }


    @Test
    fun whenFetch_shouldReturn_characterPagingData() {
        testCoroutine.runBlockingTest {
            doReturn(getCharacterpaginDataResult())
                .`when`(getCharactersUseCase)
                .getCharacters()
            homeViewModel.getCharacters()
            verify(getCharactersUseCase).getCharacters()

        }
    }

    @Test
    fun validate_updateItemOnDatabase() {
        testCoroutine.runBlockingTest {
            `when`(homeViewModel.characterSelected(buildCharacterDataModel())).then {
                homeViewModel.viewModelScope.launch {
                    verify(
                        updateHeroeFavoriteUseCase,
                        atLeastOnce()
                    ).updateDatabase(buildCharacterModel())
                }
            }
        }
    }


    private fun getCharacterpaginDataResult(): Flow<PagingData<ResultItem.Character>> {
        @OptIn(ExperimentalPagingApi::class)
        return Pager(
            config = PagingConfig(
                pageSize = 50,
                enablePlaceholders = false
            ),
            remoteMediator = CharacterRemoteMediator(
                repository,
                database
            ),
            pagingSourceFactory = pagingSourceFactory
        ).flow

    }

    private fun buildCharacterModel() : ResultItem.Character {
        return ResultItem.Character(
            15642,
            "Spider-man",
            "Spider Hero",
            UUID.randomUUID().toString(),
            true
        )
    }

    private fun buildCharacterDataModel() : CharacterClickData {
        return CharacterClickData(
            character = buildCharacterModel(),
            clickType = ClickCharacterType.CHANGE_FAVORITE,
            transitionImage = null
        )
    }

}