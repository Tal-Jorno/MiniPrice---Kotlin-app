package com.example.minipriceapp

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.widget.TextView
import android.widget.Toast
import com.google.gson.Gson

class CartActivity : AppCompatActivity() {

    private val gson = Gson()
    private lateinit var buttonCheckout: Button
    private lateinit var totalTextView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cart)

        val prefs = getSharedPreferences("MiniPricePrefs", MODE_PRIVATE)
        val editingOrderNumber = prefs.getString("editing_order_number", null)
        val editingOrderDate = prefs.getString("editing_order_date", null)
        val editingUserId = prefs.getString("editing_order_user_id", null)

        val isEditing = !editingOrderNumber.isNullOrEmpty() &&
                !editingOrderDate.isNullOrEmpty() &&
                !editingUserId.isNullOrEmpty()

        val isRestoring = intent.getBooleanExtra("is_restoring", false)
        val isNewOrder = !isEditing && !isRestoring

        val restoredAddress: HashMap<String, String>? = when {
            isRestoring -> {
                val fromIntent = intent.getSerializableExtra("restored_address") as? HashMap<String, String>
                fromIntent?.let {
                    val json = gson.toJson(it)
                    prefs.edit().putString("restored_address_json", json).apply()
                }
                fromIntent
            }
            isEditing -> {
                val storedJson = prefs.getString("restored_address_json", null)
                if (!storedJson.isNullOrEmpty()) {
                    gson.fromJson(storedJson, HashMap::class.java) as HashMap<String, String>
                } else null
            }
            else -> {
                prefs.edit().remove("restored_address_json").apply()
                null
            }
        }

        val recyclerView = findViewById<RecyclerView>(R.id.recyclerViewCart)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = CartAdapter(CartManager.getCart())

        totalTextView = findViewById(R.id.finalTotalText)
        buttonCheckout = findViewById(R.id.buttonCheckout)

        updateCheckoutButtonState()

        findViewById<Button>(R.id.buttonBack).setOnClickListener {
            finish()
        }

        findViewById<Button>(R.id.buttonBackToMenu).setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(intent)
            finish()
        }

        buttonCheckout.setOnClickListener {
            val username = prefs.getString("loggedInUser", null)

            if (username == null) {
                Toast.makeText(this, "User not logged in", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val checkoutIntent = Intent(this, AddressActivity::class.java)

            if (isEditing) {
                checkoutIntent.putExtra("editing_order_number", editingOrderNumber)
                checkoutIntent.putExtra("editing_order_date", editingOrderDate)
                checkoutIntent.putExtra("editing_user_id", editingUserId)
            }

            if (restoredAddress != null) {
                checkoutIntent.putExtra("restored_address", restoredAddress)
                checkoutIntent.putExtra("is_restoring", true)
            }

            startActivity(checkoutIntent)
        }

        val total = CartManager.getCart().sumOf { it.price * it.quantity }
        totalTextView.text = "Total Price: ₪%.2f".format(total)
    }

    private fun updateCheckoutButtonState() {
        val isCartEmpty = CartManager.getCart().isEmpty()
        buttonCheckout.isEnabled = !isCartEmpty
        buttonCheckout.alpha = if (isCartEmpty) 0.5f else 1.0f
    }
}
