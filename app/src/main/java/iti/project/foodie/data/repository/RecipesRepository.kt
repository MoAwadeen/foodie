package iti.project.foodie.data.repository

import iti.project.foodie.data.source.local.Recipe
import iti.project.foodie.data.source.local.RecipeDoa
import iti.project.foodie.data.source.remote.model.Meal

class RecipesRepository(private val recipeDao: RecipeDoa) {

    suspend fun addMealToFavorites(meal: Meal) {
        val recipe = Recipe(
            mealId = meal.idMeal,
            strMeal = meal.strMeal,
            strMealThumb = meal.strMealThumb,
            strArea = meal.strArea,
            strCategory = meal.strCategory,
            strIngredient1 = meal.strIngredient1,
            strIngredient2 = meal.strIngredient2,
        )
        recipeDao.insert(recipe)
    }

    suspend fun removeMealFromFavorites(mealId: String) {
        recipeDao.delete(mealId)
    }

    suspend fun isMealFavorite(mealId: String): Boolean {
        return recipeDao.getFavoriteMeal(mealId) != null
    }

    suspend fun getAllFavoriteMeals(): List<Recipe> {
        return recipeDao.getAllMeals()
    }
}
