package iti.project.foodie.ui.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import iti.project.foodie.R
import iti.project.foodie.data.source.local.Recipe

class FavoriteAdapter(
    private var recipeList: List<Recipe>,
    private val listener: OnItemClickListener
) : RecyclerView.Adapter<FavoriteAdapter.RecipeViewHolder>() {

    interface OnItemClickListener {
        fun onItemClick(meal: Recipe)
    }

    class RecipeViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val recipeImage: ImageView = itemView.findViewById(R.id.recipeImage)
        val recipeTitle: TextView = itemView.findViewById(R.id.recipeTitle)
        val recipeCountry: TextView = itemView.findViewById(R.id.recipeCountry)
        val recipeCategory: TextView = itemView.findViewById(R.id.recipeCategory)
        val recipeIngredient: TextView = itemView.findViewById(R.id.recipeIngredients)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecipeViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.vertical_item_view, parent, false)
        return RecipeViewHolder(view)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: RecipeViewHolder, position: Int) {
        val recipe = recipeList[position]

        Glide.with(holder.itemView.context).load(recipe.strMealThumb).into(holder.recipeImage)

        holder.recipeTitle.text = recipe.strMeal
        holder.recipeCountry.text = recipe.strArea ?: ""
        holder.recipeCategory.text = recipe.strCategory
        holder.recipeIngredient.text = "${recipe.strIngredient1}, ${recipe.strIngredient2}, ..."

        holder.itemView.setOnClickListener {
            listener.onItemClick(recipe)
        }
    }

    override fun getItemCount(): Int {
        return recipeList.size
    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateData(newRecipeList: List<Recipe>) {
        recipeList = newRecipeList
        notifyDataSetChanged()
    }
}
