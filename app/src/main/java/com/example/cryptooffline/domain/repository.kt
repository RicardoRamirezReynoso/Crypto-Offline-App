package com.example.cryptooffline.domain

import com.example.cryptooffline.data.local.CoinDao
import com.example.cryptooffline.data.local.CoinEntity
import com.example.cryptooffline.data.local.UserPreferencesManager
import com.example.cryptooffline.data.local.toEntity
import com.example.cryptooffline.data.remote.CoinGeckoApiService
import com.example.cryptooffline.data.remote.CoinMarket
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import javax.inject.Inject
import kotlin.collections.map


class CoinRepository @Inject constructor(
    private val coinDao: CoinDao,
    private val apiService: CoinGeckoApiService,
    private val prefsManager: UserPreferencesManager
) {

    fun getAllCoins(): Flow<List<CoinEntity>> {
        return coinDao.getAllCoins()
    }

    // Funcion de actualizacion para la pantalla principal
    suspend fun refreshMarketList() {
        try {
            val currentCurrency = prefsManager.vsCurrencyFlow.first()
            val coinsFromApi = apiService.getCoinMarkets(
                vsCurrency = currentCurrency,
                perPage = 20,
                ids = null // Pide la lista general
            )
            saveRefreshedCoins(coinsFromApi)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    // Funcion de actualizacion para el Worker (solo favoritos)
    suspend fun refreshFavoriteCoins() {
        try {
            val favoriteCoinIds = coinDao.getFavoriteCoins().first().map { it.id }.toSet()
            if (favoriteCoinIds.isEmpty()) return

            val idsString = favoriteCoinIds.joinToString(",")
            val currentCurrency = prefsManager.vsCurrencyFlow.first()
            val coinsFromApi = apiService.getCoinMarkets(
                vsCurrency = currentCurrency,
                ids = idsString, // Pide solo los favoritos
                perPage = favoriteCoinIds.size,
            )
            saveRefreshedCoins(coinsFromApi)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    // Lógica de guardado de monedas
    private suspend fun saveRefreshedCoins(coinsFromApi: List<CoinMarket>) {
        val favoriteCoinIds = coinDao.getFavoriteCoins().first().map { it.id }.toSet()
        val newCoinEntities = coinsFromApi.map { coinMarket ->
            val entity = coinMarket.toEntity()
            if (favoriteCoinIds.contains(entity.id)) {
                entity.copy(isFavorite = true)
            } else {
                entity
            }
        }
        coinDao.upsertAll(newCoinEntities)
    }



    // Función para cambiar el estado de favorito de una moneda
    suspend fun updateFavoriteStatus(coinId: String, isFavorite: Boolean) {
        coinDao.updateFavoriteStatus(coinId, isFavorite)
    }

    fun getCurrencyFlow(): Flow<String> = prefsManager.vsCurrencyFlow

    // Permite al ViewModel cambiar la moneda
    suspend fun setCurrency(currency: String) {
        prefsManager.setVsCurrency(currency)
    }
}