package iti.project.foodie.ui.recipe

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.chip.Chip
import iti.project.foodie.R
import iti.project.foodie.data.repository.AuthRepository
import iti.project.foodie.data.source.local.HistoryDB
import iti.project.foodie.data.source.local.RoomDb
import iti.project.foodie.data.source.local.SearchHistory
import iti.project.foodie.data.source.local.SearchHistoryDao
import iti.project.foodie.data.source.remote.model.Meal
import iti.project.foodie.data.source.remote.model.MealList
import iti.project.foodie.data.source.remote.network.RetrofitModule
import iti.project.foodie.ui.adapters.SearchAdapter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SearchFragment : Fragment() {

    private lateinit var searchAdapter: SearchAdapter
    private lateinit var searchResultsRecyclerView: RecyclerView
    private lateinit var searchView: SearchView
    private lateinit var navController: NavController
    private lateinit var database: HistoryDB
    private lateinit var searchHistoryDao: SearchHistoryDao
    private lateinit var authRepository: AuthRepository
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var email: String
    private var currentUserId: Int? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        sharedPreferences = requireActivity().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
        email = sharedPreferences.getString("email", null) ?: ""

        val userDb = RoomDb.getDataBase(requireContext())
        val userDao = userDb.UserDao()
        authRepository = AuthRepository(userDao)

        CoroutineScope(Dispatchers.IO).launch {
            currentUserId = userDb.UserDao().getCurrentUserId(email)
            withContext(Dispatchers.Main) {
                loadSearchHistory()
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_search, container, false)

        // Initialize views
        searchView = view.findViewById(R.id.search_view)
        searchResultsRecyclerView = view.findViewById(R.id.search_results_recycler_view)
        searchResultsRecyclerView.layoutManager = LinearLayoutManager(context)

        searchAdapter = SearchAdapter { meal ->
            onItemClick(meal)
        }
        searchResultsRecyclerView.adapter = searchAdapter

        // Initialize the database and DAO
        database = HistoryDB.getDatabase(requireContext())
        searchHistoryDao = database.searchHistoryDao()

        // Divider decoration
        val dividerItemDecoration = DividerItemDecoration(
            searchResultsRecyclerView.context,
            (searchResultsRecyclerView.layoutManager as LinearLayoutManager).orientation
        )
        searchResultsRecyclerView.addItemDecoration(dividerItemDecoration)

        // Set up search view listener
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                query?.let {
                    saveSearchQueryToHistory(it)
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

    private fun saveSearchQueryToHistory(query: String) {
        lifecycleScope.launch {
            val exists = searchHistoryDao.doesQueryExist(query)
            if (exists == 0) {  // Only save if the query doesn't already exist
                currentUserId?.let {
                    val searchHistory = SearchHistory(query = query, userId = it)
                    searchHistoryDao.insertSearchHistory(searchHistory)
                    loadSearchHistory() // Reload chips after saving a new query
                }
            }
        }
    }

    private fun loadSearchHistory() {
        currentUserId?.let { userId ->
            lifecycleScope.launch {
                val searchHistoryList = searchHistoryDao.getAllSearchHistory(userId)
                displaySearchHistoryButtons(searchHistoryList)
            }
        }
    }

    private fun displaySearchHistoryButtons(searchHistoryList: List<SearchHistory>) {
        val container = view?.findViewById<LinearLayout>(R.id.search_history_container)
        container?.removeAllViews() // Clear previous chips

        searchHistoryList.forEach { history ->
            val chip = Chip(requireContext()).apply {
                text = history.query
                setChipBackgroundColorResource(R.color.purple) // Background color
                setTextColor(resources.getColorStateList(R.color.orange)) // Text color
                setRippleColorResource(R.color.light_orange) // Ripple effect color
                isCloseIconVisible = false // Hide close icon
                setOnClickListener {
                    performSearch(history.query)
                }
            }
            container?.addView(chip)
        }
    }

    private fun performSearch(query: String) {
        searchView.setQuery(query, true) // Set the query in the SearchView and submit
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
        findNavController().navigate(R.id.recipeDetailFragment, bundle)
    }
}
