package com.example.minipriceapp

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore

class CanceledOrdersActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: OrdersAdapter
    private var canceledOrdersList: List<Order> = emptyList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_canceled_orders)

        recyclerView = findViewById(R.id.recyclerViewOrders)
        recyclerView.layoutManager = LinearLayoutManager(this)

        val prefs = getSharedPreferences("MiniPricePrefs", MODE_PRIVATE)
        val username = prefs.getString("loggedInUser", "Guest") ?: "Guest"

        adapter = OrdersAdapter(canceledOrdersList, { index ->
            val selectedOrder = canceledOrdersList[index]
            val intent = Intent(this, OrderDetailsActivity::class.java)
            intent.putExtra("order", selectedOrder)
            startActivity(intent)
        }, username)

        recyclerView.adapter = adapter

        findViewById<Button>(R.id.buttonBackToMenu).setOnClickListener {
            finish()
        }

        loadCanceledOrders(username)
    }

    private fun loadCanceledOrders(userId: String) {
        FirebaseFirestore.getInstance()
            .collection("orders")
            .whereEqualTo("userId", userId)
            .whereEqualTo("status", "Canceled")
            .get()
            .addOnSuccessListener { result ->
                canceledOrdersList = result.mapNotNull { it.toObject(Order::class.java) }
                    .sortedByDescending { it.timestamp }
                OrderManager.clearOrders()
                OrderManager.addOrders(canceledOrdersList)
                adapter.updateOrders(canceledOrdersList)
            }
            .addOnFailureListener { e ->
                Toast.makeText(this, "Failed to load canceled orders: ${e.message}", Toast.LENGTH_LONG).show()
            }
    }
}
