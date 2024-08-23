package iti.project.foodie.data.repository

import iti.project.foodie.data.source.local.Recipe
import iti.project.foodie.data.source.local.RecipeDoa

class RecipesRepository(private val recipeDao: RecipeDoa) {

    suspend fun addMealToFavorites(mealId: String) {
        val recipe = Recipe(mealId)
        recipeDao.insert(recipe)
    }

    suspend fun removeMealFromFavorites(mealId: String) {
        recipeDao.delete(mealId)
    }

    suspend fun isMealFavorite(mealId: String): Boolean {
        return recipeDao.getFavoriteMeal(mealId) != null
    }
}
