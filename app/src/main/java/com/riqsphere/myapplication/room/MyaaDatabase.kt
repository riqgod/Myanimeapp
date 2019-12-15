package com.riqsphere.myapplication.room

import android.content.Context
import androidx.room.*
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.riqsphere.myapplication.model.recommendations.Recommendation
import com.riqsphere.myapplication.model.watchlist.WatchlistAnime

@Database(entities = [WatchlistAnime::class, Recommendation::class], version = 1, exportSchema = false)
@TypeConverters(MyaaDatabase.DataConverter::class)
abstract class MyaaDatabase : RoomDatabase() {
    abstract fun watchlistAnimeDao(): WatchlistAnimeDao
    abstract fun recommendationDao(): RecommendationDao

    companion object {
        @Volatile
        private var INSTANCE: MyaaDatabase? = null

        fun getDatabase(context: Context): MyaaDatabase {
            val tempInstance =
                INSTANCE
            tempInstance?.let { return it }

            synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    MyaaDatabase::class.java,
                    "watchlist_anime_database"
                ).build()
                INSTANCE = instance
                return instance
            }
        }

        val dataConverter = DataConverter()
    }

    class DataConverter {
        private val gson = Gson()

        @TypeConverter
        public fun arrayListFromString(s: String): IntArray {
            val type = object: TypeToken<IntArray>(){}.type!!
            return gson.fromJson<IntArray>(s, type)
        }

        @TypeConverter
        public fun stringFromArrayList(arr: IntArray): String {
            return when (arr.size) {
                1 -> gson.toJson(arr)
                else -> gson.toJson(arr)
            }
        }
    }
}