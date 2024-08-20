package iti.project.foodie.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import iti.project.foodie.R
import iti.project.foodie.data.source.remote.model.Meal
import iti.project.foodie.ui.recipe.HomeFragment

class VerticalHomeAdapter(private var recipeList: List<Meal>,
                          private val listener: OnItemClickListener) :
    RecyclerView.Adapter<VerticalHomeAdapter.RecipeViewHolder>() {

    interface OnItemClickListener {
        fun onItemClick(recipe: Meal)
        fun observeRandomMeal()
    }

    // Define a view holder for the recipe items
    class RecipeViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val recipeImage: ImageView = itemView.findViewById(R.id.recipeImage)
        val recipeTitle: TextView = itemView.findViewById(R.id.recipeTitle)
        val recipeIngredients: TextView = itemView.findViewById(R.id.recipeIngredients)
        val recipeCountry: TextView = itemView.findViewById(R.id.recipeCountry)
        val countryIcon: ImageView = itemView.findViewById(R.id.countryIcon)
        val favIcon: ImageView = itemView.findViewById(R.id.favIcon)
        val recipeCategory: TextView = itemView.findViewById(R.id.recipeCategory)
        val categoryIcon: ImageView = itemView.findViewById(R.id.categoryIcon)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecipeViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.vertical_item_view, parent, false)
        return RecipeViewHolder(view)
    }

    override fun onBindViewHolder(holder: RecipeViewHolder, position: Int) {
        val recipe = recipeList[position]

        // Load image using Glide
        Glide.with(holder.itemView.context).load(recipe.strMealThumb).into(holder.recipeImage)

        holder.recipeTitle.text = recipe.strMeal
        holder.recipeIngredients.text = ("${recipe.strIngredient1} , ${recipe.strIngredient2} , ...")
        holder.recipeCountry.text = recipe.strArea
        holder.recipeCategory.text = recipe.strCategory

        // Set favorite icon
        //holder.favIcon.setImageResource(if (Meal.isFavorite) R.drawable.ic_sec_fav else R.drawable.ic_sec_fav)

        // Handle item click if needed
        holder.itemView.setOnClickListener {
            // Implement item click listener if needed
            recipe.let { listener.onItemClick(it) }
        }
    }

    override fun getItemCount(): Int {
        return recipeList.size
    }

    fun updateData(newRecipeList: List<Meal>) {
        recipeList = newRecipeList
        notifyDataSetChanged()
    }

}
