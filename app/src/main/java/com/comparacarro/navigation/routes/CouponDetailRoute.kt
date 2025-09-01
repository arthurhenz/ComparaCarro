//package com.comparacarro.navigation.routes
//
//import androidx.compose.runtime.Composable
//import androidx.compose.runtime.getValue
//import androidx.hilt.navigation.compose.hiltViewModel
//import androidx.lifecycle.compose.collectAsStateWithLifecycle
//import androidx.navigation.NavGraphBuilder
//import androidx.navigation.compose.composable
//import com.comparacarro.navigation.Screen
//import com.example.poacupons.model.AvailableDays
//import com.example.poacupons.model.Coupon
//import com.example.poacupons.model.CouponCategory
//import com.example.poacupons.navigation.Screen
//import com.example.poacupons.screens.coupon_detail.CouponDetailScreen
//import com.example.poacupons.screens.coupon_detail.CouponDetailViewModel
//
//fun NavGraphBuilder.couponDetailRoute(
//    goBack: () -> Unit
//) {
//    composable(
//        route = Screen.CouponDetail.route,
//        arguments = Screen.CouponDetail.arguments
//    ) {
//        CouponDetailRoute(
//            goBack = goBack
//        )
//    }
//}
//
//@Composable
//fun CouponDetailRoute(
//    goBack: () -> Unit,
//    viewModel: CouponDetailViewModel = hiltViewModel()
//) {
//    val state by viewModel.state.collectAsStateWithLifecycle()
//
//    CouponDetailScreen(
//        onEvent = viewModel::onEvent,
//        goBack = goBack,
//        coupon = state.coupon ?: Coupon(
//            category = CouponCategory.TODOS,
//            discount = 0.15f,
//            title = "15% off at Local Restaurant",
//            isCouponUsed = false,
//            upVotes = 10,
//            availableDays = listOf(AvailableDays.SEXTA),
//            icon = "restaurant",
//            ticketId = "123"
//        )
//    )
//}
