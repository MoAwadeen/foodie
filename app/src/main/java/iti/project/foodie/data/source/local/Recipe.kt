package iti.project.foodie.data.source.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Favorite Meals")
data class Recipe(
    @PrimaryKey val mealId: String,
    val strMeal: String?,
    val strMealThumb: String?,
    val strCategory: String?,
    val strArea: String?,
    val strIngredient1: String?,
    val strIngredient2: String?
)
