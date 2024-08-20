package iti.project.foodie.ui.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import iti.project.foodie.R

class HorizontalHomeAdapter(private val context: Context , private val arrayList: ArrayList<String>) :
    RecyclerView.Adapter<HorizontalHomeAdapter.ViewHolder>() {

    private var onItemClickListener : OnItemClickListener ?= null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view: View = LayoutInflater.from(context).inflate(R.layout.horizontal_item_view, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val imageUrl = arrayList[position]

        if (imageUrl.isNotEmpty()) {
            Glide.with(context)
                .load(imageUrl)
                .apply(RequestOptions()
                    .placeholder(R.drawable.placeholder)
                    .error(R.drawable.placeholder)
                )
                .into(holder.imageView)
        } else {
            holder.imageView.setImageResource(R.drawable.placeholder)
        }

        holder.itemView.setOnClickListener {
            onItemClickListener?.onClick(holder.imageView, imageUrl)
        }
    }

    override fun getItemCount(): Int {
        return arrayList.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var imageView: ImageView = itemView.findViewById(R.id.categoryView)
    }

    fun setOnItemClickListener(listener : OnItemClickListener?) {
        this.onItemClickListener = listener
    }

    interface OnItemClickListener {
        fun onClick(imageView : ImageView?, path : String?)
    }
}