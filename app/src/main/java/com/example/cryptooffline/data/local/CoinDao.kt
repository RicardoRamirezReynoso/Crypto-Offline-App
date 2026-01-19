package com.example.cryptooffline.data.local

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow

@Dao
interface CoinDao {

    // Si una moneda con la misma Primary Key ya existe, la actualiza, si no existe, la inserta
    @Upsert
    suspend fun upsertAll(coins: List<CoinEntity>)

    @Query("SELECT * FROM coins ORDER BY marketCapRank ASC")
    fun getAllCoins(): Flow<List<CoinEntity>>

    @Query("SELECT * FROM coins WHERE isFavorite = 1 ORDER BY marketCapRank ASC")
    fun getFavoriteCoins(): Flow<List<CoinEntity>>

    // Un función para cambiar el estado de 'isFavorite' de una moneda
    @Query("UPDATE coins SET isFavorite = :isFavorite WHERE id = :coinId")
    suspend fun updateFavoriteStatus(coinId: String, isFavorite: Boolean)

}


