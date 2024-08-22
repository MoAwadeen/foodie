package iti.project.foodie.ui.recipe

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import iti.project.foodie.R

class RecipeDetailFragment : Fragment() {

    private lateinit var recipeImage: ImageView
    private lateinit var recipeTitle: TextView
    private lateinit var recipeCountry: TextView
    private lateinit var recipeCategory: TextView
    private lateinit var ingredientsList: TextView
    private lateinit var recipeVideo: WebView
    private lateinit var exitBtn: ImageView
    private lateinit var favouriteBtn: ImageView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_recipe_detail, container, false)
    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recipeImage = view.findViewById(R.id.recipeImage)
        recipeTitle = view.findViewById(R.id.recipeTitle)
        recipeCountry = view.findViewById(R.id.countryText)
        recipeCategory = view.findViewById(R.id.categoryText)
        ingredientsList = view.findViewById(R.id.ingredientsList)
        recipeVideo = view.findViewById(R.id.recipeVideo)
        exitBtn = view.findViewById(R.id.exitBtn)
        favouriteBtn = view.findViewById(R.id.favouriteBtn)

        arguments?.let { bundle ->
            val mealName = bundle.getString("mealName")
            val mealImage = bundle.getString("mealImage")
            val mealCountry = bundle.getString("mealCountry")
            val mealCategory = bundle.getString("mealCategory")
            val ingredient1 = bundle.getString("ingredient1")
            val ingredient2 = bundle.getString("ingredient2")

            recipeTitle.text = mealName
            recipeCountry.text = mealCountry
            recipeCategory.text = mealCategory
            ingredientsList.text = "$ingredient1 , $ingredient2" // Add more ingredients as needed

            // Load the meal image using Glide
            mealImage?.let {
                Glide.with(this).load(it).into(recipeImage)
            }
        }

        exitBtn.setOnClickListener {
            findNavController().navigate(R.id.homeFragment)
        }

        favouriteBtn.setOnClickListener {
            findNavController().navigate(R.id.favoriteFragment)
        }
    }
}
