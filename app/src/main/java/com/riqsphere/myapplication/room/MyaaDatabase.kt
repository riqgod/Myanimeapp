package com.riqsphere.myapplication.room

import android.content.Context
import androidx.room.*
import com.google.gson.Gson
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
    }

    class DataConverter {
        private val gson = Gson()

        @TypeConverter
        public fun arrayListFromString(s: String): ArrayList<Int> = gson.fromJson<ArrayList<Int>>(s, java.util.ArrayList::class.java)

        @TypeConverter
        public fun stringFromArrayList(arrayList: ArrayList<Int>): String = gson.toJson(arrayList)
    }
}