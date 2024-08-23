package iti.project.foodie.data.source.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Favorite Meals")
data class Recipe(
    @PrimaryKey val mealId: String
)