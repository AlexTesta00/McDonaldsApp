package com.example.mcdonalds.model

import android.graphics.Bitmap

interface McItem {
    fun getName() : String
    fun getImage() : String
    fun getImageDesc() : String
    fun getTotalPrice() : Double
    fun getCalories() : Int
    fun deleteItem(item: String)
}