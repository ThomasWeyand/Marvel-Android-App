package br.com.thomas.weyandmarvel.favorite

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import br.com.thomas.weyandmarvel.db.CharacterDAO
import br.com.thomas.weyandmarvel.utils.TestCoroutine
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.junit.MockitoJUnitRunner


@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class FavoriteViewModelTest {

    @Mock
    @get:Rule
    val testInstantTaskExecutorRule: TestRule = InstantTaskExecutorRule()

    @get:Rule
    val testCoroutine = TestCoroutine()

    @Mock
    private lateinit var characterDao: CharacterDAO

    @Test
    fun check_whenFetch_favoriteList() {
        testCoroutine.runBlockingTest {
            `when`(characterDao.getFavoriteCharacterList()).then{
                verify(characterDao, atLeastOnce()).getFavoriteCharacterList()
            }
        }
    }

}