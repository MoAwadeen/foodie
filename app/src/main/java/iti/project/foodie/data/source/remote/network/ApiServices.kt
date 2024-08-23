package iti.project.foodie.data.source.remote.network

import iti.project.foodie.data.source.remote.model.CategoriesList
import iti.project.foodie.data.source.remote.model.MealList
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

public interface ApiServices {
    @GET("random.php")
    fun getRandomMeal() : Call<MealList>

    @GET("categories.php")
    fun getCategories() : Call<CategoriesList>

    @GET("filter.php") // Replace with your actual endpoint
    fun getMealsByCategory(@Query("c") category: String): Call<MealList>

    @GET("search.php") // Replace with your actual endpoint
    fun getMealByName(@Query("s") name: String): Call<MealList>

    @GET("lookup.php")
    fun getMealDetails(@Query("i") mealId: String): Call<MealList>
}
