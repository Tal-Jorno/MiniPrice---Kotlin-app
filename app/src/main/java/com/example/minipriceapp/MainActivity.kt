package com.example.minipriceapp

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)

        val prefs = getSharedPreferences("MiniPricePrefs", MODE_PRIVATE)
        val userName = prefs.getString("loggedInUser", "guests") ?: "guests"

        val welcomeText = findViewById<TextView>(R.id.main_LBL_welcome)
        welcomeText.text = "Welcome, $userName"

        findViewById<Button>(R.id.main_BTN_catalog).setOnClickListener {
            startActivity(Intent(this, CatalogActivity::class.java))
        }

        findViewById<Button>(R.id.main_BTN_cart).setOnClickListener {
            startActivity(Intent(this, CartActivity::class.java))
        }

        findViewById<Button>(R.id.main_BTN_orders).setOnClickListener {
            startActivity(Intent(this, OrdersHistoryActivity::class.java))
        }

        findViewById<Button>(R.id.main_BTN_request_product).setOnClickListener {
            startActivity(Intent(this, RequestProductActivity::class.java))
        }

        findViewById<Button>(R.id.main_BTN_logout).setOnClickListener {
            prefs.edit().remove("loggedInUser").apply()
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }
    }
}
