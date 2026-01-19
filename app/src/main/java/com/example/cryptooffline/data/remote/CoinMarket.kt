package com.example.cryptooffline.data.remote

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class CoinMarket(
    val id: String,
    val symbol: String,
    val name: String,
    val image: String,

    @Json(name = "current_price")
    val currentPrice: Double?,

    @Json(name = "market_cap_rank")
    val marketCapRank: Int?,

    @Json(name = "price_change_percentage_24h")
    val priceChangePercentage24h: Double?,

    @Json(name = "sparkline_in_7d")
    val sparklineIn7d: SparklineIn7d?
)

//Contiene la lista de precios históricos de los últimos 7 días
@JsonClass(generateAdapter = true)
data class SparklineIn7d(
    val price: List<Double>
)


