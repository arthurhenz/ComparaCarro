package com.comparacarro.di

import com.detail.DetailViewModel
import com.home.HomeViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val presentationModule = module {
    viewModel { HomeViewModel() }
    viewModel { parameters -> DetailViewModel(cardId = parameters.get()) }

}
