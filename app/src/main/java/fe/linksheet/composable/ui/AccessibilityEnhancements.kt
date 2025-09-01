package fe.linksheet.composable.ui

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.selection.toggleable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.semantics.*
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/**
 * Enhanced Accessibility Features for 2025
 * Implements WCAG 2.1 AA standards with modern UX patterns
 */

/**
 * High Contrast Theme Colors
 */
object HighContrastColors {
    val surface = Color(0xFF000000)
    val onSurface = Color(0xFFFFFFFF)
    val primary = Color(0xFF00FF00)
    val onPrimary = Color(0xFF000000)
    val secondary = Color(0xFF00FFFF)
    val onSecondary = Color(0xFF000000)
    val error = Color(0xFFFF0000)
    val onError = Color(0xFFFFFFFF)
    val warning = Color(0xFFFFFF00)
    val onWarning = Color(0xFF000000)
}

/**
 * Accessibility-Enhanced Button with multiple feedback types
 */
@Composable
fun AccessibleButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    colors: ButtonColors = ButtonDefaults.buttonColors(),
    contentDescription: String? = null,
    hapticFeedback: Boolean = true,
    content: @Composable RowScope.() -> Unit
) {
    val haptic = LocalHapticFeedback.current
    
    Button(
        onClick = {
            if (hapticFeedback) {
                haptic.performHapticFeedback(androidx.compose.ui.hapticfeedback.HapticFeedbackType.LongPress)
            }
            onClick()
        },
        modifier = modifier
            .semantics {
                contentDescription?.let { this.contentDescription = it }
                role = Role.Button
                if (!enabled) {
                    disabled()
                }
            }
            .heightIn(min = 48.dp), // Minimum touch target size
        enabled = enabled,
        colors = colors,
        shape = RoundedCornerShape(12.dp),
        elevation = ButtonDefaults.buttonElevation(
            defaultElevation = 4.dp,
            pressedElevation = 8.dp,
            disabledElevation = 0.dp
        ),
        content = content
    )
}

/**
 * Voice-Guided Navigation Component
 */
@Composable
fun VoiceGuidedNavigation(
    currentScreen: String,
    availableActions: List<String>,
    onActionSelected: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .semantics {
                contentDescription = "Voice navigation for $currentScreen. Available actions: ${availableActions.joinToString(", ")}"
                role = Role.Button
            }
    ) {
        Text(
            text = "Voice Commands Available:",
            style = MaterialTheme.typography.titleMedium.copy(
                fontWeight = FontWeight.SemiBold
            ),
            modifier = Modifier.padding(16.dp)
        )
        
        availableActions.forEach { action ->
            AccessibleButton(
                onClick = { onActionSelected(action) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 4.dp),
                contentDescription = "Voice command: $action"
            ) {
                Icon(
                    imageVector = Icons.Default.Mic,
                    contentDescription = null,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(text = action)
            }
        }
    }
}

/**
 * Screen Reader Optimized List Item
 */
