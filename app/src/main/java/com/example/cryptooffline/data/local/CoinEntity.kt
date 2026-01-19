package com.example.cryptooffline.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.cryptooffline.data.remote.CoinMarket

@Entity(tableName = "coins")
data class CoinEntity(
    @PrimaryKey
    val id: String,
    val symbol: String,
    val name: String,
    val image: String,
    val currentPrice: Double?,
    val marketCapRank: Int?,
    val priceChangePercentage24h: Double?,
    val isFavorite: Boolean = false // Campo para controlar los favoritos
)

// Función de extensión para convertir el objeto de la API (CoinMarket) a la entidad de BD (CoinEntity)
fun CoinMarket.toEntity(): CoinEntity {
    return CoinEntity(
        id = this.id,
        symbol = this.symbol,
        name = this.name,
        image = this.image,
        currentPrice = this.currentPrice,
        marketCapRank = this.marketCapRank,
        priceChangePercentage24h = this.priceChangePercentage24h
    )
}


