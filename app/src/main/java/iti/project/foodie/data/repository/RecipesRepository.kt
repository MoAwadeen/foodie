package iti.project.foodie.data.repository

import iti.project.foodie.data.source.local.Recipe
import iti.project.foodie.data.source.local.RecipeDao
import iti.project.foodie.data.source.remote.model.Meal

class RecipesRepository(private val recipeDao: RecipeDao) {

    suspend fun addMealToFavorites(meal: Meal, userId: Int) {
        val recipe = Recipe(
            mealId = meal.idMeal,
            strMeal = meal.strMeal,
            strMealThumb = meal.strMealThumb,
            strArea = meal.strArea,
            strCategory = meal.strCategory,
            strIngredient1 = meal.strIngredient1,
            strIngredient2 = meal.strIngredient2,
            userId = userId // Pass the user ID
        )
        recipeDao.insert(recipe)
    }

    suspend fun removeMealFromFavorites(mealId: String, userId: Int) {
        recipeDao.delete(mealId, userId) // Pass the user ID
    }

    suspend fun isMealFavorite(mealId: String, userId: Int): Boolean {
        return recipeDao.getFavoriteMeal(mealId, userId) != null // Pass the user ID
    }

    suspend fun getAllFavoriteMeals(userId: Int): List<Recipe> {
        return recipeDao.getAllMeals(userId) // Pass the user ID
    }
}
