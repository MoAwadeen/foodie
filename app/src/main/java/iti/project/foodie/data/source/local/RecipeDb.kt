package iti.project.foodie.data.source.local

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import android.content.Context
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

@Database(entities = [Recipe::class], version = 2)
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
                )
                    .addMigrations(MIGRATION_1_2)
                    .build()
                INSTANCE = instance
                instance
            }
        }

        private val MIGRATION_1_2 = object : Migration(1, 2) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL("ALTER TABLE `Favorite Meals` ADD COLUMN strMeal TEXT")
                db.execSQL("ALTER TABLE `Favorite Meals` ADD COLUMN strMealThumb TEXT")
                db.execSQL("ALTER TABLE `Favorite Meals` ADD COLUMN strCategory TEXT")
                db.execSQL("ALTER TABLE `Favorite Meals` ADD COLUMN strArea TEXT")
                db.execSQL("ALTER TABLE `Favorite Meals` ADD COLUMN strIngredient1 TEXT")
                db.execSQL("ALTER TABLE `Favorite Meals` ADD COLUMN strIngredient2 TEXT")
            }
        }
    }
}
