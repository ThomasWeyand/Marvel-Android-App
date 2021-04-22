package br.com.thomas.weyandmarvel.presentation.detail

import androidx.lifecycle.ViewModel
import br.com.thomas.weyandmarvel.domain.IUpdateHeroeFavoriteUseCase
import br.com.thomas.weyandmarvel.model.CharacterDetailVO
import br.com.thomas.weyandmarvel.presentation.mapper.mapFromDetail

class CharacterDetailViewModel(private val updateHeroeFavoriteUseCase: IUpdateHeroeFavoriteUseCase) : ViewModel() {


    suspend fun updateFavoriteItemDatabase(character: CharacterDetailVO) {
        updateHeroeFavoriteUseCase.updateDatabase(character.mapFromDetail())
    }

}