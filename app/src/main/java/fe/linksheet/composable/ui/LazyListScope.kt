package fe.linksheet.composable.ui

import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.LazyItemScope
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.runtime.Composable

fun <T> LazyListScope.items(
    items: List<T>,
    key: ((T) -> Any)? = null,
    itemContent: @Composable LazyItemScope.(T) -> Unit
) {
    items(items = items, key = key, itemContent = itemContent)
}

fun <T> LazyListScope.itemsIndexed(
    items: List<T>,
    key: ((index: Int, item: T) -> Any)? = null,
    itemContent: @Composable LazyItemScope.(index: Int, item: T) -> Unit
) {
    itemsIndexed(items = items, key = key, itemContent = itemContent)
}
