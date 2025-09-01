package fe.linksheet.composable.page.history

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import java.net.URLEncoder
import java.net.URLDecoder
import java.nio.charset.StandardCharsets

/**
 * Navigation setup for History-related screens
 */
object HistoryNavigation {
    const val HISTORY_ROUTE = "history"
    const val LINK_DETAIL_ROUTE = "link_detail/{linkUrl}"
    const val QR_CODE_ROUTE = "qr_code/{linkUrl}"
    
    fun createLinkDetailRoute(linkUrl: String): String {
        val encodedUrl = URLEncoder.encode(linkUrl, StandardCharsets.UTF_8.toString())
        return "link_detail/$encodedUrl"
    }
    
    fun createQRCodeRoute(linkUrl: String): String {
        val encodedUrl = URLEncoder.encode(linkUrl, StandardCharsets.UTF_8.toString())
        return "qr_code/$encodedUrl"
    }
    
    fun decodeLinkUrl(encodedUrl: String): String {
        return URLDecoder.decode(encodedUrl, StandardCharsets.UTF_8.toString())
    }
}

/**
 * Complete navigation setup for History screens
 */
@Composable
fun HistoryNavigationHost(
    navController: NavHostController = rememberNavController(),
    onBackToMain: () -> Unit
) {
    NavHost(
        navController = navController,
        startDestination = HistoryNavigation.HISTORY_ROUTE
    ) {
        // History List Screen
        composable(HistoryNavigation.HISTORY_ROUTE) {
            HistoryRoute(
                onBackPressed = onBackToMain,
                onNavigateToLinkDetail = { linkUrl ->
                    val route = HistoryNavigation.createLinkDetailRoute(linkUrl)
                    navController.navigate(route)
                }
            )
        }
        
        // Link Detail Screen
        composable(HistoryNavigation.LINK_DETAIL_ROUTE) { backStackEntry ->
            val encodedUrl = backStackEntry.arguments?.getString("linkUrl") ?: ""
            val linkUrl = HistoryNavigation.decodeLinkUrl(encodedUrl)
            
            LinkDetailScreen(
                linkUrl = linkUrl,
                onBackPressed = {
                    navController.popBackStack()
                },
                onNavigateToQRCode = { url ->
                    val route = HistoryNavigation.createQRCodeRoute(url)
                    navController.navigate(route)
                }
            )
        }
        
        // QR Code Screen
        composable(HistoryNavigation.QR_CODE_ROUTE) { backStackEntry ->
            val encodedUrl = backStackEntry.arguments?.getString("linkUrl") ?: ""
            val linkUrl = HistoryNavigation.decodeLinkUrl(encodedUrl)
            
            QRCodeScreen(
                linkUrl = linkUrl,
                onBackPressed = {
                    navController.popBackStack()
                }
            )
        }
    }
}

/**
 * Example usage in main navigation
 */
@Composable
fun ExampleMainNavigation() {
    val mainNavController = rememberNavController()
    
    NavHost(
        navController = mainNavController,
        startDestination = "main"
    ) {
        composable("main") {
            // Your main screen content
            // When user wants to go to history:
            // mainNavController.navigate("history_flow")
        }
        
        composable("history_flow") {
            HistoryNavigationHost(
                onBackToMain = {
                    mainNavController.popBackStack()
                }
            )
        }
    }
}