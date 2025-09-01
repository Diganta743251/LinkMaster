package fe.linksheet.composable.ui

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/**
 * Modern 2025 3D Theme System with immersive effects
 */
object Modern3DTheme {
    
    // 3D Effect Parameters
    val DefaultElevation = 8.dp
    val HoverElevation = 16.dp
    val PressedElevation = 4.dp
    
    // Glassmorphism Parameters
    val GlassBlurRadius = 20.dp
    val GlassOpacity = 0.15f
    val GlassBorderOpacity = 0.3f
    
    // Animation Durations
    const val FastAnimation = 150
    const val MediumAnimation = 300
    const val SlowAnimation = 600
    
    // Modern Color Schemes
    val NeonColorScheme = darkColorScheme(
        primary = Color(0xFF8B5CF6),
        onPrimary = Color(0xFF0F0F23),
        primaryContainer = Color(0xFF4F46E5),
        onPrimaryContainer = Color(0xFFE0E7FF),
        secondary = Color(0xFF06B6D4),
        onSecondary = Color(0xFF164E63),
        secondaryContainer = Color(0xFF0891B2),
        onSecondaryContainer = Color(0xFFCFFAFE),
        tertiary = Color(0xFFEC4899),
        onTertiary = Color(0xFF831843),
        background = Color(0xFF0F0F23),
        onBackground = Color(0xFFF9FAFB),
        surface = Color(0xFF1E1B4B),
        onSurface = Color(0xFFF9FAFB),
        surfaceVariant = Color(0xFF312E81),
        onSurfaceVariant = Color(0xFFD1D5DB),
        outline = Color(0xFF6B7280),
        error = Color(0xFFF59E0B),
        onError = Color(0xFF0F0F23)
    )
    
    val GlassColorScheme = darkColorScheme(
        primary = Color(0xFF667EEA),
        onPrimary = Color(0xFFFFFFFF),
        primaryContainer = Color(0xFF1A1A2E),
        onPrimaryContainer = Color(0xFFE0E7FF),
        secondary = Color(0xFFF093FB),
        onSecondary = Color(0xFF831843),
        background = Color(0xFF0F0F23),
        onBackground = Color(0xFFF9FAFB),
        surface = Color(0x261A1A2E),
        onSurface = Color(0xFFF9FAFB),
        outline = Color(0x4D3B4A6B)
    )
}

/**
 * 3D Card with depth and shadow effects
 */
@Composable
fun Modern3DCard(
    modifier: Modifier = Modifier,
    elevation: Dp = Modern3DTheme.DefaultElevation,
    shape: Shape = RoundedCornerShape(16.dp),
    colors: CardColors = CardDefaults.cardColors(),
    content: @Composable ColumnScope.() -> Unit
) {
    var isPressed by remember { mutableStateOf(false) }
    val animatedElevation by animateDpAsState(
        targetValue = if (isPressed) Modern3DTheme.PressedElevation else elevation,
        animationSpec = tween(Modern3DTheme.FastAnimation),
        label = "elevation"
    )
    
    Card(
        modifier = modifier
            .shadow(
                elevation = animatedElevation,
                shape = shape,
                ambientColor = Color.Black.copy(alpha = 0.3f),
                spotColor = Color.Black.copy(alpha = 0.5f)
            ),
        shape = shape,
        colors = colors,
        elevation = CardDefaults.cardElevation(defaultElevation = animatedElevation),
        content = content
    )
}

/**
 * Glassmorphism Card with blur and transparency effects
 */
