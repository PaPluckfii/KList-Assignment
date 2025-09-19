package com.example.klist

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

/**
 * A reusable item component used inside KList
 */
@Composable
fun KListItem(title: String, subtitle: String? = null) {
    Column(modifier = Modifier.padding(horizontal = 8.dp, vertical = 12.dp)) {
        Text(text = title, style = MaterialTheme.typography.bodyMedium)
        subtitle?.let {
            Text(text = it, style = MaterialTheme.typography.bodySmall, modifier = Modifier.padding(top = 4.dp))
        }
    }
}

@Composable
fun <T> KListItem(data: T, textExtractor: (T) -> Pair<String, String?> = { it.toString() to null }) {
    val (title, subtitle) = textExtractor(data)
    KListItem(title = title, subtitle = subtitle)
}