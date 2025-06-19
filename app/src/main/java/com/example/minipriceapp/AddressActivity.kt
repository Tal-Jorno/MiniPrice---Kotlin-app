package com.example.minipriceapp

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FirebaseFirestore
import java.text.SimpleDateFormat
import java.util.*

class AddressActivity : AppCompatActivity() {

    private lateinit var nameEditText: EditText
    private lateinit var cityEditText: EditText
    private lateinit var streetEditText: EditText
    private lateinit var houseNumberEditText: EditText
    private lateinit var floorEditText: EditText
    private lateinit var apartmentEditText: EditText
    private lateinit var phoneEditText: EditText
    private lateinit var confirmButton: Button
    private lateinit var cancelButton: Button

    private fun generateOrderNumber(length: Int = 8): String {
        val chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789"
        return (1..length).map { chars.random() }.joinToString("")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_address)

        nameEditText = findViewById(R.id.editTextName)
        cityEditText = findViewById(R.id.editTextCity)
        streetEditText = findViewById(R.id.editTextStreet)
        houseNumberEditText = findViewById(R.id.editTextHouseNumber)
        floorEditText = findViewById(R.id.editTextFloor)
        apartmentEditText = findViewById(R.id.editTextApartmentNumber)
        phoneEditText = findViewById(R.id.editTextPhoneNumber)
        confirmButton = findViewById(R.id.buttonConfirmAddress)
        cancelButton = findViewById(R.id.buttonCancelAddress)

        val prefs = getSharedPreferences("MiniPricePrefs", MODE_PRIVATE)
        val editingOrderNumber = intent.getStringExtra("editing_order_number")
            ?: prefs.getString("editing_order_number", null)
        val editingOrderDate = intent.getStringExtra("editing_order_date")
            ?: prefs.getString("editing_order_date", null)
        val editingUserId = intent.getStringExtra("editing_user_id")
            ?: prefs.getString("editing_order_user_id", null)

        val isEditing = !editingOrderNumber.isNullOrEmpty() &&
                !editingOrderDate.isNullOrEmpty() &&
                !editingUserId.isNullOrEmpty()
        val isRestoring = intent.getBooleanExtra("is_restoring", false) ||
                intent.getSerializableExtra("restored_address") != null
        val isNewOrder = !isEditing && !isRestoring

        Log.d("DEBUG_MODE", "isEditing=$isEditing, isRestoring=$isRestoring, isNewOrder=$isNewOrder")

        if (isNewOrder) {
            prefs.edit()
                .remove("restored_name")
                .remove("restored_city")
                .remove("restored_street")
                .remove("restored_houseNumber")
                .remove("restored_floor")
                .remove("restored_apartment")
                .remove("restored_phone")
                .apply()
        }

        val restored: HashMap<*, *>? = if (!isNewOrder) {
            intent.getSerializableExtra("restored_address") as? HashMap<*, *> ?: run {
                val savedAddress = hashMapOf<String, String>()
                prefs.getString("restored_name", null)?.let { savedAddress["name"] = it }
                prefs.getString("restored_city", null)?.let { savedAddress["city"] = it }
                prefs.getString("restored_street", null)?.let { savedAddress["street"] = it }
                prefs.getString("restored_houseNumber", null)?.let { savedAddress["houseNumber"] = it }
                prefs.getString("restored_floor", null)?.let { savedAddress["floor"] = it }
                prefs.getString("restored_apartment", null)?.let { savedAddress["apartment"] = it }
                prefs.getString("restored_phone", null)?.let { savedAddress["phone"] = it }
                if (savedAddress.isNotEmpty()) savedAddress else null
            }
        } else {
            null
        }

        if (restored != null) {
            Log.d("DEBUG_FILL", "Restoring address: $restored")
            nameEditText.setText(restored["name"]?.toString().orEmpty())
            cityEditText.setText(restored["city"]?.toString().orEmpty())
            streetEditText.setText(restored["street"]?.toString().orEmpty())
            houseNumberEditText.setText(restored["houseNumber"]?.toString().orEmpty())
            floorEditText.setText(restored["floor"]?.toString().orEmpty())
            apartmentEditText.setText(restored["apartment"]?.toString().orEmpty())
            phoneEditText.setText(restored["phone"]?.toString().orEmpty())
        }

        confirmButton.setOnClickListener {
            val name = nameEditText.text.toString().trim()
            val city = cityEditText.text.toString().trim()
            val street = streetEditText.text.toString().trim()
            val houseNumber = houseNumberEditText.text.toString().trim()
            val floor = floorEditText.text.toString().trim()
            val apartment = apartmentEditText.text.toString().trim()
            val phone = phoneEditText.text.toString().trim()

            if (name.isEmpty() || city.isEmpty() || street.isEmpty() || houseNumber.isEmpty() || phone.isEmpty()) {
                Toast.makeText(this, "Please fill all required fields", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (isEditing || isRestoring) {
                prefs.edit()
                    .putString("restored_name", name)
                    .putString("restored_city", city)
                    .putString("restored_street", street)
                    .putString("restored_houseNumber", houseNumber)
                    .putString("restored_floor", floor)
                    .putString("restored_apartment", apartment)
                    .putString("restored_phone", phone)
                    .apply()
            }

            val username = prefs.getString("loggedInUser", null)
            if (username == null) {
                Toast.makeText(this, "User not logged in", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val total = CartManager.getCart().sumOf { it.price * it.quantity }
            val dateFormat = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
            val now = Date()
            val formattedDate = dateFormat.format(now)

            val orderMap = hashMapOf(
                "userId" to username,
                "username" to username,
                "totalPrice" to total,
                "status" to "In Progress",
                "timestamp" to Timestamp.now(),
                "address" to hashMapOf(
                    "name" to name,
                    "city" to city,
                    "street" to street,
                    "houseNumber" to houseNumber,
                    "floor" to floor,
                    "apartment" to apartment,
                    "phone" to phone
                ),
                "items" to CartManager.getCart().map {
                    hashMapOf(
                        "name" to it.name,
                        "price" to it.price,
                        "quantity" to it.quantity,
                        "imageName" to it.imageName
                    )
                }
            ) as HashMap<String, Any>

            val db = FirebaseFirestore.getInstance()

            if (isEditing) {
                orderMap["date"] = editingOrderDate!!
                orderMap["orderNumber"] = editingOrderNumber!!

                db.collection("orders")
                    .document(editingOrderNumber)
                    .set(orderMap)
                    .addOnSuccessListener {
                        prefs.edit()
                            .remove("editing_order_number")
                            .remove("editing_order_user_id")
                            .remove("editing_order_date")
                            .apply()

                        CartManager.clearCart()
                        startActivity(Intent(this, OrdersHistoryActivity::class.java))
                        finish()
                    }
                    .addOnFailureListener { e ->
                        Toast.makeText(this, "Failed to update order: ${e.message}", Toast.LENGTH_LONG).show()
                    }
            } else {
                val newOrderNumber = generateOrderNumber()
                orderMap["date"] = formattedDate
                orderMap["orderNumber"] = newOrderNumber

                db.collection("orders")
                    .document(newOrderNumber)
                    .set(orderMap)
                    .addOnSuccessListener {
                        CartManager.clearCart()
                        startActivity(Intent(this, OrdersHistoryActivity::class.java))
                        finish()
                    }
                    .addOnFailureListener { e ->
                        Toast.makeText(this, "Failed to save order: ${e.message}", Toast.LENGTH_LONG).show()
                    }
            }
        }

        cancelButton.setOnClickListener {
            finish()
        }
    }
}
