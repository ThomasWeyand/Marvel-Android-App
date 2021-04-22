package br.com.thomas.weyandmarvel.detail

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.viewModelScope
import br.com.thomas.weyandmarvel.domain.IUpdateHeroeFavoriteUseCase
import br.com.thomas.weyandmarvel.model.CharacterDetailVO
import br.com.thomas.weyandmarvel.presentation.detail.CharacterDetailViewModel
import br.com.thomas.weyandmarvel.presentation.mapper.mapFromDetail
import br.com.thomas.weyandmarvel.utils.TestCoroutine
import kotlinx.coroutines.ExperimentalCoroutinesApi
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
class CharacterDetailViewModelTest {

    @get:Rule
    val testInstantTaskExecutorRule: TestRule = InstantTaskExecutorRule()

    @get:Rule
    val testCoroutine = TestCoroutine()

    @Mock
    private lateinit var updateHeroeFavoriteUseCase: IUpdateHeroeFavoriteUseCase

    private val detailViewModel by lazy {
        CharacterDetailViewModel(
            updateHeroeFavoriteUseCase
        )
    }

    @Test
    fun validate_whenUpdateFavoriteDatabase() {
        testCoroutine.runBlockingTest {
            `when`(detailViewModel.updateFavoriteItemDatabase(mockCharacterDetailVO()))
                .then {
                    detailViewModel.viewModelScope.launch {
                        verify(updateHeroeFavoriteUseCase, atLeastOnce())
                            .updateDatabase(mockCharacterDetailVO().mapFromDetail())
                    }
                }
        }
    }

    private fun mockCharacterDetailVO(): CharacterDetailVO {
        return CharacterDetailVO(
            id = 415645,
            thumbnailPath = UUID.randomUUID().toString(),
            description = UUID.randomUUID().toString(),
            name = UUID.randomUUID().toString(),
            isFavorite = true
        )
    }

}