# KList-Assignment -> KList DSL (Jetpack Compose)

A **custom DSL for building list-based UIs** in Jetpack Compose, inspired by the `Modifier` chaining pattern.  
This project demonstrates how to design scalable, reusable, and declarative UI patterns using Kotlin and Compose.

---

## ✨ Features

- Fluent, chainable API (inspired by `Modifier.padding(...)`)
- Simple and clean `KList` builder
- Support for:
  - `.padding(dp)` → outer padding
  - `.header(title)` → optional section header
  - `.items(list) { }` → declarative list items
  - `.clickable { }` → item click support
  - `.dividers(true)` → optional dividers
  - `.slideInOut()` → animated item entry/exit
  - `.loading(true)` → loading state with progress indicator
  - `.shimmerLoading(true)` → shimmer placeholders while data loads

---

## 🚀 Usage

### Example

```kotlin
@Composable
fun HomeScreen(coins: List<Coin>, isLoading: Boolean, showShimmer: Boolean) {
    KList
        .padding(16.dp)
        .header("Top Gainers")
        .loading(isLoading)              // spinner-style loading
        .shimmerLoading(showShimmer, 6)  // shimmer placeholders
        .items(coins) { coin ->
            KListItem(coin) { c -> 
                c.symbol to "${c.priceChange}%" 
            }
        }
        .dividers(true)
        .slideInOut()
        .render()
}
