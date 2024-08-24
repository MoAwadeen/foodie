package iti.project.foodie.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import iti.project.foodie.R
import iti.project.foodie.data.source.remote.model.Meal
import android.widget.TextView
import android.widget.ImageView
import com.bumptech.glide.Glide
class SearchAdapter(private val onItemClickListener: (Meal) -> Unit) :
    ListAdapter<Meal, SearchAdapter.SearchViewHolder>(SearchDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_search_result, parent, false)
        return SearchViewHolder(view, onItemClickListener)
    }

    override fun onBindViewHolder(holder: SearchViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class SearchViewHolder(
        itemView: View,
        private val onItemClickListener: (Meal) -> Unit
    ) : RecyclerView.ViewHolder(itemView) {
        private val mealName: TextView = itemView.findViewById(R.id.meal_name)
        private val mealImage: ImageView = itemView.findViewById(R.id.meal_image)
        private val mealCategory: TextView = itemView.findViewById(R.id.category_name)
        private val mealArea: TextView = itemView.findViewById(R.id.area_name)

        fun bind(meal: Meal) {
            mealName.text = meal.strMeal
            mealCategory.text = meal.strCategory
            mealArea.text = meal.strArea
            Glide.with(itemView.context)
                .load(meal.strMealThumb)
                .placeholder(R.drawable.placeholder) // Optional placeholder
                .error(R.drawable.placeholder) // Optional error image
                .into(mealImage)

            // Set the click listener
            itemView.setOnClickListener {
                onItemClickListener(meal)
            }
        }
    }

    class SearchDiffCallback : DiffUtil.ItemCallback<Meal>() {
        override fun areItemsTheSame(oldItem: Meal, newItem: Meal): Boolean {
            return oldItem.idMeal == newItem.idMeal
        }

        override fun areContentsTheSame(oldItem: Meal, newItem: Meal): Boolean {
            return oldItem == newItem
        }
    }
}
