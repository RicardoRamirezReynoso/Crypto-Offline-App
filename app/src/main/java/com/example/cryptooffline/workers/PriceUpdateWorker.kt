package com.example.cryptooffline.workers

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.cryptooffline.domain.CoinRepository
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject


// Actualización de precios en segundo plano
@HiltWorker
class PriceUpdateWorker @AssistedInject constructor(
    @Assisted appContext: Context,
    @Assisted workerParams: WorkerParameters,
    private val coinRepository: CoinRepository
) : CoroutineWorker(appContext, workerParams) {

    override suspend fun doWork(): Result {
        return try {
            coinRepository.refreshFavoriteCoins()

            Result.success()
        } catch (e: Exception) {
            e.printStackTrace()
            Result.retry()
        }
    }
}