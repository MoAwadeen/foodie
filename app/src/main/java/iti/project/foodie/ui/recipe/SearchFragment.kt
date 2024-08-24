package iti.project.foodie.ui.recipe

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.DividerItemDecoration
import iti.project.foodie.R
import iti.project.foodie.data.source.remote.model.Meal
import iti.project.foodie.data.source.remote.model.MealList
import iti.project.foodie.data.source.remote.network.RetrofitModule
import iti.project.foodie.ui.adapters.SearchAdapter
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import androidx.appcompat.widget.SearchView
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
class SearchFragment : Fragment() {

    private lateinit var searchAdapter: SearchAdapter
    private lateinit var searchResultsRecyclerView: RecyclerView
    private lateinit var searchView: SearchView
    private lateinit var navController: NavController

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_search, container, false)

        searchView = view.findViewById(R.id.search_view)
        searchResultsRecyclerView = view.findViewById(R.id.search_results_recycler_view)
        searchResultsRecyclerView.layoutManager = LinearLayoutManager(context)

        searchAdapter = SearchAdapter { meal ->
            onItemClick(meal)
        }

        searchResultsRecyclerView.adapter = searchAdapter

        val dividerItemDecoration = DividerItemDecoration(
            searchResultsRecyclerView.context,
            (searchResultsRecyclerView.layoutManager as LinearLayoutManager).orientation
        )
        searchResultsRecyclerView.addItemDecoration(dividerItemDecoration)

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                query?.let {
                    fetchMealsByName(it)
                }
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return false
            }
        })

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = NavHostFragment.findNavController(this)
    }

    private fun fetchMealsByName(query: String) {
        RetrofitModule.api.getMealByName(query).enqueue(object : Callback<MealList> {
            override fun onResponse(call: Call<MealList>, response: Response<MealList>) {
                if (response.isSuccessful) {
                    val mealList = response.body()?.meals
                    if (mealList != null) {
                        searchAdapter.submitList(mealList)
                    } else {
                        Log.e("SearchFragment", "Meal list is null")
                    }
                } else {
                    Log.e("SearchFragment", "Response not successful: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<MealList>, t: Throwable) {
                Log.e("SearchFragment", "API call failed", t)
            }
        })
    }

    private fun onItemClick(meal: Meal) {
        val bundle = Bundle().apply {
            putString("mealId", meal.idMeal)
        }
        navController.navigate(R.id.recipeDetailFragment, bundle)
    }
}
