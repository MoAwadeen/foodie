package iti.project.foodie.ui.recipe

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.airbnb.lottie.LottieAnimationView
import iti.project.foodie.R
import iti.project.foodie.data.repository.AuthRepository
import iti.project.foodie.data.repository.RecipesRepository
import iti.project.foodie.data.source.local.Recipe
import iti.project.foodie.data.source.local.RecipeDb
import iti.project.foodie.data.source.local.RoomDb
import iti.project.foodie.ui.adapters.FavoriteAdapter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class FavoriteFragment : Fragment(), FavoriteAdapter.OnItemClickListener {

    private lateinit var favoriteRecyclerView: RecyclerView
    private lateinit var favoriteAdapter: FavoriteAdapter
    private lateinit var recipesRepository: RecipesRepository
    private lateinit var authRepository: AuthRepository
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var email: String
    private var currentUserId: Int? = null
    private lateinit var loadingAnimation: LottieAnimationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        sharedPreferences = requireActivity().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
        email = sharedPreferences.getString("email", null) ?: ""

        val recipeDb = RecipeDb.getDatabase(requireContext())
        val recipeDao = recipeDb.RecipeDao()
        recipesRepository = RecipesRepository(recipeDao)

        val userDb = RoomDb.getDataBase(requireContext())
        val userDao = userDb.UserDao()
        authRepository = AuthRepository(userDao)

        // Retrieve the current user ID from your repository or shared preferences
        CoroutineScope(Dispatchers.IO).launch {
            currentUserId = userDb.UserDao().getCurrentUserId(email)
        }

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

        // Initialize the Lottie animation view
        loadingAnimation = view.findViewById(R.id.animationView)

        // Start the animation and fetch data
        startLoadingAnimation()

        fetchFavoriteMeals()

        return view
    }

    private fun fetchFavoriteMeals() {
        // Check if currentUserId is available
        currentUserId?.let { userId ->
            CoroutineScope(Dispatchers.IO).launch {
                val favoriteMeals = recipesRepository.getAllFavoriteMeals(userId) // Pass user ID
                withContext(Dispatchers.Main) {
                    favoriteAdapter.updateData(favoriteMeals)
                    delay(2000)
                    stopLoadingAnimation() // Stop animation once data is loaded
                }
            }
        }
    }

    private fun startLoadingAnimation() {
        loadingAnimation.visibility = View.VISIBLE
        loadingAnimation.playAnimation()
    }

    private fun stopLoadingAnimation() {
        loadingAnimation.visibility = View.GONE
        loadingAnimation.cancelAnimation()
    }

    override fun onItemClick(meal: Recipe) {
        val bundle = Bundle().apply {
            putString("mealId", meal.mealId)
        }
        findNavController().navigate(R.id.recipeDetailFragment, bundle)
    }
}

