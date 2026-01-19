package com.example.cryptooffline.di

import android.content.Context
import androidx.room.Room
import com.example.cryptooffline.data.local.AppDatabase
import com.example.cryptooffline.data.local.CoinDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "crypto_database"
        ).build()
    }

    @Provides
    @Singleton
    fun provideCoinDao(database: AppDatabase): CoinDao {
        return database.coinDao()
    }
}