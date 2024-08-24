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
import iti.project.foodie.data.source.remote.model.Meal

class VerticalHomeAdapter(private var mealList: List<Meal> ,
                          private val listener: OnItemClickListener) :
    RecyclerView.Adapter<VerticalHomeAdapter.RecipeViewHolder>() {

    interface OnItemClickListener {
        fun onItemClick(meal : Meal)
        fun observeRandomMeal()
    }

    // Define a view holder for the recipe items
    class RecipeViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView) {
        val recipeImage: ImageView = itemView.findViewById(R.id.recipeImage)
        val recipeTitle: TextView = itemView.findViewById(R.id.recipeTitle)
        val recipeCountry: TextView = itemView.findViewById(R.id.recipeCountry)
        val recipeCategory: TextView = itemView.findViewById(R.id.recipeCategory)
        val recipeIngredient : TextView = itemView.findViewById(R.id.recipeIngredients)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecipeViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.vertical_item_view, parent, false)
        return RecipeViewHolder(view)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: RecipeViewHolder, position: Int) {
        val meal = mealList[position]

        // Load image using Glide
        Glide.with(holder.itemView.context).load(meal.strMealThumb).into(holder.recipeImage)

        holder.recipeTitle.text = meal.strMeal
        meal.strArea?.let {holder.recipeCountry.text = it}
        holder.recipeCountry.text = meal.strArea?:""
        holder.recipeCategory.text = meal.strCategory
        holder.recipeIngredient.text = ("${meal.strIngredient1} , ${meal.strIngredient2} , ....." )

        // Handle item click
        holder.itemView.setOnClickListener {
            listener.onItemClick(meal)
        }
    }

    override fun getItemCount(): Int {
        return mealList.size
    }

    fun updateData(newMealList: List<Meal>) {
        mealList = newMealList
        notifyDataSetChanged()
    }
}