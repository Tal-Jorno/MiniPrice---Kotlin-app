package com.example.minipriceapp

import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import android.app.Activity
import android.os.Handler
import android.os.Looper

class ProductAdapter(private val products: List<ProductItem>) :
    RecyclerView.Adapter<ProductAdapter.ProductViewHolder>() {

    class ProductViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageView: ImageView = itemView.findViewById(R.id.productImage)
        val nameView: TextView = itemView.findViewById(R.id.productName)
        val priceView: TextView = itemView.findViewById(R.id.productPrice)
        val increaseButton: Button = itemView.findViewById(R.id.buttonIncrease)
        val decreaseButton: Button = itemView.findViewById(R.id.buttonDecrease)
        val quantityEditText: EditText = itemView.findViewById(R.id.editTextQuantity)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_product, parent, false)
        return ProductViewHolder(view)
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        val product = products[position]

        // המרה של imageName ל־resource ID
        val context = holder.itemView.context
        val imageResId = context.resources.getIdentifier(product.imageName, "drawable", context.packageName)
        holder.imageView.setImageResource(imageResId)

        holder.nameView.text = product.name
        holder.priceView.text = "%.2f ₪".format(product.price)

        val savedQuantity = CartManager.getQuantityFor(product.name)
        product.quantity = savedQuantity
        holder.quantityEditText.setText(savedQuantity.toString())

        holder.increaseButton.setOnClickListener {
            val qty = holder.quantityEditText.text.toString().toIntOrNull() ?: 0
            val newQty = qty + 1
            product.quantity = newQty
            holder.quantityEditText.setText(newQty.toString())
            CartManager.updateCart(product)
        }

        holder.decreaseButton.setOnClickListener {
            val qty = holder.quantityEditText.text.toString().toIntOrNull() ?: 0
            val newQty = if (qty > 0) qty - 1 else 0
            product.quantity = newQty
            holder.quantityEditText.setText(newQty.toString())
            CartManager.updateCart(product)
        }

        holder.quantityEditText.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                val text = s.toString()
                val quantity = text.toIntOrNull()
                if (quantity != null && quantity >= 0) {
                    product.quantity = quantity
                    CartManager.updateCart(product)
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })
    }

    override fun getItemCount(): Int = products.size
}
