package iti.project.foodie.data.source.remote.network

import iti.project.foodie.data.source.remote.model.MealList
import retrofit2.Call
import retrofit2.http.GET

public interface ApiServices {
    @GET("random.php")
    fun getRandomMeal(): Call<MealList>
}