@Composable
fun GlassmorphismCard(
    modifier: Modifier = Modifier,
    shape: Shape = RoundedCornerShape(16.dp),
    content: @Composable ColumnScope.() -> Unit
) {
    Card(
        modifier = modifier
            .background(
                brush = Brush.linearGradient(
                    colors = listOf(
                        Color.White.copy(alpha = Modern3DTheme.GlassOpacity),
                        Color.White.copy(alpha = Modern3DTheme.GlassOpacity * 0.5f)
                    ),
                    start = androidx.compose.ui.geometry.Offset(0f, 0f),
                    end = androidx.compose.ui.geometry.Offset(1000f, 1000f)
                ),
                shape = shape
            )
            .clip(shape),
        shape = shape,
        colors = CardDefaults.cardColors(
            containerColor = Color.Transparent
        ),
        border = androidx.compose.foundation.BorderStroke(
            width = 1.dp,
            color = Color.White.copy(alpha = Modern3DTheme.GlassBorderOpacity)
        ),
        content = content
    )
}

/**
 * Neon Glow Card with animated glow effects
 */
@Composable
fun NeonGlowCard(
    modifier: Modifier = Modifier,
    glowColor: Color = Color(0xFF8B5CF6),
    shape: Shape = RoundedCornerShape(16.dp),
    content: @Composable ColumnScope.() -> Unit
) {
    val infiniteTransition = rememberInfiniteTransition(label = "neon_glow")
    val glowAlpha by infiniteTransition.animateFloat(
        initialValue = 0.3f,
        targetValue = 0.8f,
        animationSpec = infiniteRepeatable(
            animation = tween(2000, easing = EaseInOutSine),
            repeatMode = RepeatMode.Reverse
        ),
        label = "glow_alpha"
    )
    
    Card(
        modifier = modifier
            .shadow(
                elevation = 12.dp,
                shape = shape,
                ambientColor = glowColor.copy(alpha = glowAlpha),
                spotColor = glowColor.copy(alpha = glowAlpha * 0.7f)
            ),
        shape = shape,
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFF1E1B4B)
        ),
        border = androidx.compose.foundation.BorderStroke(
            width = 1.dp,
            brush = Brush.linearGradient(
                colors = listOf(
                    glowColor.copy(alpha = glowAlpha),
                    glowColor.copy(alpha = glowAlpha * 0.5f),
                    glowColor.copy(alpha = glowAlpha)
                )
            )
        ),
        content = content
    )
}

/**
 * Modern Animated Button with 3D effects
 */
@Composable
fun Modern3DButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    shape: Shape = RoundedCornerShape(12.dp),
    colors: ButtonColors = ButtonDefaults.buttonColors(),
    elevation: ButtonElevation? = ButtonDefaults.buttonElevation(),
    content: @Composable RowScope.() -> Unit
) {
    var isPressed by remember { mutableStateOf(false) }
    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.95f else 1f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessLow
        ),
        label = "button_scale"
    )
    
    Button(
        onClick = {
            isPressed = true
            onClick()
            isPressed = false
        },
        modifier = modifier
            .graphicsLayer {
                scaleX = scale
                scaleY = scale
            },
        enabled = enabled,
        shape = shape,
        colors = colors,
        elevation = elevation,
        content = content
    )
}

/**
 * Floating Action Button with enhanced 3D effects
 */
@Composable
fun Modern3DFAB(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    shape: Shape = RoundedCornerShape(16.dp),
    containerColor: Color = MaterialTheme.colorScheme.primaryContainer,
    contentColor: Color = MaterialTheme.colorScheme.onPrimaryContainer,
    content: @Composable () -> Unit
) {
    var isHovered by remember { mutableStateOf(false) }
    val elevation by animateDpAsState(
        targetValue = if (isHovered) Modern3DTheme.HoverElevation else Modern3DTheme.DefaultElevation,
        animationSpec = tween(Modern3DTheme.MediumAnimation),
        label = "fab_elevation"
    )
    
    FloatingActionButton(
        onClick = onClick,
        modifier = modifier
            .shadow(
                elevation = elevation,
                shape = shape,
                ambientColor = containerColor.copy(alpha = 0.3f),
                spotColor = containerColor.copy(alpha = 0.5f)
            ),
        shape = shape,
        containerColor = containerColor,
        contentColor = contentColor,
        elevation = FloatingActionButtonDefaults.elevation(defaultElevation = elevation),
        content = content
    )
}