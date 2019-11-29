package com.riqsphere.myapplication.watchlist.persistence

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [watchlistAnime::class], version = 1, exportSchema = false)
abstract class watchlistAnimeDatabase : RoomDatabase() {
    abstract fun watchlistAnimeDao(): watchlistAnimeDao

    companion object {
        @Volatile
        private var INSTANCE: watchlistAnimeDatabase? = null

        fun getDatabase(context: Context): watchlistAnimeDatabase {
            val tempInstance = INSTANCE
            tempInstance?.let { return it }

            synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    watchlistAnimeDatabase::class.java,
                    "watchlist_anime_database"
                ).build()
                INSTANCE = instance
                return instance
            }
        }
    }
}