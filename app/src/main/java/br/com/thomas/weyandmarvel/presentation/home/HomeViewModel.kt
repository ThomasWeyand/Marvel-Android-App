package br.com.thomas.weyandmarvel.presentation.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import br.com.thomas.weyandmarvel.presentation.utils.ClickCharacterType
import br.com.thomas.weyandmarvel.domain.IGetCharactersUseCase
import br.com.thomas.weyandmarvel.domain.IUpdateHeroeFavoriteUseCase
import br.com.thomas.weyandmarvel.model.CharacterClickData
import br.com.thomas.weyandmarvel.model.ResultItem
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class HomeViewModel(
    private val getCharactersUseCase: IGetCharactersUseCase,
    private val updateHeroeFavoriteUseCase: IUpdateHeroeFavoriteUseCase
) : ViewModel() {

    private val clickMutableData = MutableLiveData<CharacterClickData>()
    val clickData : LiveData<CharacterClickData> get() = clickMutableData

    fun getCharacters(): Flow<PagingData<ResultItem.Character>> {
        val newResult: Flow<PagingData<ResultItem.Character>> = getCharactersUseCase.getCharacters()
            .cachedIn(viewModelScope)

        return newResult
    }

    fun characterSelected(data: CharacterClickData) {
        when(data.clickType) {
            ClickCharacterType.CHANGE_FAVORITE -> {
                viewModelScope.launch {
                    updateFavoriteItemDatabase(data.character)
                }
            }
            ClickCharacterType.OPEN_DETAILS -> {
                clickMutableData.value = data
            }
        }
    }

    private suspend fun updateFavoriteItemDatabase(character: ResultItem.Character) {
        updateHeroeFavoriteUseCase.updateDatabase(character)
    }

}