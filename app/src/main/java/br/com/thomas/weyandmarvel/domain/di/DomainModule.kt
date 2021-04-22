package br.com.thomas.weyandmarvel.domain.di

import br.com.thomas.weyandmarvel.data.repository.IRepositoryService
import br.com.thomas.weyandmarvel.db.CharacterDAO
import br.com.thomas.weyandmarvel.db.CharacterDatabase
import br.com.thomas.weyandmarvel.domain.*
import org.koin.dsl.module

val domainModule = module {
    single { characterUseCase(get(), get()) }
    single { updateFavoriteItemUseCase(get()) }
    single { searchCharacterUseCase(get(), get()) }
}

fun characterUseCase(
    repositoryService: IRepositoryService,
    database: CharacterDatabase
): IGetCharactersUseCase {
    return GetCharactersUseCase(repositoryService = repositoryService, database = database)
}

fun updateFavoriteItemUseCase(
    characterDAO: CharacterDAO
): IUpdateHeroeFavoriteUseCase {
    return UpdateHeroeFavoriteUseCase(characterDAO = characterDAO)
}

fun searchCharacterUseCase(
    repositoryService: IRepositoryService,
    database: CharacterDatabase
): ISearchCharactersUseCase {
    return SearchCharactersUseCase(repositoryService, database)
}

