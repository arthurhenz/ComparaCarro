package com.data.di

import com.data.repository.CardRepository
import com.data.repository.CardRepositoryImpl
import com.data.usecase.GetLargeCardsUseCase
import com.data.usecase.GetSmallCardsUseCase
import org.koin.core.annotation.ComponentScan
import org.koin.core.annotation.Module
import org.koin.dsl.module

//val dataModule = module {
//    single<CardRepository> { CardRepositoryImpl() }
//    factory { GetLargeCardsUseCase(get()) }
//    factory { GetSmallCardsUseCase(get()) }
//}

@Module
@ComponentScan("com.data")
class DataModule
