package iti.project.foodie.data.source.local

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import android.content.Context

@Database(entities = [Recipe::class], version = 1)
abstract class RecipeDb : RoomDatabase() {
    abstract fun RecipeDoa(): RecipeDoa

    companion object {
        @Volatile
        private var INSTANCE: RecipeDb? = null

        fun getDatabase(context: Context): RecipeDb {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    RecipeDb::class.java,
                    "Favorite Meal Database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}
