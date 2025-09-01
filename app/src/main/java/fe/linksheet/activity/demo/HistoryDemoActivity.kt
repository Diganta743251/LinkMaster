package fe.linksheet.activity.demo

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import fe.linksheet.composable.page.history.*
import fe.linksheet.composable.ui.theme.LinkSheetTheme
import fe.linksheet.module.database.entity.AppSelectionHistory
import fe.linksheet.module.viewmodel.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

/**
 * Demo Activity for testing the History feature independently
 * This can be used to test the UI without the full app build
 */
class HistoryDemoActivity : ComponentActivity() {
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        
        setContent {
            LinkSheetTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    HistoryDemoApp()
                }
            }
        }
    }
}

@Composable
private fun HistoryDemoApp() {
    var currentScreen by remember { mutableStateOf("history") }
    var selectedUrl by remember { mutableStateOf("") }
    
    when (currentScreen) {
        "history" -> {
            HistoryRoute(
                onBackPressed = { /* Demo - no back action */ },
                onNavigateToDetail = { url ->
                    selectedUrl = url
                    currentScreen = "detail"
                },
                onNavigateToQRCode = { url ->
                    selectedUrl = url
                    currentScreen = "qr"
                },
                viewModel = createDemoHistoryViewModel()
            )
        }
        
        "detail" -> {
            LinkDetailScreen(
                onBackPressed = { currentScreen = "history" },
                onNavigateToQRCode = { currentScreen = "qr" },
                viewModel = createDemoLinkDetailViewModel(selectedUrl)
            )
        }
        
        "qr" -> {
            QRCodeScreen(
                onBackPressed = { 
                    currentScreen = if (selectedUrl.isNotEmpty()) "detail" else "history"
                },
                viewModel = createDemoQRCodeViewModel(selectedUrl)
            )
        }
    }
}

@Composable
private fun createDemoHistoryViewModel(): HistoryViewModel {
    val context = LocalContext.current
    
    // Create demo data
    val demoItems = listOf(
        createDemoHistoryItem(
            id = 1,
            url = "https://github.com/LinkSheet/LinkSheet",
            domain = "github.com",
            title = "LinkSheet Repository",
            accessCount = 15,
            hoursAgo = 1,
            isPinned = true
        ),
        createDemoHistoryItem(
            id = 2,
            url = "https://www.youtube.com/watch?v=dQw4w9WgXcQ",
            domain = "youtube.com",
            title = "Never Gonna Give You Up",
            accessCount = 8,
            hoursAgo = 3,
            isPinned = false
        ),
        createDemoHistoryItem(
            id = 3,
            url = "https://stackoverflow.com/questions/android-compose",
            domain = "stackoverflow.com",
            title = "Android Compose Best Practices",
            accessCount = 5,
            hoursAgo = 6,
            isPinned = false
        ),
        createDemoHistoryItem(
            id = 4,
            url = "https://developer.android.com/jetpack/compose",
            domain = "developer.android.com",
            title = "Jetpack Compose Documentation",
            accessCount = 12,
            hoursAgo = 12,
            isPinned = true
        ),
        createDemoHistoryItem(
            id = 5,
            url = "https://medium.com/@author/kotlin-coroutines-guide",
            domain = "medium.com",
            title = "Complete Guide to Kotlin Coroutines",
            accessCount = 3,
            hoursAgo = 24,
            isPinned = false
        ),
        createDemoHistoryItem(
            id = 6,
            url = "https://www.reddit.com/r/androiddev/comments/example",
            domain = "reddit.com",
            title = "Android Development Discussion",
            accessCount = 2,
            hoursAgo = 48,
            isPinned = false
        )
    )
    
    return object : HistoryViewModel(context, null!!) {
        override val historyState = MutableStateFlow(
            HistoryState.Success(
                items = demoItems,
                totalCount = demoItems.size,
                hasMore = false
            )
        )
        
        override val searchQuery = MutableStateFlow("")
        override val sortOption = MutableStateFlow(SortOption.RECENT)
        override val viewMode = MutableStateFlow(ViewMode.LIST)
        override val isSelectionMode = MutableStateFlow(false)
        override val selectedItems = MutableStateFlow(emptySet<Int>())
        override val showDeleteDialog = MutableStateFlow(false)
        
        override fun search(query: String) {
            _searchQuery.value = query
            // In demo, filter items by query
            val filteredItems = if (query.isBlank()) {
                demoItems
            } else {
                demoItems.filter { 
                    it.title.contains(query, ignoreCase = true) ||
                    it.domain.contains(query, ignoreCase = true) ||
                    it.url.contains(query, ignoreCase = true)
                }
            }
            _historyState.value = HistoryState.Success(
                items = filteredItems,
                totalCount = filteredItems.size
            )
        }
        
        override fun sort(option: SortOption) {
            _sortOption.value = option
            val sortedItems = when (option) {
                SortOption.RECENT -> demoItems.sortedByDescending { it.lastAccessed }
                SortOption.MOST_USED -> demoItems.sortedByDescending { it.accessCount }
                SortOption.ALPHABETICAL -> demoItems.sortedBy { it.title }
                SortOption.PINNED_FIRST -> demoItems.sortedWith(
                    compareByDescending<DemoHistoryItem> { it.isPinned }
                        .thenByDescending { it.lastAccessed }
                )
            }
            _historyState.value = HistoryState.Success(
                items = sortedItems,
                totalCount = sortedItems.size
            )
        }
    }
}

