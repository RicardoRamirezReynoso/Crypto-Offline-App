package com.example.cryptooffline.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import com.example.cryptooffline.data.remote.CoinMarket
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types

@Entity(tableName = "coins")
@TypeConverters(SparklineConverter::class)
data class CoinEntity(
    @PrimaryKey
    val id: String,
    val symbol: String,
    val name: String,
    val image: String,
    val currentPrice: Double?,
    val marketCapRank: Int?,
    val priceChangePercentage24h: Double?,
    val sparkline: List<Double>?, // campo para la gráfica
    val isFavorite: Boolean = false
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
        priceChangePercentage24h = this.priceChangePercentage24h,
        sparkline = this.sparklineIn7d?.price
    )
}

// Permite que Room convierta List<Double> en texto JSON y viceversa
object SparklineConverter {
    private val moshi = Moshi.Builder().build()
    private val listType = Types.newParameterizedType(List::class.java, Double::class.javaObjectType)
    private val adapter = moshi.adapter<List<Double>>(listType)

    @TypeConverter
    fun fromJson(json: String?): List<Double>? {
        return json?.let { adapter.fromJson(it) }
    }

    @TypeConverter
    fun toJson(data: List<Double>?): String? {
        return data?.let { adapter.toJson(it) }
    }
}


