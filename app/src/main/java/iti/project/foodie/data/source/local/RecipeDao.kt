package iti.project.foodie.data.source.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface RecipeDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(meal: Recipe)

    @Query("DELETE FROM `Favorite Meals` WHERE mealId = :mealId AND userId = :userId")
    suspend fun delete(mealId: String, userId: Int)

    @Query("SELECT * FROM `Favorite Meals` WHERE mealId = :mealId AND userId = :userId LIMIT 1")
    suspend fun getFavoriteMeal(mealId: String, userId: Int): Recipe?

    @Query("SELECT * FROM `Favorite Meals` WHERE userId = :userId")
    suspend fun getAllMeals(userId: Int): List<Recipe>
}

