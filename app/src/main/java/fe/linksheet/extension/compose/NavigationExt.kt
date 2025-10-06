package fe.linksheet.extension.compose

import android.os.Bundle
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController

@Composable
fun AddIntentDeepLinkHandler(navController: NavHostController) {
    // Minimal no-op for debug build
}

@Composable
fun NavHostController.ObserveDestination(block: (NavHostController, Any?, Any?) -> Unit) {
    // Minimal no-op observer for debug build
}
