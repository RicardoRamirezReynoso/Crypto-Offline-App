package com.example.cryptooffline

import android.app.Application
import android.content.Context
import androidx.test.runner.AndroidJUnitRunner
import dagger.hilt.android.testing.HiltTestApplication

// Un Test Runner personalizado para pruebas de instrumentación con Hilt
class HiltTestRunner : AndroidJUnitRunner() {

    override fun newApplication(
        cl: ClassLoader?,
        className: String?,
        context: Context?
    ): Application {
        // Hilt usa HiltTestApplication en lugar de CryptoOfflineApp durante las pruebas.
        return super.newApplication(cl, HiltTestApplication::class.java.name, context)
    }
}