package fe.android.span.helper

import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.text.SpanStyle

// Minimal span/link helpers used by Theme/AppTheme

data class LinkAnnotationStyle(val style: SpanStyle)

class LinkTags(val urlIds: Map<String, String> = emptyMap())

val LocalLinkAnnotationStyle = compositionLocalOf { LinkAnnotationStyle(SpanStyle()) }
val LocalLinkTags = compositionLocalOf { LinkTags() }
