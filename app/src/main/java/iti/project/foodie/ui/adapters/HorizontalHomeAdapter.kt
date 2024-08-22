package iti.project.foodie.ui.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.ai.client.generativeai.common.RequestOptions
import iti.project.foodie.R
import iti.project.foodie.data.source.remote.model.Category

class HorizontalHomeAdapter(
    private val context: Context,
    private val categoryList: MutableList<Category>,
    private val listener: OnItemClickListener // Pass the listener
) : RecyclerView.Adapter<HorizontalHomeAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view: View = LayoutInflater.from(context).inflate(R.layout.horizontal_item_view, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val category = categoryList[position]

        holder.categoryName.text = category.strCategory

        Glide.with(context)
            .load(category.strCategoryThumb)
            .into(holder.categoryView)

        holder.itemView.setOnClickListener {
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
}
