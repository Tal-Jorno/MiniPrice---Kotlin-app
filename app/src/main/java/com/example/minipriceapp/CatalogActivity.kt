package com.example.minipriceapp

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView

class CatalogActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_catalog)

        val recyclerView = findViewById<RecyclerView>(R.id.recyclerViewCategories)
        recyclerView.layoutManager = GridLayoutManager(this, 2)

        val categories = listOf(
            CategoryItem("Vegetables and Fruits", R.drawable.ic_vegetables_fruits),
            CategoryItem("Dairy Products and Eggs", R.drawable.ic_dairy_products),
            CategoryItem("Milk and Meat Substitutes", R.drawable.ic_milk_meat_substitutes),
            CategoryItem("Meat, Chicken, and Fish", R.drawable.ic_meat_chicken_fish),
            CategoryItem("Spreads and Salads", R.drawable.ic_spreads_salads),
            CategoryItem("Pasta, Rice, and Legumes", R.drawable.ic_pasta_rice_legumes),
            CategoryItem("Canned Goods", R.drawable.ic_canned_goods),
            CategoryItem("Snacks", R.drawable.ic_snacks),
            CategoryItem("Sweets", R.drawable.ic_sweets),
            CategoryItem("Cereals", R.drawable.ic_cereals),
            CategoryItem("Breads", R.drawable.ic_breads),
            CategoryItem("Frozen Foods", R.drawable.ic_frozen_foods),
            CategoryItem("Ice Cream", R.drawable.ic_ice_cream),
            CategoryItem("Tea and Coffee", R.drawable.ic_tea_coffee),
            CategoryItem("Pharmacy", R.drawable.ic_pharmacy),
            CategoryItem("Beverages", R.drawable.ic_beverages),
            CategoryItem("Cleaning Products", R.drawable.ic_cleaning_products)
        )

        recyclerView.adapter = CategoryAdapter(categories)

        val backButton = findViewById<Button>(R.id.buttonBackToMenu)
        backButton.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
            startActivity(intent)
            finish()
        }
    }
}
