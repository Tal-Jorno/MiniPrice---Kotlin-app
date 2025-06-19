package com.example.minipriceapp

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class ProductItem(
    var name: String = "",
    var price: Double = 0.0,
    var imageName: String = "",
    var quantity: Int = 0
) : Parcelable
