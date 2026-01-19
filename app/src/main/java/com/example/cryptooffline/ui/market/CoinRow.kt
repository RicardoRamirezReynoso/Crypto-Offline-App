package com.example.cryptooffline.ui.market

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.StarOutline
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.cryptooffline.data.local.CoinEntity
import java.text.NumberFormat
import java.util.Locale

@Composable
fun CoinRow(
    coin: CoinEntity,
    currency: String,
    onFavoriteClick: (String, Boolean) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {  }
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Icono de la moneda
        AsyncImage(
            model = coin.image,
            contentDescription = "Icono de ${coin.name}",
            modifier = Modifier.size(40.dp)
        )

        Spacer(modifier = Modifier.width(16.dp))

        // Nombre y Símbolo
        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = coin.name,
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "${coin.symbol.uppercase()}/${currency.uppercase()}",
                style = MaterialTheme.typography.bodyMedium,
                color = Color.Gray
            )
        }

        Spacer(modifier = Modifier.width(16.dp))

        // Precio y Cambio de 24h
        Column(
            horizontalAlignment = Alignment.End
        ) {
            Text(
                text = formatCurrency(coin.currentPrice,currency),
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.SemiBold
            )
            Text(
                text = formatPercentage(coin.priceChangePercentage24h),
                style = MaterialTheme.typography.bodyMedium,
                color = if ((coin.priceChangePercentage24h ?: 0.0) >= 0) Color(0xFF4CAF50) else Color.Red
            )
        }

        // Icono de Favorito
        IconButton(onClick = { onFavoriteClick(coin.id, coin.isFavorite) }) {
            Icon(
                imageVector = if (coin.isFavorite) Icons.Filled.Star else Icons.Outlined.StarOutline,
                contentDescription = "Marcar como favorito",
                tint = if (coin.isFavorite) Color.Yellow else Color.Gray,
                modifier = Modifier.size(28.dp)
            )
        }
    }
}

fun formatCurrency(price: Double?, currency: String): String {
    if (price == null) return "--.--"

    // Bloque para determinar el Local correcto
    val locale = when (currency.lowercase()) {
        "mxn" -> Locale("es", "MX")
        "usd" -> Locale.US
        else -> Locale.US // Por defecto USD
    }

    val format = NumberFormat.getCurrencyInstance(locale)
    return format.format(price)
}

// Formatea un precio como moneda (USD)
fun formatPercentage(percentage: Double?): String {
    if (percentage == null) return "---"
    return String.format(Locale.US, "%.2f%%", percentage)
}

@Preview(showBackground = true)
@Composable
fun CoinRowPreview() {
    val sampleCoin = CoinEntity(
        id = "bitcoin",
        symbol = "btc",
        name = "Bitcoin",
        image = "",
        currentPrice = 68543.0,
        marketCapRank = 1,
        priceChangePercentage24h = -1.56,
        isFavorite = true
    )
    MaterialTheme {
        CoinRow(coin = sampleCoin, currency = "usd", onFavoriteClick = { _, _ -> })
    }
}

@Preview(showBackground = true)
@Composable
fun CoinRowNegativeChangePreview() {
    val sampleCoin = CoinEntity(
        id = "ethereum",
        symbol = "eth",
        name = "Ethereum",
        image = "",
        currentPrice = 3450.0,
        marketCapRank = 2,
        priceChangePercentage24h = 2.1,
        isFavorite = false
    )
    MaterialTheme {
        CoinRow(coin = sampleCoin, currency = "mxn", onFavoriteClick = { _, _ -> })
    }
}