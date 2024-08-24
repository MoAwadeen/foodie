package iti.project.foodie.ui.recipe

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import iti.project.foodie.R
import iti.project.foodie.data.repository.RecipesRepository
import iti.project.foodie.data.source.local.Recipe
import iti.project.foodie.data.source.local.RecipeDb
import iti.project.foodie.ui.adapters.FavoriteAdapter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class FavoriteFragment : Fragment(), FavoriteAdapter.OnItemClickListener {

    private lateinit var favoriteRecyclerView: RecyclerView
    private lateinit var favoriteAdapter: FavoriteAdapter
    private lateinit var recipesRepository: RecipesRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val database = RecipeDb.getDatabase(requireContext())
        val recipeDao = database.RecipeDoa()
        recipesRepository = RecipesRepository(recipeDao)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_favorite, container, false)

        favoriteRecyclerView = view.findViewById(R.id.favoriteView)
        favoriteRecyclerView.layoutManager = LinearLayoutManager(context)

        favoriteAdapter = FavoriteAdapter(emptyList(), this)
        favoriteRecyclerView.adapter = favoriteAdapter

        fetchFavoriteMeals()

        return view
    }

    private fun fetchFavoriteMeals() {
        CoroutineScope(Dispatchers.IO).launch {
            val favoriteMeals = recipesRepository.getAllFavoriteMeals()
            withContext(Dispatchers.Main) {
                favoriteAdapter.updateData(favoriteMeals)
            }
        }
    }

    override fun onItemClick(meal: Recipe) {
        val bundle = Bundle().apply {
            putString("mealId", meal.mealId) // Use mealId from Recipe
        }
        findNavController().navigate(R.id.recipeDetailFragment, bundle)
    }
}
