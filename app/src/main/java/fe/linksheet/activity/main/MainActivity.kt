package fe.linksheet.activity.main

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.collection.valueIterator
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.rememberNavController
import fe.composekit.preference.collectAsStateWithLifecycle
import fe.linksheet.activity.util.DebugStatePublisher
import fe.linksheet.activity.util.NavGraphDebugState
import fe.linksheet.activity.util.UiEvent
import fe.linksheet.activity.UiEventReceiverBaseComponentActivity
// Analytics dialog import removed - violates Play Store policies
// Remote config dialog import removed - violates Play Store policies
import fe.linksheet.composable.ui.BoxAppHost
import fe.linksheet.extension.compose.AddIntentDeepLinkHandler
import fe.linksheet.extension.compose.ObserveDestination
import fe.linksheet.module.viewmodel.MainViewModel
import fe.linksheet.util.buildconfig.Build
import org.koin.androidx.viewmodel.ext.android.viewModel


class MainActivity : UiEventReceiverBaseComponentActivity() {
    private val viewModel by viewModel<MainViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent(edgeToEdge = true) {
            BoxAppHost {
                val snackbarHostState = remember { SnackbarHostState() }
                val navController = rememberNavController()

                val uiEvent by events.collectAsStateWithLifecycle()
                LaunchedEffect(key1 = uiEvent) {
                    uiEvent?.let {
                        when (it) {
                            is UiEvent.ShowSnackbar -> snackbarHostState.showSnackbar(message = it.text)
                            is UiEvent.NavigateTo -> navController.navigate(it.route)
                        }
                    }
                }

                AddIntentDeepLinkHandler(navController = navController)

                // Remote config dialog removed - violates Play Store policies

                if (Build.IsDebug) {
                    navController.ObserveDestination { _, destination, args ->
                        viewModel.enqueueNavEvent(destination, args)
                    }

                    // Analytics dialog removed - violates Play Store policies
                }

                MainNavHost(
                    navController = navController,
                    navigate = { navController.navigate(it) },
                    navigateNew = { navController.navigate(it) },
                    onBackPressed = { navController.popBackStack() }
                )

                if (Build.IsDebug) {
                    LaunchedEffect(key1 = Unit) {
                        @SuppressLint("RestrictedApi")
                        val graphNodes = navController.graph.nodes.valueIterator().asSequence().toList()
                        DebugStatePublisher.publishDebugState(NavGraphDebugState(graphNodes))
                    }
                }

                SnackbarHost(
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .navigationBarsPadding(),
                    hostState = snackbarHostState
                )
            }
        }
    }
}
