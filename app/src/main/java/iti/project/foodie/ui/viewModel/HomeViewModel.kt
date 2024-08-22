package iti.project.foodie.ui.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import iti.project.foodie.data.source.remote.model.CategoriesList
import iti.project.foodie.data.source.remote.model.Category
import iti.project.foodie.data.source.remote.model.Meal
import iti.project.foodie.data.source.remote.model.MealList
import iti.project.foodie.data.source.remote.network.RetrofitModule
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeViewModel : ViewModel() {

    private val randomMealsLiveData = MutableLiveData<List<Meal>>() // LiveData for random meals
    private val categoriesLiveData = MutableLiveData<List<Category>>()
    private val mealsByCategoryLiveData = MutableLiveData<List<Meal>>()
    private val retrofitService = RetrofitModule.api

    init {
        fetchRandomMeals() // Fetch random meals on initialization
    }

    private fun fetchRandomMeals() {
        val randomMeals = mutableListOf<Meal>()
        val fetchCount = 5 // Number of random meals to fetch

        for (i in 0 until fetchCount) {
            retrofitService.getRandomMeal().enqueue(object : Callback<MealList> {
                override fun onResponse(call: Call<MealList>, response: Response<MealList>) {
                    if (response.isSuccessful) {
                        response.body()?.meals?.let { meals ->
                            randomMeals.addAll(meals)
                            randomMealsLiveData.value = randomMeals // Update LiveData
                        }
                    }
                }

                override fun onFailure(call: Call<MealList>, t: Throwable) {
                    Log.d("HomeViewModel", "Error fetching random meals: ${t.message}")
                }
            })
        }
    }


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

    fun getMealsByCategory(category : String) {
        retrofitService.getMealsByCategory(category).enqueue(object : Callback<MealList> {
            override fun onResponse(call: Call<MealList> , response: Response<MealList>) {
                if (response.isSuccessful) {
                    response.body()?.meals?.let {
                        mealsByCategoryLiveData.value = it
                    }
                }
            }

            override fun onFailure(call: Call<MealList>, t: Throwable) {
                Log.d("HomeViewModel", "Error fetching meals: ${t.message}")
            }
        })
    }

    fun observeMealsByCategoryLiveData(): LiveData<List<Meal>> {
        return mealsByCategoryLiveData
    }

    fun observeRandomMealLiveData(): LiveData<List<Meal>> {
        return randomMealsLiveData // Expose random meals LiveData
    }
}
