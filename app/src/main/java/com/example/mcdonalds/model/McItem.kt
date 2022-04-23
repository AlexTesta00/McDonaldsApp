package com.example.mcdonalds.model

interface McItem {
    fun getName() : String
    fun getImage() : Int
    fun getImageDesc() : String
    fun getTotalPrice() : Double
    fun getCalories() : Int
    fun deleteItem(item: String)
}