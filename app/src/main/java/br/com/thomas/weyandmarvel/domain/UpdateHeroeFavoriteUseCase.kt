package br.com.thomas.weyandmarvel.domain

import br.com.thomas.weyandmarvel.db.CharacterDAO
import br.com.thomas.weyandmarvel.model.ResultItem

class UpdateHeroeFavoriteUseCase(private val characterDAO: CharacterDAO) :
    IUpdateHeroeFavoriteUseCase {

    override suspend fun updateDatabase(character: ResultItem.Character) {
        characterDAO.updateFavoriteValue(character.isFavorite, character.id)
    }

}