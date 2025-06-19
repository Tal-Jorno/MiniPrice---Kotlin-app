package com.example.minipriceapp

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.GridLayoutManager
import android.content.Intent
import android.util.Log

class ProductsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_products)

        val categoryName = intent.getStringExtra("category_name") ?: "Unknown Category"
        Log.d("DEBUG_PRODUCTS", "Category received: $categoryName")

        val textView = findViewById<TextView>(R.id.textViewCategoryTitle)
        textView.text = "Products in: $categoryName"

        findViewById<Button>(R.id.buttonBack).setOnClickListener {
            finish()
        }

        findViewById<Button>(R.id.buttonGoToCart).setOnClickListener {
            val orderNumber = intent.getStringExtra("editing_order_number")
            val userId = intent.getStringExtra("editing_user_id")
            val date = intent.getStringExtra("editing_order_date")
            val restoredAddress = intent.getSerializableExtra("restored_address") as? HashMap<String, String>

            Log.d("DEBUG_PRODUCTS", "Navigating to cart with orderNumber=$orderNumber, userId=$userId, date=$date, address=$restoredAddress")

            val intent = Intent(this, CartActivity::class.java).apply {
                putExtra("editing_order_number", orderNumber)
                putExtra("editing_user_id", userId)
                putExtra("editing_order_date", date)
                if (restoredAddress != null) {
                    putExtra("restored_address", restoredAddress)
                    putExtra("is_restoring", true)
                }
            }

            startActivity(intent)
        }



        val products = when (categoryName) {
            "Dairy Products and Eggs" -> listOf(
                ProductItem("Milk", 5.0, "ic_milk"),
                ProductItem("Milk 1%", 5.5, "ic_milk1"),
                ProductItem("Eggs", 8.0, "ic_eggs"),
                ProductItem("Bulgarian Cheese", 9.5, "ic_bulgarian_cheese"),
                ProductItem("Cottage Cheese", 6.8, "ic_cottage_cheese"),
                ProductItem("Cream Cheese", 7.2, "ic_cream_cheese"),
                ProductItem("Labane", 7.0, "ic_labane"),
                ProductItem("Mozzarella", 10.0, "ic_mozzarella"),
                ProductItem("Safed Cheese", 9.0, "ic_safed_cheese"),
                ProductItem("White Cheese", 6.5, "ic_white_cheese"),
                ProductItem("Parmesan", 12.0, "ic_parmesan"),
                ProductItem("Gouda", 11.0, "ic_gouda"),
                ProductItem("Ice Coffee", 4.5, "ic_ice_coffee"),
                ProductItem("Pro Coffee", 5.5, "ic_pro_coffee"),
                ProductItem("Coco Drink", 4.0, "ic_coco"),
                ProductItem("Vanilla protein yogurt", 5.0, "ic_vanilla_protein_yogurt"),
                ProductItem("Natural protein yogurt", 5.0, "ic_natural_protein_yogurt"),
                ProductItem("Yogurt", 4.0, "ic_yogurt"),
                ProductItem("Butter", 3.2, "ic_butter"),
                ProductItem("Sweet cream", 6.0, "ic_sweet_cream"),
                ProductItem("Cook cream", 6.0, "ic_cook_cream")
            )
            "Vegetables and Fruits" -> listOf(
                ProductItem("Apple", 2.5, "ic_apple"),
                ProductItem("Bananas", 3.0, "ic_bananas"),
                ProductItem("Grapes", 4.5, "ic_grapes"),
                ProductItem("Orange", 2.8, "ic_orange"),
                ProductItem("Lemon", 1.5, "ic_lemon"),
                ProductItem("Tomato", 2.0, "ic_tomato"),
                ProductItem("Cucumber", 1.8, "ic_cucumber"),
                ProductItem("Carrot", 2.2, "ic_carrot"),
                ProductItem("Zucchini", 2.0, "ic_zucchini"),
                ProductItem("Eggplant", 2.5, "ic_eggplant"),
                ProductItem("Onion", 1.5, "ic_onion"),
                ProductItem("Potato", 2.0, "ic_potato"),
                ProductItem("Sweet Potato", 3.0, "ic_sweet_potato"),
                ProductItem("Mushrooms", 4.0, "ic_mushrooms"),
                ProductItem("Lettuce", 2.5, "ic_lettuce"),
                ProductItem("Sweet Pepper", 3.2, "ic_sweet_pepper"),

            )
            "Milk and Meat Substitutes" -> listOf(
                ProductItem("Almond Milk", 6.5, "ic_almond_milk"),
                ProductItem("Oat Milk", 6.0, "ic_oat_milk"),
                ProductItem("Soy Milk", 5.5, "ic_soy_milk"),
                ProductItem("Tofu", 8.0, "ic_tofu"),
                ProductItem("Soy Delicacy", 7.5, "ic_soy_delicacy"),
                ProductItem("Protein Soy Delicacy", 8.5, "ic_protein_soy_delicacy"),
                ProductItem("Vegan Burger", 10.0, "ic_vegan_burger")
            )
            "Meat, Chicken, and Fish" -> listOf(
                ProductItem("Ground Beef", 22.0, "ic_ground_beef"),
                ProductItem("Ground Chicken", 18.0, "ic_ground_chicken"),
                ProductItem("Ribeye Steak", 35.0, "ic_ribeye"),
                ProductItem("Chicken Breast", 20.0, "ic_chicken_breast"),
                ProductItem("Chicken Thighs", 18.0, "ic_chicken_thighs"),
                ProductItem("Chicken Legs", 16.0, "ic_chicken_legs"),
                ProductItem("Sausages", 15.0, "ic_sausages"),
                ProductItem("Salmon", 28.0, "ic_salmon"),
                ProductItem("Tilapia", 22.0, "ic_tilapia"),
                ProductItem("Burger Patty", 12.0, "ic_burger"),
                ProductItem("Romanian Kebab", 12.0, "ic_kebab")
            )
            "Spreads and Salads" -> listOf(
                ProductItem("Hummus", 5.0, "ic_hummus"),
                ProductItem("Matbucha", 4.5, "ic_matbucha"),
                ProductItem("Olive Spread", 6.0, "ic_olive_spread"),
                ProductItem("Sun-Dried Tomato Spread", 6.5, "ic_sun_dried_tomato_spread"),
                ProductItem("Tahini", 5.5, "ic_tahini"),
                ProductItem("Cabbage Salad with Mayonnaise", 4.0, "ic_cabbage_salad_with_mayonnaise"),
                ProductItem("Eggplant Salad with Mayonnaise", 4.5, "ic_eggplant_salad_with_mayonnaise"),
                ProductItem("Salted Fish", 7.0, "ic_salted_fish"),
                ProductItem("Salami", 8.0, "ic_salami"),
                ProductItem("Pastrami", 8.5, "ic_pastrami")
            )
            "Pasta, Rice, and Legumes" -> listOf(
                ProductItem("Spaghetti", 6.0, "ic_spaghetti"),
                ProductItem("Penne Pasta", 6.0, "ic_penne_pasta"),
                ProductItem("Ptitim", 5.5, "ic_ptitim"),
                ProductItem("Sweet Potato Ravioli", 9.0, "ic_sweet_potato_ravioli"),
                ProductItem("Lasagna Sheets", 8.0, "ic_lasagna_sheets"),
                ProductItem("Rice", 5.0, "ic_rice"),
                ProductItem("Rice Noodles", 7.0, "ic_rice_noodles"),
                ProductItem("Short Grain Rice", 5.5, "ic_short_grain_rice"),
                ProductItem("Bulgur", 4.5, "ic_bulgur"),
                ProductItem("Couscous", 4.0, "ic_couscous"),
                ProductItem("Quinoa", 6.0, "ic_quinoa"),
                ProductItem("Chickpeas", 4.2, "ic_chickpeas"),
                ProductItem("White Beans", 4.2, "ic_white_beans"),
                ProductItem("Peas", 3.8, "ic_peas"),
                ProductItem("White Flour", 3.0, "ic_white_flour"),
                ProductItem("Spelt Flour", 4.0, "ic_spelt_flour")
            )
            "Canned Goods" -> listOf(
                ProductItem("Beans", 4.5, "ic_beans"),
                ProductItem("Chickpeas", 4.2, "ic_canned_chickpeas"),
                ProductItem("Corn", 3.8, "ic_corn"),
                ProductItem("Olives", 6.0, "ic_olives"),
                ProductItem("Pickles", 5.0, "ic_pickles"),
                ProductItem("Pineapple", 6.5, "ic_pineapple"),
                ProductItem("Tuna in Water", 6.0, "ic_tuna_in_water"),
                ProductItem("Tuna in Oil", 6.5, "ic_tuna_in_oil"),
                ProductItem("Champignon Mushrooms", 5.5, "ic_champignon_mushrooms"),
                ProductItem("Crushed Tomatoes", 4.0, "ic_crushed_tomatoes"),
                ProductItem("Tomato Paste", 3.5, "ic_tomato_paste"),
                ProductItem("Pizza Tomato Sauce", 5.0, "ic_pizza_tomato_sauce"),
                ProductItem("Ketchup", 5.0, "ic_ketchup"),
                ProductItem("Mayonnaise", 5.5, "ic_mayonnaise"),
                ProductItem("Garlic Sauce", 5.0, "ic_garlic_sauce"),
                ProductItem("Chili Sauce", 5.5, "ic_chili_sauce"),
                ProductItem("Soy Sauce", 5.5, "ic_soy_sauce"),
                ProductItem("Teriyaki Sauce", 6.0, "ic_teriyaki_sauce")
            )
            "Snacks" -> listOf(
                ProductItem("Bamba", 3.0, "ic_bamba"),
                ProductItem("Bamba Nougat Filled", 4.0, "ic_bamba_nougat_filled"),
                ProductItem("Bisli Grill", 3.2, "ic_bisli_grill"),
                ProductItem("Bisli BBQ", 3.2, "ic_bisli_bbq"),
                ProductItem("Apropos", 4.0, "ic_apropos"),
                ProductItem("Cheetos", 3.0, "ic_cheetos"),
                ProductItem("Doritos Grill", 4.5, "ic_doritos_grill"),
                ProductItem("Doritos Sour", 4.5, "ic_doritos_sour"),
                ProductItem("Pringles", 6.5, "ic_pringles"),
                ProductItem("Popcorn", 3.0, "ic_popcorn"),
                ProductItem("Nachos", 4.0, "ic_nachos"),
                ProductItem("Abadi Cookies", 5.0, "ic_abadi_cookies"),
                ProductItem("Chocolate Cakes", 4.0, "ic_chocolate_cakes"),
                ProductItem("Corn Cakes", 3.5, "ic_corn_cakes"),
                ProductItem("Rice Cakes", 3.5, "ic_rice_cakes"),
                ProductItem("Sesame Crackers", 4.0, "ic_sesame_crackers"),
                ProductItem("Tapuchips", 3.5, "ic_tapuchips"),
                ProductItem("Pretzels", 3.0, "ic_pretzels"),
                ProductItem("Fitness Sweet Potato Pretzel", 4.2, "ic_fitness_sweet_potato_pretzel"),
                ProductItem("Fitness Beet Pretzel", 4.2, "ic_fitness_beet_pretzel"),
                ProductItem("Fitness Thin Beet Cracker", 4.2, "ic_fitness_thin_beet_cracker"),
                ProductItem("Fitness Thin Rosemary Cracker", 4.2, "ic_fitness_thin_rosemary_cracker")
            )
            "Sweets" -> listOf(
                ProductItem("Click Balls", 3.5, "ic_click_balls"),
                ProductItem("Click Cornflakes", 3.5, "ic_click_cornflakes"),
                ProductItem("Ferrero Rocher", 7.0, "ic_ferrero_rocher"),
                ProductItem("Fruit Gum", 3.0, "ic_fruit_gum"),
                ProductItem("Gummy Candies", 3.5, "ic_gummy_candies"),
                ProductItem("House Cake", 5.0, "ic_house_cake"),
                ProductItem("Kif Kef", 3.5, "ic_kif_kef"),
                ProductItem("Kinder Bueno", 4.5, "ic_kinder_bueno"),
                ProductItem("Kinder Fingers", 4.0, "ic_kinder_fingers"),
                ProductItem("Loacker Wafers", 4.0, "ic_loacker_wafers"),
                ProductItem("Mars", 3.5, "ic_mars"),
                ProductItem("Mekupelet", 3.5, "ic_mekupelet"),
                ProductItem("Mentos", 2.5, "ic_mentos"),
                ProductItem("Milka Cookies", 5.0, "ic_milka_cookies"),
                ProductItem("Milka Cookies Biscuits", 5.5, "ic_milka_cookies_biscuits"),
                ProductItem("Milka Oreo", 5.0, "ic_milka_oreo"),
                ProductItem("Mint Gum", 2.5, "ic_mint_gum"),
                ProductItem("Nutella Cookies", 5.5, "ic_nutella_cookies"),
                ProductItem("Oreo", 4.0, "ic_oreo"),
                ProductItem("Pare Nut Chocolate", 5.0, "ic_pare_nut_chocolate"),
                ProductItem("Pare Chocolate", 4.5, "ic_pare_chocolate"),
                ProductItem("Pesek Zman", 3.5, "ic_pesek_zman"),
                ProductItem("Snickers", 3.5, "ic_snickers"),
                ProductItem("Toffee Candies", 3.0, "ic_toffee_candies"),
                ProductItem("Toffifee", 4.5, "ic_toffifee"),
                ProductItem("Tortit", 3.5, "ic_tortit"),
                ProductItem("Twix", 3.5, "ic_twix"),
                ProductItem("Petiber", 4.0, "ic_petiber")
            )
            "Cereals" -> listOf(
                ProductItem("Cornflakes", 5.0, "ic_cornflakes"),
                ProductItem("Branflakes", 5.0, "ic_branflakes"),
                ProductItem("Cheerios", 6.0, "ic_cheerios"),
                ProductItem("Coco Man", 5.5, "ic_coco_man"),
                ProductItem("Crunch Cereal", 5.5, "ic_crunch_cereal"),
                ProductItem("Fitness Cereal", 6.0, "ic_fitness_cereal"),
                ProductItem("Granola", 7.0, "ic_granola"),
                ProductItem("Oats", 4.0, "ic_oats"),
                ProductItem("Ogi", 4.5, "ic_ogi"),
                ProductItem("Kariot", 5.0, "ic_kariot"),
                ProductItem("Nature Valley", 5.5, "ic_nature_valley"),
                ProductItem("Allin Bar", 4.5, "ic_allin_bar"),
                ProductItem("Energy Bar", 4.5, "ic_energy_bar"),
                ProductItem("Shugi Bar", 4.5, "ic_shugi_bar")
            )
            "Breads" -> listOf(
                ProductItem("White Bread", 5.0, "ic_white_bread"),
                ProductItem("Grain Bread", 6.0, "ic_grain_bread"),
                ProductItem("Sourdough Bread", 6.5, "ic_sourdough_bread"),
                ProductItem("Light Bread", 5.5, "ic_light_bread"),
                ProductItem("Pita", 4.0, "ic_pita"),
                ProductItem("Light Pita", 4.5, "ic_light_pita"),
                ProductItem("Baguette", 5.0, "ic_baguette"),
                ProductItem("Buns", 4.0, "ic_buns")
            )
            "Frozen Foods" -> listOf(
                ProductItem("Bourekas", 7.0, "ic_bourekas"),
                ProductItem("Broccoli", 6.0, "ic_broccoli"),
                ProductItem("Cauliflower", 6.0, "ic_cauliflower"),
                ProductItem("Green Beans", 5.5, "ic_green_beans"),
                ProductItem("Mixed Vegetables", 6.5, "ic_mixed_vegetables"),
                ProductItem("Frozen Berries", 9.0, "ic_frozen_berries"),
                ProductItem("Frozen Mango", 9.0, "ic_frozen_mango"),
                ProductItem("Frozen Pineapple", 9.0, "ic_frozen_pineapple"),
                ProductItem("Jachnun", 8.0, "ic_jachnun"),
                ProductItem("Falafel", 7.0, "ic_falafel"),
                ProductItem("Mamaof Schnitzel", 10.0, "ic_mamaof_schnitzel"),
                ProductItem("Pizza Maadanot", 11.0, "ic_maadanot_pizza"),
                ProductItem("Potato Cigars", 7.5, "ic_potato_cigars")
            )
            "Ice Cream" -> listOf(
                ProductItem("Ben & Jerry's Chocolate", 14.0, "ic_ben_and_jerrys_chocolate"),
                ProductItem("Ben & Jerry's Vanilla", 14.0, "ic_ben_and_jerrys_vanilla"),
                ProductItem("Glidell Chocolate", 10.0, "ic_glidell_chocolate"),
                ProductItem("Glidell Vanilla", 10.0, "ic_glidell_vanilla"),
                ProductItem("Glidoniyot", 9.0, "ic_glidoniyot"),
                ProductItem("Kasata", 7.0, "ic_kasata"),
                ProductItem("Kremissimo Vanilla Cookies", 12.0, "ic_kremissimo_vanilla_cookies"),
                ProductItem("Mini Magnum Chocolate", 11.0, "ic_mini_magnum_chocolate"),
                ProductItem("Mini Magnum Vanilla", 11.0, "ic_mini_magnum_vanilla"),
                ProductItem("Nestle Tilon", 6.0, "ic_nestle_tilon"),
                ProductItem("Solero Strawberry", 6.5, "ic_solero_strawberry"),
                ProductItem("Crunch Mini Cornflakes", 7.0, "ic_crunch_mini_cornflakes")
            )
            "Tea and Coffee" -> listOf(
                ProductItem("Black Tea", 4.0, "ic_black_tea"),
                ProductItem("Mint Tea", 4.0, "ic_mint_tea"),
                ProductItem("Chamomile Tea", 4.5, "ic_chamomile_tea"),
                ProductItem("Berries Tea", 4.5, "ic_berries_tea"),
                ProductItem("Nescafe Elite", 6.0, "ic_nescafe_elite"),
                ProductItem("Nescafe Taster's Choice", 6.5, "ic_nescafe_tasters_choice"),
                ProductItem("Turkish Coffee", 5.0, "ic_turkish_coffee")
            )
            "Pharmacy" -> listOf(
                ProductItem("Body Wash", 9.0, "ic_body_wash"),
                ProductItem("Shampoo", 10.0, "ic_shampoo"),
                ProductItem("Conditioner", 10.0, "ic_conditioner"),
                ProductItem("Wet Wipes", 7.0, "ic_wet_wipes"),
                ProductItem("Hand Soap", 5.0, "ic_hand_soap"),
                ProductItem("Toilet Paper", 12.0, "ic_toilet_paper"),
                ProductItem("Hair Cream", 8.0, "ic_hair_cream"),
                ProductItem("Baby Oil", 9.0, "ic_baby_oil"),
                ProductItem("Pacifiers", 6.0, "ic_pacifiers"),
                ProductItem("Baby Shampoo", 8.0, "ic_baby_shampoo"),
                ProductItem("Baby Soap", 7.0, "ic_baby_soap"),
                ProductItem("Toothbrushes", 6.0, "ic_toothbrushes"),
                ProductItem("Toothpaste", 7.0, "ic_toothpaste"),
                ProductItem("Men Spray Deodorant", 9.0, "ic_men_spray_deodorant"),
                ProductItem("Women Spray Deodorant", 9.0, "ic_women_spray_deodorant"),
                ProductItem("Men Speed Stick", 10.0, "ic_men_speed_stick"),
                ProductItem("Women Speed Stick", 10.0, "ic_women_speed_stick"),
                ProductItem("Hand Cream", 8.0, "ic_hand_cream"),
                ProductItem("Foot Cream", 8.0, "ic_foot_cream"),
                ProductItem("Ear Swabs", 4.0, "ic_ear_swabs"),
                ProductItem("Toothpicks", 3.0, "ic_toothpicks"),
                ProductItem("Hair Brush", 12.0, "ic_hair_brush"),
                ProductItem("Tweezers", 5.0, "ic_tweezers"),
                ProductItem("Face Sunscreen", 14.0, "ic_face_sunscreen"),
                ProductItem("Spray Sunscreen", 15.0, "ic_spray_sunscreen")
            )
            "Beverages" -> listOf(
                ProductItem("Coca Cola", 6.0, "ic_coca_cola"),
                ProductItem("Coca Cola Cans", 5.0, "ic_coca_cola_cans"),
                ProductItem("Coca Cola Zero", 6.0, "ic_coca_cola_zero"),
                ProductItem("Coca Cola Zero Cans", 5.0, "ic_coca_cola_zero_cans"),
                ProductItem("Fanta", 6.0, "ic_fanta"),
                ProductItem("Fanta Can", 5.0, "ic_fanta_can"),
                ProductItem("Orange Juice", 7.0, "ic_orange_juice"),
                ProductItem("Grape Juice", 7.0, "ic_grape_juice"),
                ProductItem("Soda", 4.0, "ic_soda"),
                ProductItem("Mineral Water", 3.0, "ic_mineral_water"),
                ProductItem("Mineral Water Mini", 2.0, "ic_mineral_water_mini"),
                ProductItem("Beer Heineken", 6.0, "ic_beer_heineken"),
                ProductItem("Red Wine", 18.0, "ic_red_wine"),
                ProductItem("White Wine", 18.0, "ic_white_wine"),
                ProductItem("Sparkling Wine", 20.0, "ic_sparkling_wine")
            )
            "Cleaning Products" -> listOf(
                ProductItem("Bleach", 7.0, "ic_bleach"),
                ProductItem("Cleaning Wipes", 8.0, "ic_cleaning_wipes"),
                ProductItem("Dish Sponges", 5.0, "ic_dish_sponges"),
                ProductItem("Kitchen Cleaner", 9.0, "ic_kitchen_cleaner"),
                ProductItem("Mold Remover", 10.0, "ic_mold_remover"),
                ProductItem("Nitrile Gloves", 6.0, "ic_nitrile_gloves"),
                ProductItem("Paper Towels", 10.0, "ic_paper_towels"),
                ProductItem("Floor Cleaner (Ritzpaz)", 11.0, "ic_ritzpaz_floor_cleaner"),
                ProductItem("Furniture Cleaner (Sano)", 10.0, "ic_sano_furniture_cleaner"),
                ProductItem("Shower Cleaner", 9.0, "ic_shower_cleaner"),
                ProductItem("Toilet Cleaner", 9.0, "ic_toilet_cleaner"),
                ProductItem("Upholstery Cleaner", 10.0, "ic_upholstery_cleaner"),
                ProductItem("Trash Bags", 6.0, "ic_trash_bags")
            )
            else -> {
                Log.w("DEBUG_PRODUCTS", "Unknown category: $categoryName, no products loaded!")
                emptyList()
            }
        }
        Log.d("DEBUG_PRODUCTS", "Loaded ${products.size} products for category: $categoryName")

        val recyclerView = findViewById<RecyclerView>(R.id.recyclerViewProducts)
        recyclerView.layoutManager = GridLayoutManager(this, 2)
        recyclerView.adapter = ProductAdapter(products)
    }
}
