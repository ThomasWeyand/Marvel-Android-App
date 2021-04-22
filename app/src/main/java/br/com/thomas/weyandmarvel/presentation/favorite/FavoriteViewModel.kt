package br.com.thomas.weyandmarvel.presentation.favorite

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import br.com.thomas.weyandmarvel.presentation.utils.ClickCharacterType
import br.com.thomas.weyandmarvel.db.CharacterDAO
import br.com.thomas.weyandmarvel.model.CharacterClickData
import br.com.thomas.weyandmarvel.model.ResultItem
import kotlinx.coroutines.flow.Flow

class FavoriteViewModel(private val characterDAO: CharacterDAO) : ViewModel() {

    private val clickMutableData = MutableLiveData<CharacterClickData>()
    val clickData : LiveData<CharacterClickData> get() = clickMutableData

    fun getFavoriteList(): Flow<PagingData<ResultItem.Character>> {
        return Pager(PagingConfig(pageSize = 50, prefetchDistance = 40)) {
            characterDAO.getFavoriteCharacterList()
        }.flow
    }

    fun characterSelected(data: CharacterClickData) {
        when(data.clickType) {
            ClickCharacterType.CHANGE_FAVORITE -> {}
            ClickCharacterType.OPEN_DETAILS -> {
                clickMutableData.value = data
            }
        }
    }

}