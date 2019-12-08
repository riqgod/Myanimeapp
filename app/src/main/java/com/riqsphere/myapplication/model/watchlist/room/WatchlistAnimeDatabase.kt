package com.riqsphere.myapplication.model.watchlist.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [WatchlistAnime::class], version = 1, exportSchema = false)
abstract class WatchlistAnimeDatabase : RoomDatabase() {
    abstract fun watchlistAnimeDao(): WatchlistAnimeDao

    companion object {
        @Volatile
        private var INSTANCE: WatchlistAnimeDatabase? = null

        fun getDatabase(context: Context): WatchlistAnimeDatabase {
            val tempInstance = INSTANCE
            tempInstance?.let { return it }

            synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    WatchlistAnimeDatabase::class.java,
                    "watchlist_anime_database"
                ).build()
                INSTANCE = instance
                return instance
            }
        }
    }
}