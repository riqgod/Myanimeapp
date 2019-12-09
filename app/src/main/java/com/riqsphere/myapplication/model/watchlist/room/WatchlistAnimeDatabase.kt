package com.riqsphere.myapplication.model.watchlist.room

import android.content.Context
import androidx.room.*
import com.google.gson.Gson

@Database(entities = [WatchlistAnime::class], version = 1, exportSchema = false)
@TypeConverters(DataConverter::class)
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

class DataConverter {
    private val gson = Gson()

    @TypeConverter
    public fun arrayListFromString(s: String): ArrayList<Int> = gson.fromJson<ArrayList<Int>>(s, java.util.ArrayList::class.java)

    @TypeConverter
    public fun stringFromArrayList(arrayList: ArrayList<Int>): String = gson.toJson(arrayList)
}