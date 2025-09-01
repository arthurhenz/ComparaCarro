//package com.comparacarro.navigation
//
//import androidx.compose.runtime.Composable
//import androidx.navigation.NavHostController
//import androidx.navigation.NavType
//import androidx.navigation.compose.NavHost
//import androidx.navigation.compose.rememberNavController
//import androidx.navigation.navArgument
//import androidx.navigation.NamedNavArgument
//import com.comparacarro.navigation.routes.couponDetailRoute
//import com.comparacarro.navigation.routes.homeScreenRoute
//
//@Composable
//fun AppNavigation(
//    navController: NavHostController = rememberNavController(),
//    startDestination: String = Screen.Home.route
//) {
//    NavHost(
//        navController = navController,
//        startDestination = startDestination
//    ) {
//        homeScreenRoute(
//            onCouponClick = { couponId ->
//                navController.navigate(route = Screen.CouponDetail.createRoute(couponId.toString()))
//            }
//        )
//
//        couponDetailRoute(goBack = { navController.navigateUp() })
//    }
//}
//
//sealed class Screen(
//    val route: String,
//    val arguments: List<NamedNavArgument> = emptyList()
//) {
//    object Home : Screen("home")
//
//    object CouponDetail : Screen(
//        route = "coupon/{couponId}",
//        arguments = listOf(
//            navArgument("couponId") {
//                type = NavType.StringType
//            }
//        )
//    ) {
//        fun createRoute(couponId: String) = "coupon/$couponId"
//    }
//}
