package iti.project.foodie.data.source.local
import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [SearchHistory::class], version = 1)
abstract class HistoryDB : RoomDatabase() {
    abstract fun searchHistoryDao(): SearchHistoryDao

    companion object {
        @Volatile
        private var INSTANCE: HistoryDB? = null

        fun getDatabase(context: Context): HistoryDB {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    HistoryDB::class.java,
                    "history_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}
