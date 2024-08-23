package iti.project.foodie.data.source.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface RecipeDoa {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(meal: Recipe)

    @Query("DELETE FROM `Favorite Meals` WHERE mealId = :mealId")
    suspend fun delete(mealId: String)

    @Query("SELECT * FROM `Favorite Meals` WHERE mealId = :mealId LIMIT 1")
    suspend fun getFavoriteMeal(mealId: String): Recipe?
}