package com.example.minipriceapp

object OrderHistoryManager {
    private val orderHistory = mutableListOf<Order>()

    fun addOrder(order: Order) {
        orderHistory.add(order)
    }

    fun getOrders(): List<Order> {
        return orderHistory
    }

    fun clearOrders() {
        orderHistory.clear()
    }
}
