package com.example.cryptooffline

import android.app.Application
import androidx.hilt.work.HiltWorkerFactory
import androidx.work.Configuration
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.example.cryptooffline.workers.PriceUpdateWorker
import dagger.hilt.android.HiltAndroidApp
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@HiltAndroidApp
class CryptoOfflineApp : Application() {

    @Inject
    lateinit var workerFactory: HiltWorkerFactory

    override fun onCreate() {
        super.onCreate()

        // Inicializa WorkManager explícitamente con la configuración de Hilt
        // Resuelve el problema de "NoSuchMethodException"
        WorkManager.initialize(
            this,
            Configuration.Builder().setWorkerFactory(workerFactory).build()
        )

        setupRecurringWork()
    }

    // Creacion de solicitud de trabajo periódico
    private fun setupRecurringWork() {
        val repeatingRequest = PeriodicWorkRequestBuilder<PriceUpdateWorker>(
            20, // Intervalo de repetición
            TimeUnit.MINUTES
        ).build()

        //  Se encola el trabajo periódico usando un nombre único, evitando duplicados
        WorkManager.getInstance(applicationContext).enqueueUniquePeriodicWork(
            "price-update-work",
            ExistingPeriodicWorkPolicy.KEEP,
            repeatingRequest
        )
    }
}