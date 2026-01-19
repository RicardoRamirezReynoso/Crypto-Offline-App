package com.example.cryptooffline.data.local

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "user_preferences")

@Singleton
class UserPreferencesManager @Inject constructor(@ApplicationContext private val context: Context) {

    // Clave para guardar la moneda seleccionada
    private object PreferencesKeys {
        val VS_CURRENCY = stringPreferencesKey("vs_currency")
    }

    // Se lee el valor de la clave, si no existe, devuelve "usd" como valor por defecto
    val vsCurrencyFlow: Flow<String> = context.dataStore.data
        .map { preferences ->
            preferences[PreferencesKeys.VS_CURRENCY] ?: "usd"
        }

    // Función suspendida para actualizar la preferencia de la moneda
    suspend fun setVsCurrency(currency: String) {
        context.dataStore.edit { preferences ->
            preferences[PreferencesKeys.VS_CURRENCY] = currency
        }
    }
}