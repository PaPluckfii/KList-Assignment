package com.example.klist

import android.widget.Toast
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

/**
 * Sample for Using KList
 */

//Sample Data Class
data class Coin(val symbol: String, val priceChange: Double)

@Composable
fun HomeScreenSample(
    modifier: Modifier = Modifier,
    coins: List<Coin>
) {

    val context = LocalContext.current

    Surface(
        modifier = modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        // Example usage using the top-level KList convenience entrypoints
        KList
            .builder<Coin>()
            .header("Top Gainers")
            .padding(16.dp)
            .items(coins) { coin ->
                // We use the generic KListItem overload with an extractor lambda
                KListItem(coin) { c -> c.symbol to "${c.priceChange}%" }
            }
            .clickable {
                Toast.makeText(
                    context,
                    "Clicked ${it.symbol}",
                    Toast.LENGTH_SHORT
                ).show()
            }
            .dividers()
//            .slideInOut()
//            .shimmer()
            .render()
    }
}

@Preview
@Composable
fun PreviewHomeScreenSample() {
    HomeScreenSample(
        coins = listOf(
            Coin("BTC", 1.23),
            Coin("ETH", 0.98),
            Coin("LTC", 0.76),
        )
    )
}