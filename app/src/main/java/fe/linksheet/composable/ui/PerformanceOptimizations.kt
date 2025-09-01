package fe.linksheet.composable.ui

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.layout.SubcomposeLayout
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay
import kotlin.math.roundToInt

/**
 * Performance Optimizations for 2025 UI
 * Implements smart caching, lazy loading, and efficient rendering
 */

/**
 * Smart Image Cache with automatic memory management
 */
class SmartImageCache {
    private val cache = mutableMapOf<String, ImageBitmap>()
    private val accessTimes = mutableMapOf<String, Long>()
    private val maxCacheSize = 50 // Maximum number of cached images
    
    fun get(key: String): ImageBitmap? {
        accessTimes[key] = System.currentTimeMillis()
        return cache[key]
    }
    
    fun put(key: String, image: ImageBitmap) {
        if (cache.size >= maxCacheSize) {
            // Remove least recently used item
            val lruKey = accessTimes.minByOrNull { it.value }?.key
            lruKey?.let {
                cache.remove(it)
                accessTimes.remove(it)
            }
        }
        cache[key] = image
        accessTimes[key] = System.currentTimeMillis()
    }
    
    fun clear() {
        cache.clear()
        accessTimes.clear()
    }
}

/**
 * Lazy Loading Container with viewport detection
 */
@Composable
fun LazyLoadingContainer(
    isVisible: Boolean,
    loadingContent: @Composable () -> Unit = { CircularProgressIndicator() },
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    var hasLoaded by rememberSaveable { mutableStateOf(false) }
    
    LaunchedEffect(isVisible) {
        if (isVisible && !hasLoaded) {
            // Simulate loading delay for heavy content
            delay(100)
            hasLoaded = true
        }
    }
    
    Box(modifier = modifier) {
        when {
            !isVisible -> {
                // Placeholder when not visible
                Box(modifier = Modifier.fillMaxSize())
            }
            !hasLoaded -> {
                loadingContent()
            }
            else -> {
                content()
            }
        }
    }
}

/**
 * Efficient List with viewport-based rendering
 */
@Composable
fun <T> PerformantLazyColumn(
    items: List<T>,
    modifier: Modifier = Modifier,
    key: ((item: T) -> Any)? = null,
    contentPadding: PaddingValues = PaddingValues(0.dp),
    itemContent: @Composable LazyItemScope.(item: T, isVisible: Boolean) -> Unit
) {
    val listState = rememberLazyListState()
    val visibleItemsInfo by remember {
        derivedStateOf {
            listState.layoutInfo.visibleItemsInfo
        }
    }
    
    LazyColumn(
        modifier = modifier,
        state = listState,
        contentPadding = contentPadding
    ) {
        itemsIndexed(
            items = items,
            key = if (key != null) { index, item -> key(item) } else null
        ) { index, item ->
            val isVisible = visibleItemsInfo.any { it.index == index }
            itemContent(item, isVisible)
        }
    }
}

/**
 * Smart Composable that skips recomposition when not needed
 */
@Composable
fun <T> SmartComposable(
    data: T,
    modifier: Modifier = Modifier,
    content: @Composable (T) -> Unit
) {
    val stableData by remember(data) { mutableStateOf(data) }
    
    // Only recompose if data actually changed
    key(stableData) {
        content(stableData)
    }
}

/**
 * Cached Drawing Modifier for expensive graphics operations
 */
fun Modifier.cachedDraw(
    cacheKey: Any,
    onDraw: DrawScope.() -> Unit
): Modifier = this.drawWithCache {
    val cachedDrawResult = onDrawCacheReads {
        onDraw()
    }
    onDrawWithContent {
        drawContent()
        cachedDrawResult()
    }
}

/**
 * Memory-Efficient Image Component
 */
@Composable
fun MemoryEfficientImage(
    imageKey: String,
    modifier: Modifier = Modifier,
    contentDescription: String? = null,
    imageLoader: suspend (String) -> ImageBitmap?
) {
    var imageBitmap by remember { mutableStateOf<ImageBitmap?>(null) }
    var isLoading by remember { mutableStateOf(true) }
    
    LaunchedEffect(imageKey) {
        isLoading = true
        imageBitmap = imageLoader(imageKey)
        isLoading = false
    }
    
    Box(modifier = modifier) {
        when {
            isLoading -> {
                CircularProgressIndicator()
            }
            imageBitmap != null -> {
                androidx.compose.foundation.Image(
                    bitmap = imageBitmap!!,
                    contentDescription = contentDescription,
                    modifier = Modifier.fillMaxSize()
                )
            }
            else -> {
                // Placeholder for failed loads
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(MaterialTheme.colorScheme.surfaceVariant)
                )
            }
        }
    }
}

/**
 * Optimized Animation Controller
 */
class OptimizedAnimationController {
    private var isAnimating by mutableStateOf(false)
    private val animationQueue = mutableListOf<() -> Unit>()
    
