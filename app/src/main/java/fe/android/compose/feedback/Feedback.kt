package fe.android.compose.feedback

import android.content.Context
import androidx.compose.runtime.compositionLocalOf

enum class FeedbackType { Confirm, Decline }

interface HapticFeedbackInteraction {
    fun copy(text: String, type: FeedbackType)
    fun wrap(type: FeedbackType, action: () -> Unit): () -> Unit = action
}

val LocalHapticFeedbackInteraction = compositionLocalOf<HapticFeedbackInteraction> {
    object : HapticFeedbackInteraction {
        override fun copy(text: String, type: FeedbackType) {}
    }
}

fun rememberHapticFeedbackInteraction(context: Context): HapticFeedbackInteraction = object : HapticFeedbackInteraction {
    override fun copy(text: String, type: FeedbackType) {}
}
