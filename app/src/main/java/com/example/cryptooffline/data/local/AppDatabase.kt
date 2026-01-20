package com.example.cryptooffline.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(
    entities = [CoinEntity::class],
    version = 1
)

@TypeConverters(SparklineConverter::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun coinDao(): CoinDao
}