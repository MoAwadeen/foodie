package iti.project.foodie.ui.viewModel

import android.annotation.SuppressLint
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.bumptech.glide.Glide
import iti.project.foodie.R
import iti.project.foodie.data.source.remote.model.CategoriesList
import iti.project.foodie.data.source.remote.model.Category
import iti.project.foodie.data.source.remote.model.Meal
import iti.project.foodie.data.source.remote.model.MealList
import iti.project.foodie.data.source.remote.network.RetrofitModule
import iti.project.foodie.ui.adapters.HorizontalHomeAdapter
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeViewModel : ViewModel() {

    private val randomMealLiveData = MutableLiveData<Meal>()
    private val horizontalMealsLiveData = MutableLiveData<List<Meal>>()

    private val categoriesLiveData = MutableLiveData<List<Category>>()
    val retrofitService = RetrofitModule.api

    fun getCategories() {
        retrofitService.getCategories().enqueue(object : Callback<CategoriesList> {
            override fun onResponse(call: Call<CategoriesList>, response: Response<CategoriesList>) {
                if (response.isSuccessful) {
                    response.body()?.categories?.let {
                        categoriesLiveData.value = it
                    }
                }
            }

            override fun onFailure(call: Call<CategoriesList>, t: Throwable) {
                Log.d("HomeViewModel", "Error fetching categories: ${t.message}")
            }
        })
    }

    fun observeCategoriesLiveData(): LiveData<List<Category>> {
        return categoriesLiveData
    }

    fun getRandomMeal() {
        RetrofitModule.api.getRandomMeal().enqueue(object : Callback<MealList> {
            override fun onResponse(call: Call<MealList>, response: Response<MealList>) {
                if (response.body() != null) {
                    val randomMeal = response.body()!!.meals[0]
                    randomMealLiveData.value = randomMeal
                } else {
                    Log.d("Test", "Response body is null")
                }
            }

            override fun onFailure(call: Call<MealList>, t: Throwable) {
                Log.d("Test", t.message.toString())
            }
        })
    }

    fun observeRandomMealLiveData(): LiveData<Meal> {
        return randomMealLiveData
    }

    fun getHorizontalMeals() {
        RetrofitModule.api.getRandomMeal().enqueue(object : Callback<MealList> {
            override fun onResponse(call: Call<MealList>, response: Response<MealList>) {
                response.body()?.meals?.let {
                    horizontalMealsLiveData.value = it
                }
            }

            override fun onFailure(call: Call<MealList>, t: Throwable) {
                Log.d("Test", t.message.toString())
            }
        })
    }

    fun observeHorizontalMealsLiveData(): LiveData<List<Meal>> {
        return horizontalMealsLiveData
    }
}