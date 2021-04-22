package br.com.thomas.weyandmarvel.presentation.di

import br.com.thomas.weyandmarvel.presentation.detail.CharacterDetailViewModel
import br.com.thomas.weyandmarvel.presentation.favorite.FavoriteViewModel
import br.com.thomas.weyandmarvel.presentation.home.HomeViewModel
import br.com.thomas.weyandmarvel.presentation.search.SearchHeroeViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val presentationModule = module {
    viewModel {
        HomeViewModel(
            get(),
            get()
        )
    }
    viewModel {
        FavoriteViewModel(
            get()
        )
    }
    viewModel { SearchHeroeViewModel(get(), get()) }
    viewModel {
        CharacterDetailViewModel(
            get()
        )
    }
}