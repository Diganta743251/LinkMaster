package fe.linksheet.composable.ui

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.LazyItemScope
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import kotlin.math.PI
import kotlin.math.sin

/**
 * Modern 2025 Animation System with fluid micro-interactions
 */
object ModernAnimations {
    
    // Animation Specifications
    val SpringyEnter = spring<Float>(
        dampingRatio = Spring.DampingRatioMediumBouncy,
        stiffness = Spring.StiffnessLow
    )
    
    val SmoothEnter = tween<Float>(
        durationMillis = 300,
        easing = EaseOutCubic
    )
    
    val QuickPulse = tween<Float>(
        durationMillis = 150,
        easing = EaseInOutQuart
    )
    
    val FluidTransition = tween<Float>(
        durationMillis = 400,
        easing = CubicBezierEasing(0.4f, 0.0f, 0.2f, 1.0f)
    )
    
    // Entrance Animations
    val SlideInFromBottom = slideInVertically(
        initialOffsetY = { it },
        animationSpec = SpringyEnter
    ) + fadeIn(animationSpec = SmoothEnter)
    
    val SlideInFromRight = slideInHorizontally(
        initialOffsetX = { it },
        animationSpec = SpringyEnter
    ) + fadeIn(animationSpec = SmoothEnter)
    
    val ScaleInWithBounce = scaleIn(
        initialScale = 0.8f,
        animationSpec = SpringyEnter
    ) + fadeIn(animationSpec = SmoothEnter)
    
    // Exit Animations
    val SlideOutToBottom = slideOutVertically(
        targetOffsetY = { it },
        animationSpec = FluidTransition
    ) + fadeOut(animationSpec = QuickPulse)
    
    val SlideOutToRight = slideOutHorizontally(
        targetOffsetX = { it },
        animationSpec = FluidTransition
    ) + fadeOut(animationSpec = QuickPulse)
    
    val ScaleOutWithFade = scaleOut(
        targetScale = 0.8f,
        animationSpec = FluidTransition
    ) + fadeOut(animationSpec = QuickPulse)
}

/**
 * Bouncy tap animation modifier
 */
@Composable
fun Modifier.bouncyTap(
    onTap: () -> Unit = {},
    scaleDown: Float = 0.95f
): Modifier {
    var isPressed by remember { mutableStateOf(false) }
    val scale by animateFloatAsState(
        targetValue = if (isPressed) scaleDown else 1f,
        animationSpec = ModernAnimations.SpringyEnter,
        label = "bouncy_tap"
    )
    
    return this
        .scale(scale)
        .pointerInput(Unit) {
            detectTapGestures(
                onPress = {
                    isPressed = true
                    tryAwaitRelease()
                    isPressed = false
                },
                onTap = { onTap() }
            )
        }
}

/**
 * Floating animation modifier
 */