@Composable
fun AccessibleListItem(
    title: String,
    subtitle: String? = null,
    icon: ImageVector? = null,
    onClick: (() -> Unit)? = null,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    isSelected: Boolean = false,
    customSemantics: String? = null
) {
    val semanticsDescription = customSemantics ?: buildString {
        append(title)
        subtitle?.let { append(", $it") }
        if (isSelected) append(", selected")
        if (!enabled) append(", disabled")
        onClick?.let { append(", button") }
    }
    
    Modern3DCard(
        modifier = modifier
            .fillMaxWidth()
            .then(
                if (onClick != null) {
                    Modifier
                        .clickable(
                            enabled = enabled,
                            onClickLabel = title,
                            role = Role.Button
                        ) { onClick() }
                        .semantics {
                            contentDescription = semanticsDescription
                            if (isSelected) {
                                selected = true
                            }
                        }
                } else {
                    Modifier.semantics {
                        contentDescription = semanticsDescription
                    }
                }
            )
            .heightIn(min = 56.dp), // Minimum touch target
        elevation = if (isSelected) 8.dp else 4.dp,
        colors = CardDefaults.cardColors(
            containerColor = if (isSelected) {
                MaterialTheme.colorScheme.primaryContainer
            } else {
                MaterialTheme.colorScheme.surface
            }
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            icon?.let {
                Icon(
                    imageVector = it,
                    contentDescription = null, // Handled by parent semantics
                    modifier = Modifier.size(24.dp),
                    tint = if (isSelected) {
                        MaterialTheme.colorScheme.onPrimaryContainer
                    } else {
                        MaterialTheme.colorScheme.onSurface
                    }
                )
                Spacer(modifier = Modifier.width(16.dp))
            }
            
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Medium
                    ),
                    color = if (isSelected) {
                        MaterialTheme.colorScheme.onPrimaryContainer
                    } else {
                        MaterialTheme.colorScheme.onSurface
                    }
                )
                
                subtitle?.let {
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = it,
                        style = MaterialTheme.typography.bodySmall,
                        color = if (isSelected) {
                            MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.7f)
                        } else {
                            MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                        }
                    )
                }
            }
            
            if (isSelected) {
                Icon(
                    imageVector = Icons.Default.Check,
                    contentDescription = null, // Handled by parent semantics
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(20.dp)
                )
            }
        }
    }
}

/**
 * Focus-Aware Container with enhanced keyboard navigation
 */
@Composable
fun FocusAwareContainer(
    modifier: Modifier = Modifier,
    focusable: Boolean = true,
    onFocusChanged: (Boolean) -> Unit = {},
    content: @Composable () -> Unit
) {
    var isFocused by remember { mutableStateOf(false) }
    
    Box(
        modifier = modifier
            .then(
                if (focusable) {
                    Modifier
                        .focusable()
                        .onFocusChanged { focusState ->
                            val focused = focusState.isFocused
                            if (focused != isFocused) {
                                isFocused = focused
                                onFocusChanged(focused)
                            }
                        }
                        .border(
                            width = if (isFocused) 2.dp else 0.dp,
                            color = if (isFocused) MaterialTheme.colorScheme.primary else Color.Transparent,
                            shape = RoundedCornerShape(8.dp)
                        )
                } else Modifier
            )
    ) {
        content()
    }
}

/**
 * Large Text Support Component
 */
@Composable
fun ScalableText(
    text: String,
    modifier: Modifier = Modifier,
    style: androidx.compose.ui.text.TextStyle = MaterialTheme.typography.bodyMedium,
    maxLines: Int = Int.MAX_VALUE,
    textAlign: TextAlign? = null,
    fontSizeMultiplier: Float = 1f
) {
    Text(
        text = text,
        modifier = modifier,
        style = style.copy(
            fontSize = style.fontSize * fontSizeMultiplier
        ),
        maxLines = maxLines,
        textAlign = textAlign
    )
}

/**
 * Color Blind Friendly Status Indicator
 */
@Composable
fun AccessibleStatusIndicator(
    status: String,
    isPositive: Boolean,
    modifier: Modifier = Modifier,
    showIcon: Boolean = true,
    showText: Boolean = true
) {
    val (color, icon, description) = when {
        isPositive -> Triple(
            Color(0xFF4CAF50), // Green
            Icons.Default.CheckCircle,
            "Success: $status"
        )
        else -> Triple(
            Color(0xFFF44336), // Red
            Icons.Default.Error,
            "Error: $status"
        )
    }
    
    Row(
        modifier = modifier
            .semantics {
                contentDescription = description
                role = Role.Image
            },
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (showIcon) {
            Icon(
                imageVector = icon,
                contentDescription = null, // Handled by parent semantics
                tint = color,
                modifier = Modifier.size(20.dp)
            )
            
            if (showText) {
                Spacer(modifier = Modifier.width(8.dp))
            }
        }
        
        if (showText) {
            Text(
                text = status,
                style = MaterialTheme.typography.bodyMedium.copy(
                    fontWeight = FontWeight.Medium
                ),
                color = color
            )
        }
    }
}

