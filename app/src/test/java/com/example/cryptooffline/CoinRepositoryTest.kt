package com.example.cryptooffline

import com.example.cryptooffline.data.local.CoinDao
import com.example.cryptooffline.data.local.CoinEntity
import com.example.cryptooffline.data.local.UserPreferencesManager
import com.example.cryptooffline.data.remote.CoinGeckoApiService
import com.example.cryptooffline.data.remote.CoinMarket
import com.example.cryptooffline.domain.CoinRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class CoinRepositoryTest {

    private lateinit var repository: CoinRepository
    private lateinit var mockApiService: CoinGeckoApiService
    private lateinit var mockCoinDao: CoinDao
    private lateinit var mockPrefsManager: UserPreferencesManager

    @Before
    fun setUp() {
        // Instancias falsas de las dependencias
        mockApiService = mockk()
        mockCoinDao =
            mockk(relaxed = true) // 'relaxed' permite que no se defina el comportamiento de todas sus funciones, devuelve valores por defecto automáticamente
        mockPrefsManager = mockk()

        repository = CoinRepository(
            coinDao = mockCoinDao,
            apiService = mockApiService,
            prefsManager = mockPrefsManager
        )
    }

    @Test
    fun refreshMarketList_success_fetchesFromApiAndSavesToDao() =
        runTest {

            // Datos que simulan la respuesta de la API
            val fakeApiCoins = listOf(
                CoinMarket("bitcoin", "btc", "Bitcoin", "", 50000.0, 1, 1.0, null),
                CoinMarket("ethereum", "eth", "Ethereum", "", 3000.0, 2, -2.0, null)
            )

            // Datos que simulan lo que hay en la base de datos (un favorito)
            val fakeFavoriteCoins = listOf(
                CoinEntity(
                    "ethereum",
                    "eth",
                    "Ethereum",
                    "",
                    2900.0,
                    2,
                    0.5,
                    null,
                    isFavorite = true
                )
            )

            // Cuando se llame a getCoinMarkets, devuelve los datos falsos de la API
            coEvery {
                mockApiService.getCoinMarkets(
                    any(),
                    any(),
                    any(),
                    any(),
                    any(),
                    any()
                )
            } returns fakeApiCoins
            // Cuando se pregunte por la moneda, devuelve "usd"
            coEvery { mockPrefsManager.vsCurrencyFlow } returns flowOf("usd")
            // Cuando se pregunte por los favoritos, devuelve lista falsa de favoritos
            coEvery { mockCoinDao.getFavoriteCoins() } returns flowOf(fakeFavoriteCoins)

            // Ejecutar la acción a probar
            repository.refreshMarketList()

            // Verifica que 'upsertAll' fue llamado en el DAO
            coVerify {
                mockCoinDao.upsertAll(
                    // Verifica que la lista que se le pasó tiene los datos correctos
                    match { coinList ->
                        val bitcoin = coinList.find { it.id == "bitcoin" }
                        val ethereum = coinList.find { it.id == "ethereum" }

                        // El Bitcoin de la API no era favorito, así que debe ser guardado como isFavorite = false
                        val isBitcoinCorrect =
                            bitcoin?.isFavorite == false && bitcoin.currentPrice == 50000.0

                        // El Ethereum de la API no era favorito, pero en la BD sí lo era
                        // El repositorio debe preservar ese estado
                        val isEthereumCorrect =
                            ethereum?.isFavorite == true && ethereum.currentPrice == 3000.0

                        isBitcoinCorrect && isEthereumCorrect
                    }
                )
            }
        }
}