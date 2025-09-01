package fe.linksheet.module.ux

import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback

/**
 * User Experience Enhancement utilities
 * Provides improved interactions and feedback
 */
class UserExperienceEnhancer(private val context: Context) {

    /**
     * Get enhanced animation specifications
     */
    fun getEnhancedAnimations(): AnimationSpecs {
        return AnimationSpecs(
            fastAnimation = tween(150),
            normalAnimation = tween(300),
            slowAnimation = tween(500),
            springAnimation = spring(
                dampingRatio = Spring.DampingRatioMediumBouncy,
                stiffness = Spring.StiffnessMedium
            ),
            bounceAnimation = spring(
                dampingRatio = Spring.DampingRatioLowBouncy,
                stiffness = Spring.StiffnessLow
            )
        )
    }

    /**
     * Get enhanced haptic feedback patterns
     */
    fun getHapticPatterns(): HapticPatterns {
        return HapticPatterns(
            lightTap = HapticFeedbackType.TextHandleMove,
            mediumTap = HapticFeedbackType.LongPress,
            strongTap = HapticFeedbackType.LongPress,
            success = HapticFeedbackType.LongPress,
            error = HapticFeedbackType.LongPress
        )
    }

    /**
     * Check if device supports enhanced features
     */
    fun getDeviceCapabilities(): DeviceCapabilities {
        return DeviceCapabilities(
            supportsHapticFeedback = hasHapticFeedback(),
            supportsAdvancedAnimations = hasAdvancedAnimationSupport(),
            supportsGestures = hasGestureSupport(),
            supportsAccessibility = hasAccessibilitySupport()
        )
    }

    /**
     * Get accessibility enhancements
     */
    fun getAccessibilityEnhancements(): AccessibilityEnhancements {
        return AccessibilityEnhancements(
            enableHighContrast = false,
            enableLargeText = false,
            enableReducedMotion = false,
            enableScreenReader = false
        )
    }

    private fun hasHapticFeedback(): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            context.packageManager.hasSystemFeature(PackageManager.FEATURE_VIBRATOR)
        } else {
            true
        }
    }

    private fun hasAdvancedAnimationSupport(): Boolean {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP
    }

    private fun hasGestureSupport(): Boolean {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q
    }

    private fun hasAccessibilitySupport(): Boolean {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH
    }
}

/**
 * Enhanced animation specifications
 */
data class AnimationSpecs(
    val fastAnimation: androidx.compose.animation.core.AnimationSpec<Float>,
    val normalAnimation: androidx.compose.animation.core.AnimationSpec<Float>,
    val slowAnimation: androidx.compose.animation.core.AnimationSpec<Float>,
    val springAnimation: androidx.compose.animation.core.AnimationSpec<Float>,
    val bounceAnimation: androidx.compose.animation.core.AnimationSpec<Float>
)

/**
 * Haptic feedback patterns
 */
data class HapticPatterns(
    val lightTap: HapticFeedbackType,
    val mediumTap: HapticFeedbackType,
    val strongTap: HapticFeedbackType,
    val success: HapticFeedbackType,
    val error: HapticFeedbackType
)

/**
 * Device capability detection
 */
data class DeviceCapabilities(
    val supportsHapticFeedback: Boolean,
    val supportsAdvancedAnimations: Boolean,
    val supportsGestures: Boolean,
    val supportsAccessibility: Boolean
)

/**
 * Accessibility enhancement settings
 */
data class AccessibilityEnhancements(
    val enableHighContrast: Boolean,
    val enableLargeText: Boolean,
    val enableReducedMotion: Boolean,
    val enableScreenReader: Boolean
)

/**
 * Composable helper for enhanced haptic feedback
 */
@Composable
fun EnhancedHapticFeedback(
    onTap: () -> Unit = {},
    onLongPress: () -> Unit = {},
    onSuccess: () -> Unit = {},
    onError: () -> Unit = {}
) {
    val haptic = LocalHapticFeedback.current
    val patterns = UserExperienceEnhancer(androidx.compose.ui.platform.LocalContext.current).getHapticPatterns()
    
    // Provide haptic feedback methods
}