/**
 * Gesture-Friendly Swipe Actions
 */
@Composable
fun SwipeActionContainer(
    onSwipeLeft: (() -> Unit)? = null,
    onSwipeRight: (() -> Unit)? = null,
    leftActionLabel: String = "Left action",
    rightActionLabel: String = "Right action",
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    var swipeOffset by remember { mutableStateOf(0f) }
    
    Box(
        modifier = modifier
            .semantics {
                customActions = listOfNotNull(
                    onSwipeLeft?.let {
                        CustomAccessibilityAction(leftActionLabel) {
                            it()
                            true
                        }
                    },
                    onSwipeRight?.let {
                        CustomAccessibilityAction(rightActionLabel) {
                            it()
                            true
                        }
                    }
                )
            }
    ) {
        // Background actions
        Row(
            modifier = Modifier.fillMaxSize(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            onSwipeRight?.let {
                Box(
                    modifier = Modifier
                        .fillMaxHeight()
                        .width(80.dp)
                        .background(Color(0xFF4CAF50)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Check,
                        contentDescription = rightActionLabel,
                        tint = Color.White
                    )
                }
            }
            
            Spacer(modifier = Modifier.weight(1f))
            
            onSwipeLeft?.let {
                Box(
                    modifier = Modifier
                        .fillMaxHeight()
                        .width(80.dp)
                        .background(Color(0xFFF44336)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = leftActionLabel,
                        tint = Color.White
                    )
                }
            }
        }
        
        // Main content
        Box(
            modifier = Modifier
                .fillMaxSize()
                .offset(x = swipeOffset.dp)
        ) {
            content()
        }
    }
}

/**
 * Accessibility Settings Panel
 */
@Composable
fun AccessibilitySettingsPanel(
    highContrastEnabled: Boolean,
    onHighContrastChanged: (Boolean) -> Unit,
    largeTextEnabled: Boolean,
    onLargeTextChanged: (Boolean) -> Unit,
    hapticFeedbackEnabled: Boolean,
    onHapticFeedbackChanged: (Boolean) -> Unit,
    voiceGuidanceEnabled: Boolean,
    onVoiceGuidanceChanged: (Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = "Accessibility Settings",
            style = MaterialTheme.typography.headlineSmall.copy(
                fontWeight = FontWeight.Bold
            ),
            modifier = Modifier.padding(horizontal = 16.dp)
        )
        
        ModernSwitchPreference(
            title = "High Contrast Mode",
            summary = "Increases color contrast for better visibility",
            checked = highContrastEnabled,
            onCheckedChange = onHighContrastChanged,
            icon = Icons.Default.Contrast,
            modifier = Modifier.padding(horizontal = 16.dp)
        )
        
        ModernSwitchPreference(
            title = "Large Text",
            summary = "Increases text size throughout the app",
            checked = largeTextEnabled,
            onCheckedChange = onLargeTextChanged,
            icon = Icons.Default.FormatSize,
            modifier = Modifier.padding(horizontal = 16.dp)
        )
        
        ModernSwitchPreference(
            title = "Haptic Feedback",
            summary = "Provides tactile feedback for interactions",
            checked = hapticFeedbackEnabled,
            onCheckedChange = onHapticFeedbackChanged,
            icon = Icons.Default.Vibration,
            modifier = Modifier.padding(horizontal = 16.dp)
        )
        
        ModernSwitchPreference(
            title = "Voice Guidance",
            summary = "Enables voice commands and audio feedback",
            checked = voiceGuidanceEnabled,
            onCheckedChange = onVoiceGuidanceChanged,
            icon = Icons.Default.RecordVoiceOver,
            modifier = Modifier.padding(horizontal = 16.dp)
        )
    }
}