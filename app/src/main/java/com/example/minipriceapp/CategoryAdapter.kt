package com.example.minipriceapp

import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class CategoryAdapter(private val categories: List<CategoryItem>) :
    RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder>() {

    class CategoryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageView: ImageView = itemView.findViewById(R.id.categoryImage)
        val textView: TextView = itemView.findViewById(R.id.categoryTitle)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_category, parent, false)
        return CategoryViewHolder(view)
    }

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
        val category = categories[position]
        holder.imageView.setImageResource(category.imageResId)
        holder.textView.text = category.title

        holder.itemView.setOnClickListener {
            val context = holder.itemView.context
            val intent = Intent(context, ProductsActivity::class.java)
            intent.putExtra("category_name", category.title)

            val originalIntent = (context as? Activity)?.intent
            originalIntent?.getStringExtra("editing_order_number")?.let {

                intent.putExtra("editing_order_number", it)
            }
            originalIntent?.getStringExtra("editing_user_id")?.let {
                intent.putExtra("editing_user_id", it)
            }
            originalIntent?.getStringExtra("editing_order_date")?.let {
                intent.putExtra("editing_order_date", it)
            }
            originalIntent?.getSerializableExtra("restored_address")?.let {
                intent.putExtra("restored_address", it as HashMap<String, String>)
                intent.putExtra("is_restoring", true)
            }

            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int = categories.size
}
