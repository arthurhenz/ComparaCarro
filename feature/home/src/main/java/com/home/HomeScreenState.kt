//package com.home
//
//sealed class HomeScreenState {
//    data object Loading : HomeScreenState()
//    data class Error(val error: String?) : HomeScreenState()
//    data class AllCoupons(val coupons: List<Coupon>) : HomeScreenState()
//    data class FilteredCoupons(val coupons: List<Coupon>, val category: CouponCategory) : HomeScreenState()
//}