@Composable
private fun createDemoLinkDetailViewModel(url: String): LinkDetailViewModel {
    val context = LocalContext.current
    
    return object : LinkDetailViewModel(context, url) {
        override val linkDetails = MutableStateFlow(
            LinkDetails(
                url = url,
                domain = extractDomain(url),
                title = generateTitle(url),
                accessCount = (1..20).random(),
                lastAccessed = System.currentTimeMillis() - (1..72).random() * 3600000L,
                isPinned = listOf(true, false).random()
            )
        )
        
        override val summaryState = MutableStateFlow(
            SummaryState(
                summary = generateSummary(url),
                isLoading = false,
                lastUpdated = System.currentTimeMillis()
            )
        )
    }
}

@Composable
private fun createDemoQRCodeViewModel(url: String): QRCodeViewModel {
    val context = LocalContext.current
    
    return object : QRCodeViewModel(context, url) {
        override val qrCodeState = MutableStateFlow(
            QRCodeState.Success(
                bitmap = createDemoBitmap(),
                size = 400,
                foregroundColor = android.graphics.Color.BLACK,
                backgroundColor = android.graphics.Color.WHITE
            )
        )
        
        override val sharingState = MutableStateFlow(SharingState.Idle)
        override val qrCodeSize = MutableStateFlow(400)
        override val foregroundColor = MutableStateFlow(androidx.compose.ui.graphics.Color.Black)
        override val backgroundColor = MutableStateFlow(androidx.compose.ui.graphics.Color.White)
    }
}

// Helper functions for demo data
private fun createDemoHistoryItem(
    id: Int,
    url: String,
    domain: String,
    title: String,
    accessCount: Int,
    hoursAgo: Int,
    isPinned: Boolean
): DemoHistoryItem {
    return DemoHistoryItem(
        id = id,
        url = url,
        domain = domain,
        title = title,
        accessCount = accessCount,
        lastAccessed = System.currentTimeMillis() - (hoursAgo * 3600000L),
        isPinned = isPinned,
        summary = generateSummary(url)
    )
}

private fun extractDomain(url: String): String {
    return try {
        java.net.URL(url).host
    } catch (e: Exception) {
        "unknown.com"
    }
}

private fun generateTitle(url: String): String {
    val domain = extractDomain(url)
    return when {
        domain.contains("github") -> "GitHub Repository"
        domain.contains("youtube") -> "YouTube Video"
        domain.contains("stackoverflow") -> "Stack Overflow Question"
        domain.contains("developer.android") -> "Android Documentation"
        domain.contains("medium") -> "Medium Article"
        domain.contains("reddit") -> "Reddit Discussion"
        else -> "Web Page"
    }
}

private fun generateSummary(url: String): String {
    val domain = extractDomain(url)
    return when {
        domain.contains("github") -> "GitHub repository or project page"
        domain.contains("youtube") -> "YouTube video content"
        domain.contains("stackoverflow") -> "Programming Q&A discussion"
        domain.contains("developer.android") -> "Official Android development documentation"
        domain.contains("medium") -> "Medium article or blog post"
        domain.contains("reddit") -> "Reddit community discussion"
        else -> "Web page content"
    }
}

private fun createDemoBitmap(): android.graphics.Bitmap {
    // Create a simple demo bitmap (in real implementation, this would be a QR code)
    return android.graphics.Bitmap.createBitmap(400, 400, android.graphics.Bitmap.Config.ARGB_8888).apply {
        // Fill with a simple pattern to represent QR code
        val canvas = android.graphics.Canvas(this)
        val paint = android.graphics.Paint().apply {
            color = android.graphics.Color.BLACK
        }
        
        // Draw a simple grid pattern
        for (i in 0 until 20) {
            for (j in 0 until 20) {
                if ((i + j) % 2 == 0) {
                    canvas.drawRect(
                        i * 20f, j * 20f,
                        (i + 1) * 20f, (j + 1) * 20f,
                        paint
                    )
                }
            }
        }
    }
}

// Demo data class
data class DemoHistoryItem(
    val id: Int,
    val url: String,
    val domain: String,
    val title: String,
    val accessCount: Int,
    val lastAccessed: Long,
    val isPinned: Boolean,
    val summary: String? = null
)

// Demo enums (these would normally be in the main codebase)
enum class SortOption {
    RECENT, MOST_USED, ALPHABETICAL, PINNED_FIRST
}

enum class ViewMode {
    LIST, GRID, COMPACT
}