package iti.project.foodie.ui.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import iti.project.foodie.R
import iti.project.foodie.data.source.remote.model.Category

class HorizontalHomeAdapter(
    private val context: Context,
    private val categoryList: MutableList<Category>
) : RecyclerView.Adapter<HorizontalHomeAdapter.ViewHolder>() {

    private var onItemClickListener: OnItemClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view: View = LayoutInflater.from(context).inflate(R.layout.horizontal_item_view, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val category = categoryList[position]

        holder.categoryName.text = category.strCategory ?: "Unknown Category"

        Glide.with(context)
            .load(category.strCategoryThumb)
            .apply(RequestOptions()
                .placeholder(R.drawable.placeholder)
                .error(R.drawable.placeholder)
            )
            .into(holder.categoryView)

        holder.itemView.setOnClickListener {
            onItemClickListener?.onClick(holder.categoryView, category.strCategoryThumb)
        }
    }


    override fun getItemCount(): Int {
        return categoryList.size
    }

    class ViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView) {
        var categoryView : ImageView = itemView.findViewById(R.id.categoryView)
        var categoryName : TextView = itemView.findViewById(R.id.CategoryName)
    }

    fun setOnItemClickListener(listener: OnItemClickListener?) {
        this.onItemClickListener = listener
    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateData(newCategories: List<Category>) {
        categoryList.clear()
        categoryList.addAll(newCategories)
        notifyDataSetChanged()
    }

    interface OnItemClickListener {
        fun onClick(imageView: ImageView?, path: String?)
    }
}
