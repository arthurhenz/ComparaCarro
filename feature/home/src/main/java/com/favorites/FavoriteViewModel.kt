package com.favorites

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.map
import com.common.navigation.NavOptions
import com.common.navigation.Navigator
import com.data.model.FavoriteCar
import com.data.usecase.GetFavoritesUseCase
import com.data.usecase.RemoveFavoriteUseCase
import com.navigation.routes.SelectComparisonRoute
import com.navigation.routes.navigateToDetail
import com.ui.BottomNavTab
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import org.koin.android.annotation.KoinViewModel

@KoinViewModel
class FavoriteViewModel(
    getFavoritesUseCase: GetFavoritesUseCase,
    private val removeFavoriteUseCase: RemoveFavoriteUseCase,
    navigator: Navigator,
) : ViewModel(), Navigator by navigator {
    val favorites: Flow<PagingData<FavoriteCarItem>> =
        getFavoritesUseCase()
            .map { page -> page.map(FavoriteCar::toItem) }
            .cachedIn(viewModelScope)

    fun onEvent(event: FavoriteScreenEvent) {
        when (event) {
            is FavoriteScreenEvent.RemoveFavorite -> removeFavorite(event.id)
        }
    }

    private fun removeFavorite(id: String) =
        viewModelScope.launch {
            try {
                removeFavoriteUseCase(id)
            } catch (e: Exception) {
                Log.e("FavoriteViewModel", "Failed to remove favorite $id: ${e.message}", e)
            }
        }

    fun openDetail(id: String) = navigateToDetail(id)

    fun navigateToCompare() {
        navigate(SelectComparisonRoute(null), NavOptions(singleTop = true))
    }

    fun navigateToTab(tab: BottomNavTab) = navigateToBottomTab(tab)
}

private fun FavoriteCar.toItem(): FavoriteCarItem =
    FavoriteCarItem(
        id = id,
        brand = brand,
        title = title,
        price = price,
        powertrain = powertrain,
        range = range,
        imageUrl = imageUrl,
    )
