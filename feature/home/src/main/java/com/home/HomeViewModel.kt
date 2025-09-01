//package com.home
//
//import androidx.lifecycle.ViewModel
//import androidx.lifecycle.viewModelScope
//import kotlinx.coroutines.flow.MutableStateFlow
//import kotlinx.coroutines.flow.StateFlow
//import kotlinx.coroutines.flow.asStateFlow
//import kotlinx.coroutines.launch
//import kotlin.collections.filter
//
//class HomeScreenViewModel : ViewModel(), KoinComponent {
//    private val repository: CarsRepository by inject()
//
//    private val _state = MutableStateFlow<HomeScreenState>(HomeScreenState.Loading)
//    val state: StateFlow<HomeScreenState> = _state.asStateFlow()
//
//    private var allCars: List<Car> = emptyList()
//
//    init {
//        loadCoupons()
//    }
//
//    private fun loadCoupons() = viewModelScope.launch {
//        try {
//            allCars = repository.getAllCars()
//            _state.value = HomeScreenState.AllCoupons(allCars)
//        } catch (e: Exception) {
//            _state.value = HomeScreenState.Error(e.message ?: "Failed to load coupons")
//        }
//    }
//
//    fun onEvent(event: HomeScreenEvent) {
//        when (event) {
//            is HomeScreenEvent.FilterByCategory -> {
//                filterByCategory(event.category)
//            }
//            HomeScreenEvent.ReloadCoupons -> {
//                loadCoupons()
//            }
//        }
//    }
//
//    private fun filterByCategory(category: CouponCategory) {
//        if (category == CouponCategory.TODOS) {
//            _state.value = HomeScreenState.AllCoupons(allCars)
//        } else {
//            val filteredCoupons = allCars.filter { it.category == category }
//            _state.value = HomeScreenState.FilteredCoupons(filteredCoupons, category)
//        }
//    }
//}
