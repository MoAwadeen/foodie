package iti.project.foodie.ui.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.android.material.card.MaterialCardView
import iti.project.foodie.R
import iti.project.foodie.data.source.remote.model.Category

class HorizontalHomeAdapter(
    private val context: Context,
    val categoryList: MutableList<Category>,
    private val listener: OnItemClickListener,
    private var selectedPosition: Int = RecyclerView.NO_POSITION
) : RecyclerView.Adapter<HorizontalHomeAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view: View = LayoutInflater.from(context).inflate(R.layout.horizontal_item_view, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val category = categoryList[position]
        val card = holder.itemView.findViewById<MaterialCardView>(R.id.card)


        holder.categoryName.text = category.strCategory

        // Update card background based on selection
        if (position == selectedPosition) {
            card.setCardBackgroundColor(ContextCompat.getColor(context, R.color.red))
            holder.categoryName.setTextColor(ContextCompat.getColor(context, R.color.white))
        } else {
            card.setCardBackgroundColor(ContextCompat.getColor(context, R.color.wheat))
            holder.categoryName.setTextColor(ContextCompat.getColor(context, R.color.night))
        }

        Glide.with(context)
            .load(category.strCategoryThumb)
            .into(holder.categoryView)

        holder.itemView.setOnClickListener {
            // Update the selected position and notify the adapter
            val previousPosition = selectedPosition
            selectedPosition = position
            notifyItemChanged(previousPosition)
            notifyItemChanged(selectedPosition)
            listener.onItemClick(category) // Notify the listener of the click
        }
    }

    override fun getItemCount(): Int {
        return categoryList.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var categoryView: ImageView = itemView.findViewById(R.id.categoryView)
        var categoryName: TextView = itemView.findViewById(R.id.categoryName)
    }

    interface OnItemClickListener {
        fun onItemClick(category: Category) // Update to pass the clicked category
    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateData(newCategories: List<Category>) {
        categoryList.clear()
        categoryList.addAll(newCategories)
        notifyDataSetChanged()
    }

    fun setSelectedPosition(position: Int) {
        val previousPosition = selectedPosition
        selectedPosition = position
        notifyItemChanged(previousPosition)
        notifyItemChanged(selectedPosition)
    }


}