@Composable
fun Modifier.floatingAnimation(
    amplitude: Dp = 4.dp,
    duration: Int = 3000
): Modifier {
    val density = LocalDensity.current
    val infiniteTransition = rememberInfiniteTransition(label = "floating")
    
    val offsetY by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 2 * PI.toFloat(),
        animationSpec = infiniteRepeatable(
            animation = tween(duration, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "float_offset"
    )
    
    return this.graphicsLayer {
        translationY = with(density) { amplitude.toPx() } * sin(offsetY)
    }
}

/**
 * Shimmer loading animation
 */
@Composable
fun Modifier.shimmerEffect(): Modifier {
    val infiniteTransition = rememberInfiniteTransition(label = "shimmer")
    val alpha by infiniteTransition.animateFloat(
        initialValue = 0.3f,
        targetValue = 0.8f,
        animationSpec = infiniteRepeatable(
            animation = tween(1000, easing = EaseInOutSine),
            repeatMode = RepeatMode.Reverse
        ),
        label = "shimmer_alpha"
    )
    
    return this.graphicsLayer {
        this.alpha = alpha
    }
}

/**
 * Pulse animation modifier
 */
@Composable
fun Modifier.pulseAnimation(
    minScale: Float = 0.95f,
    maxScale: Float = 1.05f,
    duration: Int = 1500
): Modifier {
    val infiniteTransition = rememberInfiniteTransition(label = "pulse")
    val scale by infiniteTransition.animateFloat(
        initialValue = minScale,
        targetValue = maxScale,
        animationSpec = infiniteRepeatable(
            animation = tween(duration, easing = EaseInOutSine),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulse_scale"
    )
    
    return this.scale(scale)
}

/**
 * Rotation animation modifier
 */
@Composable
fun Modifier.rotateAnimation(
    duration: Int = 2000,
    clockwise: Boolean = true
): Modifier {
    val infiniteTransition = rememberInfiniteTransition(label = "rotation")
    val rotation by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = if (clockwise) 360f else -360f,
        animationSpec = infiniteRepeatable(
            animation = tween(duration, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "rotation_angle"
    )
    
    return this.graphicsLayer {
        rotationZ = rotation
    }
}

/**
 * Staggered list animation
 */
@Composable
fun <T> LazyListScope.itemsWithAnimation(
    items: List<T>,
    key: ((item: T) -> Any)? = null,
    contentType: (item: T) -> Any? = { null },
    itemContent: @Composable LazyItemScope.(item: T) -> Unit
) {
    items(
        count = items.size,
        key = if (key != null) { index -> key(items[index]) } else null,
        contentType = { index -> contentType(items[index]) }
    ) { index ->
        val item = items[index]
        
        AnimatedVisibility(
            visible = true,
            enter = ModernAnimations.SlideInFromBottom + fadeIn(
                animationSpec = tween(
                    durationMillis = 300,
                    delayMillis = index * 50 // Stagger delay
                )
            ),
            exit = ModernAnimations.SlideOutToBottom
        ) {
            itemContent(item)
        }
    }
}

/**
 * Modern page transition animations
 */
object PageTransitions {
    val SlideHorizontal = slideInHorizontally(
        initialOffsetX = { it },
        animationSpec = ModernAnimations.FluidTransition
    ) + fadeIn(animationSpec = ModernAnimations.SmoothEnter) with
            slideOutHorizontally(
                targetOffsetX = { -it },
                animationSpec = ModernAnimations.FluidTransition
            ) + fadeOut(animationSpec = ModernAnimations.QuickPulse)
    
    val SlideVertical = slideInVertically(
        initialOffsetY = { it },
        animationSpec = ModernAnimations.FluidTransition
    ) + fadeIn(animationSpec = ModernAnimations.SmoothEnter) with
            slideOutVertically(
                targetOffsetY = { -it },
                animationSpec = ModernAnimations.FluidTransition
            ) + fadeOut(animationSpec = ModernAnimations.QuickPulse)
    
    val ScaleTransition = scaleIn(
        initialScale = 0.9f,
        animationSpec = ModernAnimations.SpringyEnter
    ) + fadeIn(animationSpec = ModernAnimations.SmoothEnter) with
            scaleOut(
                targetScale = 1.1f,
                animationSpec = ModernAnimations.FluidTransition
            ) + fadeOut(animationSpec = ModernAnimations.QuickPulse)
}

/**
 * Animated counter component
 */
@Composable
fun AnimatedCounter(
    count: Int,
    modifier: Modifier = Modifier,
    style: androidx.compose.ui.text.TextStyle = MaterialTheme.typography.headlineMedium
) {
    var oldCount by remember { mutableStateOf(count) }
    val animatedCount by animateIntAsState(
        targetValue = count,
        animationSpec = tween(
            durationMillis = 500,
            easing = EaseOutCubic
        ),
        label = "counter"
    )
    
    LaunchedEffect(count) {
        oldCount = count
    }
    
    Text(
        text = animatedCount.toString(),
        modifier = modifier,
        style = style
    )
}