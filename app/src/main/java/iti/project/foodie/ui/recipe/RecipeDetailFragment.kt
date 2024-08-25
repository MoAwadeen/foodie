package iti.project.foodie.ui.recipe

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import android.os.Build
import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import iti.project.foodie.R
import iti.project.foodie.data.repository.AuthRepository
import iti.project.foodie.data.repository.RecipesRepository
import iti.project.foodie.data.source.local.RecipeDb
import iti.project.foodie.data.source.local.RoomDb
import iti.project.foodie.data.source.remote.model.Meal
import iti.project.foodie.data.source.remote.model.MealList
import iti.project.foodie.data.source.remote.network.RetrofitModule
import iti.project.foodie.databinding.FragmentRecipeDetailBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RecipeDetailFragment : Fragment() {

    private var _binding: FragmentRecipeDetailBinding? = null
    private val binding get() = _binding!!

    private lateinit var mealId: String
    private var isFavorite: Boolean = false
    private lateinit var repository: RecipesRepository
    private var currentMeal: Meal? = null

    private lateinit var authRepository: AuthRepository
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var email: String
    private var currentUserId: Int? = null // Variable to hold the current user ID

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        activity?.window?.let { window ->

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.hide(WindowInsets.Type.statusBars())
        } else {
            @Suppress("DEPRECATION")
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }
        }


        sharedPreferences = requireActivity().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
        email = sharedPreferences.getString("email", null) ?: ""

        arguments?.let {
            mealId = it.getString("mealId", "")
        }

        val database = RecipeDb.getDatabase(requireContext())
        val recipeDao = database.RecipeDao()
        repository = RecipesRepository(recipeDao)

        val userDb = RoomDb.getDataBase(requireContext())
        val userDao = userDb.UserDao()
        authRepository = AuthRepository(userDao)

        CoroutineScope(Dispatchers.IO).launch {
            currentUserId = userDb.UserDao().getCurrentUserId(email)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRecipeDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    @SuppressLint("SetTextI18n", "SetJavaScriptEnabled")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        CoroutineScope(Dispatchers.IO).launch {
            isFavorite = repository.isMealFavorite(mealId , currentUserId!!)
            CoroutineScope(Dispatchers.Main).launch {
                updateFavoriteButton()
            }
        }

        val webSettings = binding.recipeVideo.settings
        webSettings.javaScriptEnabled = true
        webSettings.loadWithOverviewMode = true
        webSettings.useWideViewPort = true

        binding.exitBtn.setOnClickListener {
            findNavController().navigate(R.id.homeFragment)
        }

        binding.favouriteBtn.setOnClickListener {
            if (isFavorite) {
                showRemoveFromFavoritesDialog()
            } else {
                currentMeal?.let { meal ->
                    isFavorite = true
                    updateFavoriteButton()
                    saveFavoriteMeal(meal)
                    Toast.makeText(requireContext(), R.string.add_to_favourites, Toast.LENGTH_LONG).show()
                }
            }
        }

        fetchMealDetails(mealId)
    }

    private fun saveFavoriteMeal(meal: Meal) {
        CoroutineScope(Dispatchers.IO).launch {
            repository.addMealToFavorites(meal , currentUserId!!)
        }
    }

    private fun removeFavoriteMeal(mealId: String) {
        CoroutineScope(Dispatchers.IO).launch {
            repository.removeMealFromFavorites(mealId , currentUserId!!)
        }
    }

    private fun updateFavoriteButton() {
        if (isFavorite) {
            binding.favouriteBtn.setImageResource(R.drawable.ic_filled_heart) // Change to selected icon
        } else {
            binding.favouriteBtn.setImageResource(R.drawable.ic_third_favourite) // Change back to default icon
        }
    }

    private fun showRemoveFromFavoritesDialog() {
        val builder = android.app.AlertDialog.Builder(requireContext())
        builder.setTitle(R.string.remove_from_favourites)
        builder.setMessage(R.string.remove_from_favourites_confirmation)

        builder.setNegativeButton(R.string.no) { dialog, _ ->
            dialog.dismiss()
        }

        builder.setPositiveButton(R.string.yes) { dialog, _ ->
            isFavorite = false
            updateFavoriteButton()
            removeFavoriteMeal(mealId)
            Toast.makeText(requireContext(), R.string.removed_from_favourites, Toast.LENGTH_LONG).show()
            dialog.dismiss()
        }

        val alertDialog = builder.create()
        alertDialog.show()

        val positiveButton = alertDialog.getButton(AlertDialog.BUTTON_POSITIVE)
        val negativeButton = alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE)

        positiveButton.setTextColor(ContextCompat.getColor(requireContext(), R.color.light_orange))
        negativeButton.setTextColor(ContextCompat.getColor(requireContext(), R.color.light_purple))
    }

    private fun fetchMealDetails(mealId: String) {
        val call = RetrofitModule.api.getMealDetails(mealId)
        call.enqueue(object : Callback<MealList> {
            override fun onResponse(call: Call<MealList>, response: Response<MealList>) {
                if (response.isSuccessful) {
                    val meal = response.body()?.meals?.firstOrNull()
                    meal?.let {
                        currentMeal = it
                        updateUI(it)
                    }
                }
            }

            override fun onFailure(call: Call<MealList>, t: Throwable) {
                // Handle the error
            }
        })
    }

    private fun updateUI(meal: Meal) {
        binding.recipeTitle.text = meal.strMeal
        binding.recipeInstructions.text = meal.strInstructions
        binding.countryText.text = meal.strArea
        binding.categoryText.text = meal.strCategory

        Glide.with(this).load(meal.strMealThumb).into(binding.recipeImage)

        val videoUrl = meal.strYoutube.replace("watch?v=", "embed/")
        videoUrl.let {
            binding.recipeVideo.loadUrl(it)
        }

        binding.recipeInstructions.setOnClickListener {
            if (binding.recipeInstructions.maxLines == 4) {
                binding.recipeInstructions.maxLines = Int.MAX_VALUE
                binding.recipeInstructions.ellipsize = null
            } else {
                binding.recipeInstructions.maxLines = 4
                binding.recipeInstructions.ellipsize = TextUtils.TruncateAt.END
            }
        }

        val ingredients = mutableListOf<String>()
        val allIngredients = listOf(
            meal.strIngredient1,
            meal.strIngredient2,
            meal.strIngredient3,
            meal.strIngredient4,
            meal.strIngredient5,
            meal.strIngredient6,
            meal.strIngredient7,
            meal.strIngredient8,
            meal.strIngredient9,
            meal.strIngredient10,
            meal.strIngredient11,
            meal.strIngredient12,
            meal.strIngredient13,
            meal.strIngredient14,
            meal.strIngredient15
        )
        ingredients.addAll(allIngredients.filter { it.isNotBlank() })
        binding.ingredientsList.text = ingredients.joinToString(separator = "\n")

        val measures = mutableListOf<String>()
        val allMeasures = listOf(
            meal.strMeasure1,
            meal.strMeasure2,
            meal.strMeasure3,
            meal.strMeasure4,
            meal.strMeasure5,
            meal.strMeasure6,
            meal.strMeasure7,
            meal.strMeasure8,
            meal.strMeasure9,
            meal.strMeasure10,
            meal.strMeasure11,
            meal.strMeasure12,
            meal.strMeasure13,
            meal.strMeasure14,
            meal.strMeasure15
        )
        measures.addAll(allMeasures.filter { it.isNotBlank() })
        binding.measuresList.text = measures.joinToString(separator = "\n")
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null

        activity?.window?.let { window ->
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                window.insetsController?.show(WindowInsets.Type.statusBars())
            } else {
                @Suppress("DEPRECATION")
                window.clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)
            }
        }
    }
}
