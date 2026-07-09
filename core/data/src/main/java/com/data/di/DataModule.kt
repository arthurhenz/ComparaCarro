package com.data.di

import android.content.Context
import androidx.room.Room
import com.data.local.ComparaCarroDatabase
import com.data.local.FavoriteCarDao
import com.google.firebase.auth.FirebaseAuth
import org.koin.core.annotation.ComponentScan
import org.koin.core.annotation.Module
import org.koin.core.annotation.Single

@Module
@ComponentScan("com.data")
class DataModule {
    @Single
    fun provideFirebaseAuth(): FirebaseAuth = FirebaseAuth.getInstance()

    @Single
    fun provideDatabase(context: Context): ComparaCarroDatabase =
        Room.databaseBuilder(
            context,
            ComparaCarroDatabase::class.java,
            ComparaCarroDatabase.NAME,
        ).build()

    @Single
    fun provideFavoriteCarDao(database: ComparaCarroDatabase): FavoriteCarDao =
        database.favoriteCarDao()
}
