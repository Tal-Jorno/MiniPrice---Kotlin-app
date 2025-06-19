package com.example.minipriceapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView

class CartAdapter(private val cartItems: List<ProductItem>) :
    RecyclerView.Adapter<CartAdapter.CartViewHolder>() {

    class CartViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageView: ImageView = itemView.findViewById(R.id.cartItemImage)
        val nameView: TextView = itemView.findViewById(R.id.cartItemName)
        val quantityView: TextView = itemView.findViewById(R.id.cartItemQuantity)
        val totalView: TextView = itemView.findViewById(R.id.cartItemTotal)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_cart_product, parent, false)
        return CartViewHolder(view)
    }

    override fun onBindViewHolder(holder: CartViewHolder, position: Int) {
        val item = cartItems[position]

        // Load image by resource name
        val resId = holder.itemView.context.resources.getIdentifier(
            item.imageName, "drawable", holder.itemView.context.packageName
        )
        holder.imageView.setImageResource(resId)

        holder.nameView.text = item.name
        holder.quantityView.text = "Quantity: ${item.quantity}"
        holder.totalView.text = "Total: ₪%.2f".format(item.price * item.quantity)
    }

    override fun getItemCount(): Int = cartItems.size
}
