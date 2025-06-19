package com.example.minipriceapp

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.firestore.FirebaseFirestore

const val ADMIN = "AdminMiniPrice"
const val ADMIN_PASSWORD = "AdminMiniPrice123456"

class LoginActivity : AppCompatActivity() {

    private lateinit var db: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        db = FirebaseFirestore.getInstance()

        val editTextUsername = findViewById<EditText>(R.id.editTextUsername)
        val editTextPassword = findViewById<EditText>(R.id.editTextPassword)
        val buttonLogin = findViewById<Button>(R.id.buttonLogin)
        val buttonRegister = findViewById<Button>(R.id.buttonRegister)

        buttonLogin.setOnClickListener {
            val username = editTextUsername.text.toString().trim()
            val password = editTextPassword.text.toString().trim()

            if (username.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // enter as admin
            if (username == ADMIN && password == ADMIN_PASSWORD) {
                val prefs = getSharedPreferences("MiniPricePrefs", MODE_PRIVATE)
                prefs.edit().putString("loggedInUser", username).apply()

                Toast.makeText(this, "Admin login successful!", Toast.LENGTH_SHORT).show()
                startActivity(Intent(this, AdminDashboardActivity::class.java))
                finish()
                return@setOnClickListener
            }

            // enter as user guest
            db.collection("users").document(username).get().addOnSuccessListener { document ->
                if (document.exists()) {
                    val storedPassword = document.getString("password")
                    if (storedPassword == password) {
                        val prefs = getSharedPreferences("MiniPricePrefs", MODE_PRIVATE)
                        prefs.edit().putString("loggedInUser", username).apply()

                        Toast.makeText(this, "Login successful!", Toast.LENGTH_SHORT).show()
                        startActivity(Intent(this, MainActivity::class.java))
                        finish()
                    } else {
                        Toast.makeText(this, "Incorrect password", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(
                        this,
                        "User not found. Press REGISTER to create new account.",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }.addOnFailureListener {
                Toast.makeText(this, "Error accessing database", Toast.LENGTH_SHORT).show()
            }
        }

        buttonRegister.setOnClickListener {
            val username = editTextUsername.text.toString().trim()
            val password = editTextPassword.text.toString().trim()

            if (username.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            db.collection("users").document(username).get().addOnSuccessListener { document ->
                if (document.exists()) {
                    Toast.makeText(this, "Username already exists. Choose another.", Toast.LENGTH_SHORT).show()
                } else {
                    val user = hashMapOf("username" to username, "password" to password)
                    db.collection("users").document(username).set(user).addOnSuccessListener {
                        val prefs = getSharedPreferences("MiniPricePrefs", MODE_PRIVATE)
                        prefs.edit().putString("loggedInUser", username).apply()

                        Toast.makeText(this, "Registration successful!", Toast.LENGTH_SHORT).show()
                        startActivity(Intent(this, MainActivity::class.java))
                        finish()
                    }.addOnFailureListener {
                        Toast.makeText(this, "Registration failed", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }
}