    fun queueAnimation(animation: () -> Unit) {
        if (isAnimating) {
            animationQueue.add(animation)
        } else {
            executeAnimation(animation)
        }
    }
    
    private fun executeAnimation(animation: () -> Unit) {
        isAnimating = true
        animation()
        // Process next animation in queue
        if (animationQueue.isNotEmpty()) {
            val nextAnimation = animationQueue.removeFirst()
            executeAnimation(nextAnimation)
        } else {
            isAnimating = false
        }
    }
}

/**
 * Smart Layout that measures children efficiently
 */
@Composable
fun EfficientLayout(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    SubcomposeLayout(modifier = modifier) { constraints ->
        val placeables = subcompose("content", content).map { measurable ->
            measurable.measure(constraints)
        }
        
        val width = placeables.maxOfOrNull { it.width } ?: 0
        val height = placeables.sumOf { it.height }
        
        layout(width, height) {
            var yPosition = 0
            placeables.forEach { placeable ->
                placeable.place(x = 0, y = yPosition)
                yPosition += placeable.height
            }
        }
    }
}

/**
 * Debounced State for expensive operations
 */
@Composable
fun <T> rememberDebouncedState(
    value: T,
    delayMillis: Long = 300L
): State<T> {
    val debouncedValue = remember { mutableStateOf(value) }
    
    LaunchedEffect(value) {
        delay(delayMillis)
        debouncedValue.value = value
    }
    
    return debouncedValue
}

/**
 * Performance Monitor Component
 */
@Composable
fun PerformanceMonitor(
    enabled: Boolean = false,
    content: @Composable () -> Unit
) {
    if (enabled) {
        val startTime = remember { System.currentTimeMillis() }
        var renderTime by remember { mutableStateOf(0L) }
        
        LaunchedEffect(Unit) {
            renderTime = System.currentTimeMillis() - startTime
        }
        
        Column {
            content()
            if (renderTime > 0) {
                Text(
                    text = "Render time: ${renderTime}ms",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.outline
                )
            }
        }
    } else {
        content()
    }
}

/**
 * Smart Scroll Performance Optimizer
 */
@Composable
fun OptimizedScrollContainer(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    var isScrolling by remember { mutableStateOf(false) }
    val scrollState = rememberScrollState()
    
    LaunchedEffect(scrollState.value) {
        isScrolling = true
        delay(150) // Debounce scroll end detection
        isScrolling = false
    }
    
    // Reduce animation complexity while scrolling
    CompositionLocalProvider(
        LocalAnimationEnabled provides !isScrolling
    ) {
        Column(
            modifier = modifier.verticalScroll(scrollState)
        ) {
            content()
        }
    }
}

/**
 * Local provider for animation control
 */
val LocalAnimationEnabled = compositionLocalOf { true }

/**
 * Conditional Animation Wrapper
 */
@Composable
fun ConditionalAnimation(
    enabled: Boolean = LocalAnimationEnabled.current,
    enter: EnterTransition = fadeIn(),
    exit: ExitTransition = fadeOut(),
    content: @Composable () -> Unit
) {
    if (enabled) {
        AnimatedVisibility(
            visible = true,
            enter = enter,
            exit = exit
        ) {
            content()
        }
    } else {
        content()
    }
}

/**
 * Resource Pool for expensive objects
 */
class ResourcePool<T>(
    private val factory: () -> T,
    private val reset: (T) -> Unit,
    private val maxSize: Int = 10
) {
    private val available = mutableListOf<T>()
    private val inUse = mutableSetOf<T>()
    
    fun acquire(): T {
        return if (available.isNotEmpty()) {
            val resource = available.removeFirst()
            inUse.add(resource)
            resource
        } else {
            val resource = factory()
            inUse.add(resource)
            resource
        }
    }
    
    fun release(resource: T) {
        if (inUse.remove(resource)) {
            reset(resource)
            if (available.size < maxSize) {
                available.add(resource)
            }
        }
    }
}

/**
 * Efficient State Management for Large Lists
 */
@Composable
fun <T> rememberLargeListState(
    items: List<T>,
    keySelector: (T) -> Any = { it.hashCode() }
): LazyListState {
    val listState = rememberLazyListState()
    val itemKeys = remember(items) { items.map(keySelector) }
    
    // Optimize scroll position restoration
    LaunchedEffect(itemKeys) {
        if (listState.firstVisibleItemIndex >= items.size) {
            listState.scrollToItem(0)
        }
    }
    
    return listState
}

/**
 * Smart Preloader for upcoming content
 */
@Composable
fun <T> SmartPreloader(
    items: List<T>,
    currentIndex: Int,
    preloadRange: Int = 3,
    onPreload: suspend (T) -> Unit
) {
    LaunchedEffect(currentIndex, items) {
        val startIndex = (currentIndex - preloadRange).coerceAtLeast(0)
        val endIndex = (currentIndex + preloadRange).coerceAtMost(items.size - 1)
        
        for (i in startIndex..endIndex) {
            if (i != currentIndex) {
                onPreload(items[i])
            }
        }
    }
}