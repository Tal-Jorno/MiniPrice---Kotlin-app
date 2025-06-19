package com.example.minipriceapp

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore

class OrderDetailsActivity : AppCompatActivity() {
    @Suppress("DEPRECATION")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_order_details)

        val order = intent.getParcelableExtra<Order>("order") ?: run {
            finish()
            return
        }

        val titleText = findViewById<TextView>(R.id.orderDetailsTitle)
        titleText.text = "Order Details"

        val orderIdText = findViewById<TextView>(R.id.orderDetailsId)
        orderIdText.text = if (!order.orderNumber.isNullOrEmpty()) {
            "Order #${order.orderNumber}"
        } else {
            "Order #N/A"
        }

        val totalText = findViewById<TextView>(R.id.orderDetailsTotal)
        totalText.text = "Total Price: ₪%.2f".format(order.totalPrice)

        val addressText = findViewById<TextView>(R.id.orderDetailsAddress)
        val addressMap = order.address as? Map<*, *> ?: emptyMap<String, String>()

        val name = addressMap["name"]?.toString().orEmpty()
        val city = addressMap["city"]?.toString().orEmpty()
        val street = addressMap["street"]?.toString().orEmpty()
        val houseNumber = addressMap["houseNumber"]?.toString().orEmpty()
        val floor = addressMap["floor"]?.toString()
        val apartment = addressMap["apartment"]?.toString()
        val phone = addressMap["phone"]?.toString().orEmpty()

        val floorDisplay = if (!floor.isNullOrEmpty()) floor else "-"
        val apartmentDisplay = if (!apartment.isNullOrEmpty()) apartment else "-"

        addressText.text = """
            Address:
            Full Name: $name
            City: $city
            Street: $street
            House Number: $houseNumber
            Floor: $floorDisplay
            Apartment: $apartmentDisplay
            Phone: $phone
        """.trimIndent()

        val recyclerView = findViewById<RecyclerView>(R.id.recyclerViewOrderDetails)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = CartAdapter(order.items)

        findViewById<Button>(R.id.buttonBack).setOnClickListener {
            finish()
        }

        val cancelButton = findViewById<Button>(R.id.buttonCancelOrder)
        if (order.status != "In Progress") {
            cancelButton.isEnabled = false
            cancelButton.text = "Cannot cancel"
        } else {
            cancelButton.setOnClickListener {
                FirebaseFirestore.getInstance()
                    .collection("orders")
                    .whereEqualTo("userId", order.userId)
                    .whereEqualTo("date", order.date)
                    .whereEqualTo("orderNumber", order.orderNumber)
                    .get()
                    .addOnSuccessListener { documents ->
                        for (doc in documents) {
                            doc.reference.update("status", "Canceled")
                                .addOnSuccessListener {
                                    Toast.makeText(this, "Order canceled", Toast.LENGTH_SHORT).show()
                                    setResult(RESULT_OK)
                                    finish()
                                }
                                .addOnFailureListener { e ->
                                    Toast.makeText(this, "Failed: ${e.message}", Toast.LENGTH_LONG).show()
                                }
                        }
                    }
                    .addOnFailureListener {
                        Toast.makeText(this, "Order not found", Toast.LENGTH_SHORT).show()
                    }
            }
        }

        val addressHashMap = order.address?.mapValues { it.value }?.toMap() as? HashMap<String, String>

        val restoreButton = findViewById<Button>(R.id.buttonRestoreCart)
        restoreButton.setOnClickListener {
            CartManager.clearCart()
            order.items.forEach { item -> CartManager.addItem(item) }

            val intent = Intent(this, CartActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
            if (addressHashMap != null) {
                intent.putExtra("restored_address", addressHashMap)
            }

            val prefs = getSharedPreferences("MiniPricePrefs", MODE_PRIVATE)
            prefs.edit()
                .remove("editing_order_number")
                .remove("editing_order_user_id")
                .remove("editing_order_date")
                .apply()

            startActivity(intent)
            finish()
        }

        val editButton = findViewById<Button>(R.id.buttonEditOrder)
        if (order.status != "In Progress") {
            editButton.isEnabled = false
            editButton.text = "Cannot edit"
        } else {
            editButton.setOnClickListener {
                CartManager.clearCart()
                order.items.forEach { item -> CartManager.addItem(item) }

                val prefs = getSharedPreferences("MiniPricePrefs", MODE_PRIVATE)
                val editor = prefs.edit()
                    .putString("editing_order_number", order.orderNumber)
                    .putString("editing_order_user_id", order.userId)
                    .putString("editing_order_date", order.date)

                if (addressHashMap != null) {
                    editor.putString("restored_name", addressHashMap["name"])
                    editor.putString("restored_city", addressHashMap["city"])
                    editor.putString("restored_street", addressHashMap["street"])
                    editor.putString("restored_houseNumber", addressHashMap["houseNumber"])
                    editor.putString("restored_floor", addressHashMap["floor"])
                    editor.putString("restored_apartment", addressHashMap["apartment"])
                    editor.putString("restored_phone", addressHashMap["phone"])
                }

                editor.apply()

                val intent = Intent(this, CartActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
                if (addressHashMap != null) {
                    intent.putExtra("restored_address", addressHashMap)
                }

                startActivity(intent)
                finish()
            }
        }
    }
}
