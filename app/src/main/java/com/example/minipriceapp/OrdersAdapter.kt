package com.example.minipriceapp

import android.app.AlertDialog
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore

class OrdersAdapter(
    private var orders: List<Order>,
    private val onItemClick: (Int) -> Unit,
    private val loggedInUser: String
) : RecyclerView.Adapter<OrdersAdapter.OrderViewHolder>() {

    class OrderViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val dateText: TextView = itemView.findViewById(R.id.orderDate)
        val totalText: TextView = itemView.findViewById(R.id.orderTotal)
        val statusText: TextView = itemView.findViewById(R.id.orderStatus)
        val changeStatusButton: Button? = itemView.findViewById(R.id.buttonChangeStatus)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_order, parent, false)
        return OrderViewHolder(view)
    }

    override fun onBindViewHolder(holder: OrderViewHolder, position: Int) {
        val order = orders[position]

        val orderNumber = order.orderNumber?.takeIf { it.isNotBlank() }
        val displayDate = if (orderNumber != null) {
            "Order #$orderNumber\n${order.date}"
        } else {
            order.date
        }

        holder.dateText.text = displayDate
        holder.totalText.text = "₪%.2f".format(order.totalPrice)
        holder.statusText.text = "Status: ${order.status}"

        holder.itemView.setOnClickListener {
            onItemClick(position)
        }

        if (loggedInUser == ADMIN) {
            holder.changeStatusButton?.visibility = View.VISIBLE
            holder.changeStatusButton?.setOnClickListener {
                val context = holder.itemView.context
                val options = arrayOf("In Progress", "Out for Delivery", "Delivered", "Canceled")
                AlertDialog.Builder(context)
                    .setTitle("Select New Status")
                    .setItems(options) { _, which ->
                        val newStatus = options[which]
                        val orderId = order.orderNumber ?: return@setItems

                        FirebaseFirestore.getInstance()
                            .collection("orders")
                            .document(orderId)
                            .update("status", newStatus)
                            .addOnSuccessListener {
                                order.status = newStatus
                                notifyItemChanged(position)
                                Toast.makeText(context, "Status updated to $newStatus", Toast.LENGTH_SHORT).show()
                            }
                            .addOnFailureListener { e ->
                                Toast.makeText(context, "Failed: ${e.message}", Toast.LENGTH_SHORT).show()
                            }
                    }
                    .show()
            }
        } else {
            holder.changeStatusButton?.visibility = View.GONE
        }
    }

    override fun getItemCount(): Int = orders.size

    fun updateOrders(newOrders: List<Order>) {
        orders = newOrders
        notifyDataSetChanged()
    }
}
