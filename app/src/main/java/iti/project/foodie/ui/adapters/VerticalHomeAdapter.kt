package iti.project.foodie.ui.adapters

import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import iti.project.foodie.R
import iti.project.foodie.data.source.remote.model.Meal

class VerticalHomeAdapter(
    private var mealList: List<Meal>,
    private val listener: OnItemClickListener
) : RecyclerView.Adapter<VerticalHomeAdapter.RecipeViewHolder>() {

    interface OnItemClickListener {
        fun onItemClick(meal: Meal)
        fun observeRandomMeal()
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

    override fun getItemCount(): Int {
        return mealList.size
    }


    fun String?.orDefault(default: String = ""): String = this?.takeIf { it != "null" } ?: default

    fun String?.orDefaultImage(defaultImageRes: Int): String =
        this?.takeIf { it != "null" } ?: defaultImageRes.toString()

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: RecipeViewHolder, position: Int) {
        val meal = mealList[position]


        Glide.with(holder.itemView.context)
            .load(meal.strMealThumb.orDefaultImage(R.drawable.placeholder))
            .into(holder.recipeImage)

        holder.recipeTitle.text = meal.strMeal.orDefault("")
        holder.recipeCountry.text = meal.strArea.orDefault("")
        holder.recipeCategory.text = meal.strCategory.orDefault("")

        val ingredients = listOf(
            meal.strIngredient1,
            meal.strIngredient2,
            meal.strIngredient3

        ).filter { !it.isNullOrEmpty() && it != "null" }.joinToString(" , ")

        holder.recipeIngredient.text = if (ingredients.isNotBlank()) ingredients else ""

        holder.itemView.setOnClickListener {
            listener.onItemClick(meal)
        }
    }


    @SuppressLint("NotifyDataSetChanged")
    fun updateData(newMealList: List<Meal>) {
        mealList = newMealList
        notifyDataSetChanged()
    }

}
