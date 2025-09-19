package com.example.klist

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.material3.Divider
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.lazy.itemsIndexed as _itemsIndexed

/**
 * Builder-style DSL inspired by Modifier chaining for lists.
 *
 * Usage (example):
 * KList
 *   .builder<T> //For setting up T initially
 *   .padding(10.dp)
 *   .header(title = "Top Gainers")
 *   .items(listOfCoins) { coin -> KListItem(coin) }
 *   .render()
 */

// Top-level entrypoint to start a fresh builder
object KList {
    fun <T> builder(): KListBuilder<T> = KListBuilder()

    // Convenience functions for chaining directly from `KList`
    fun padding(dp: Dp) = KListBuilder<Any>().padding(dp)
    fun header(title: String) = KListBuilder<Any>().header(title)
    fun <T> items(list: List<T>, itemContent: @Composable (T) -> Unit): KListBuilder<T> =
        KListBuilder<T>().items(list, itemContent)
}

/**
 * Generic builder class holding configuration state. Methods are chainable and return `this`.
 */
class KListBuilder<T> internal constructor() {
    internal var paddingDp: Dp = 0.dp
    internal var headerTitle: String? = null
    internal var itemsList: List<T> = emptyList()
    internal var itemContent: (@Composable (T) -> Unit)? = null
    internal var itemClickable: ((T) -> Unit)? = null
    internal var showDividers: Boolean = false
    internal var useSlideInOut: Boolean = false
    internal var showShimmer: Boolean = false
    internal var shimmerItemCount: Int = 5

    /*
     * Requirements: Chainable modifiers
     */
    fun padding(dp: Dp) = apply { this.paddingDp = dp }
    fun header(title: String) = apply { this.headerTitle = title }

    /**
     * Provide the list and the Composable lambda that renders each item.
     */
    fun items(list: List<T>, itemContent: @Composable (T) -> Unit): KListBuilder<T> = apply {
        this.itemsList = list
        this.itemContent = itemContent
    }

    /**
     * Bonus 1: Clickable. Returns `this` for chaining.
     */
    fun clickable(onClick: (T) -> Unit) = apply { this.itemClickable = onClick }

    /**
     * Bonus 2: Dividers between items
     */
    fun dividers(enabled: Boolean = true) = apply { this.showDividers = enabled }

    /**
     * Bonus 3: Slide in Animation
     */
    fun slideInOut(enabled: Boolean = true) = apply { this.useSlideInOut = enabled }

    /**
     * Bonus 4: Shimmer Effect
     */
    fun shimmer(enabled: Boolean = true, itemCount: Int = 5) = apply {
        this.showShimmer = enabled
        this.shimmerItemCount = itemCount
    }

    /**
     * Render function to be called inside a @Composable scope.
     */
    @Composable
    fun render() = apply {
        Column(modifier = Modifier.padding(paddingDp)) {
            headerTitle?.let { title ->
                Text(
                    text = title,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 8.dp)
                )
            }


            // Use LazyColumn to render the items efficiently.
            if (showShimmer) {
                LazyColumn {
                    items(shimmerItemCount) {
                        ShimmerItemPlaceholder()
                    }
                }
            } else {
                LazyColumn {
                    itemsIndexed(itemsList) { index, item ->
                        val itemComposable = itemContent
                        if (itemComposable != null) {
                            val clickableModifier = if (itemClickable != null) {
                                Modifier.clickable { itemClickable?.invoke(item) }
                            } else Modifier

                            AnimatedVisibility(
                                visible = true, // could hook into state if you want dynamic show/hide
                                enter = if (useSlideInOut) slideInVertically(initialOffsetY = { it }) else EnterTransition.None,
                                exit = if (useSlideInOut) slideOutVertically(targetOffsetY = { it }) else ExitTransition.None
                            ) {
                                Column(modifier = Modifier.then(clickableModifier)) {
                                    itemComposable(item)
                                    if (showDividers && index < itemsList.size - 1) {
                                        HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

// Because androidx.compose.foundation.lazy.itemsIndexed is an extension on LazyListScope,
// we reimplement a small helper so the above file compiles without importing additional helper functions.

private fun <T> LazyListScope.itemsIndexed(
    list: List<T>,
    itemContent: @Composable (Int, T) -> Unit
) {
    _itemsIndexed(list) { index, item -> itemContent(index, item) }
}
