package com.example.minipriceapp

import android.content.Context
import com.google.firebase.firestore.FirebaseFirestore

object OrderManager {
    private val orders = mutableListOf<Order>()
    private val db = FirebaseFirestore.getInstance()

    private fun generateOrderNumber(): String {
        val chars = ('A'..'Z') + ('0'..'9')
        return (1..8)
            .map { chars.random() }
            .joinToString("")
    }

    fun addOrder(
        context: Context,
        order: Order,
        onSuccess: () -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        val prefs = context.getSharedPreferences("MiniPricePrefs", Context.MODE_PRIVATE)
        val user = prefs.getString("loggedInUser", null)
        if (user == null) {
            onFailure(Exception("User not logged in"))
            return
        }

        val orderNumber = generateOrderNumber()
        val orderWithUser = order.copy(
            userId = user,
            username = user,
            orderNumber = orderNumber
        )

        db.collection("orders")
            .add(orderWithUser)
            .addOnSuccessListener {
                orders.add(orderWithUser)
                onSuccess()
            }
            .addOnFailureListener { e ->
                onFailure(e)
            }
    }

    fun addOrders(newOrders: List<Order>) {
        orders.addAll(newOrders)
    }

    fun updateOrders(newOrders: List<Order>) {
        orders.clear()
        orders.addAll(newOrders)
    }

    fun getOrders(): MutableList<Order> {
        return orders
    }

    fun getOrder(index: Int): Order? {
        return orders.getOrNull(index)
    }

    fun clearOrders() {
        orders.clear()
    }
}
