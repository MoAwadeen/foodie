package iti.project.foodie.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import iti.project.foodie.R
import iti.project.foodie.ui.recipe.HomeFragment

class VerticalHomeAdapter(private val recipeList: List<HomeFragment.Recipe>) :
    RecyclerView.Adapter<VerticalHomeAdapter.RecipeViewHolder>() {

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
        Glide.with(holder.itemView.context).load(recipe.imageUrl).into(holder.recipeImage)

        holder.recipeTitle.text = recipe.title
        holder.recipeIngredients.text = recipe.ingredients
        holder.recipeCountry.text = recipe.country
        holder.recipeCategory.text = recipe.category

        // Set favorite icon
        holder.favIcon.setImageResource(if (recipe.isFavorite) R.drawable.ic_sec_fav else R.drawable.ic_sec_fav)

        // Handle item click if needed
        holder.itemView.setOnClickListener {
            // Implement item click listener if needed
        }
    }

    override fun getItemCount(): Int {
        return recipeList.size
    }
}
