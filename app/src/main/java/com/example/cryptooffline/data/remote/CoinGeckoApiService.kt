package com.example.cryptooffline.data.remote

import retrofit2.http.GET
import retrofit2.http.Query

interface CoinGeckoApiService {

    @GET("coins/markets")
    suspend fun getCoinMarkets(
        @Query("vs_currency") vsCurrency: String,
        @Query("ids") ids: String? = null, // Filtrar por favoritos
        @Query("order") order: String = "market_cap_desc",
        @Query("per_page") perPage: Int,
        @Query("page") page: Int = 1,
        @Query("sparkline") sparkline: Boolean = true
    ): List<CoinMarket>

}