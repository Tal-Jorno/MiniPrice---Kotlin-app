package com.example.minipriceapp

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class OrdersHistoryActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_orders_history)

        val prefs = getSharedPreferences("MiniPricePrefs", MODE_PRIVATE)
        val loggedInUser = prefs.getString("loggedInUser", "Guest") ?: "Guest"

        findViewById<Button>(R.id.buttonInProgress).setOnClickListener {
            openFilteredOrders("In Progress")
        }

        findViewById<Button>(R.id.buttonOutForDelivery).setOnClickListener {
            openFilteredOrders("Out for Delivery")
        }

        findViewById<Button>(R.id.buttonDelivered).setOnClickListener {
            openFilteredOrders("Delivered")
        }

        findViewById<Button>(R.id.buttonViewCanceled).setOnClickListener {
            openFilteredOrders("Canceled")
        }

        findViewById<Button>(R.id.buttonBackToMenu).setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(intent)
            finish()
        }
    }

    private fun openFilteredOrders(status: String) {
        val intent = Intent(this, FilteredOrdersActivity::class.java)
        intent.putExtra("status", status)
        startActivity(intent)
    }
}
