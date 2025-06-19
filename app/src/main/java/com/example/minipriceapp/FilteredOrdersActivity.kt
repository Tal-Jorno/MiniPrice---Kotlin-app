package com.example.minipriceapp

import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore
import java.text.SimpleDateFormat
import java.util.Locale


class FilteredOrdersActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: OrdersAdapter
    private val filteredOrders = mutableListOf<Order>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_filtered_orders)

        recyclerView = findViewById(R.id.recyclerViewFilteredOrders)
        recyclerView.layoutManager = LinearLayoutManager(this)

        val prefs = getSharedPreferences("MiniPricePrefs", MODE_PRIVATE)
        val loggedInUser = prefs.getString("loggedInUser", "Guest") ?: "Guest"
        Log.d("DEBUG_USER", "loggedInUser = $loggedInUser")

        adapter = OrdersAdapter(filteredOrders, { index ->
            val intent = android.content.Intent(this, OrderDetailsActivity::class.java)
            intent.putExtra("order", filteredOrders[index])
            startActivity(intent)
        }, loggedInUser)

        recyclerView.adapter = adapter

        val status = intent.getStringExtra("status")
        if (status == null) {
            Toast.makeText(this, "No status specified", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        findViewById<TextView>(R.id.titleTextView).text = "Orders: $status"
        loadOrdersByStatus(status)

        findViewById<Button>(R.id.buttonBack).setOnClickListener {
            finish()
        }
    }

    private fun loadOrdersByStatus(status: String) {
        val formatter = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
        val db = FirebaseFirestore.getInstance()
        val ordersCollection = db.collection("orders")

        val prefs = getSharedPreferences("MiniPricePrefs", MODE_PRIVATE)
        val loggedInUser = prefs.getString("loggedInUser", "Guest") ?: "Guest"

        val query = if (loggedInUser == ADMIN) {
            ordersCollection.whereEqualTo("status", status)
        } else {
            ordersCollection.whereEqualTo("status", status)
                .whereEqualTo("userId", loggedInUser)
        }

        query.get()
            .addOnSuccessListener { result ->
                filteredOrders.clear()
                for (document in result.documents) {
                    val order = document.toObject(Order::class.java)
                    if (order != null) {
                        filteredOrders.add(order)
                    }
                }
                filteredOrders.sortByDescending { formatter.parse(it.date) }
                adapter.updateOrders(filteredOrders)
            }
            .addOnFailureListener { e ->
                Toast.makeText(this, "Failed to load orders: ${e.message}", Toast.LENGTH_LONG).show()
            }
    }
}
