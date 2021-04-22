package br.com.thomas.weyandmarvel.domain

import br.com.thomas.weyandmarvel.model.ResultItem

interface IUpdateHeroeFavoriteUseCase {
    suspend fun updateDatabase(character: ResultItem.Character)

}