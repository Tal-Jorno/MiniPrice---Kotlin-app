package com.example.minipriceapp

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import androidx.annotation.Keep
import com.google.firebase.Timestamp

@Keep
@Parcelize
data class Order(
    var orderNumber: String = "",
    var date: String = "",
    var totalPrice: Double = 0.0,
    var items: List<ProductItem> = emptyList(),
    var status: String = "In Progress",
    var userId: String = "",
    var username: String = "",
    var address: Map<String, String>? = null,
    var timestamp: Timestamp? = null
) : Parcelable
