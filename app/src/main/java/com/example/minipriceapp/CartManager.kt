package com.example.minipriceapp

object CartManager {
    private val cartItems = mutableMapOf<String, ProductItem>()

    fun updateCart(product: ProductItem) {
        if (product.quantity > 0) {
            cartItems[product.name] = product.copy()
        } else {
            cartItems.remove(product.name)
        }
    }

    fun addItem(product: ProductItem) {
        val existing = cartItems[product.name]
        if (existing != null) {
            val updatedQuantity = existing.quantity + product.quantity
            cartItems[product.name] = existing.copy(quantity = updatedQuantity)
        } else {
            cartItems[product.name] = product.copy()
        }
    }

    fun getCart(): List<ProductItem> {
        return cartItems.values.toList()
    }

    fun getQuantityFor(productName: String): Int {
        return cartItems[productName]?.quantity ?: 0
    }

    fun clearCart() {
        cartItems.clear()
    }
}
