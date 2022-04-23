package com.example.mcdonalds.model

interface Order {
    fun getId() : String
    fun getDate() : String
    fun getAllItems() : MutableMap<McItem, Int>
    fun getTotalPrice() : Double
    fun getTotalCalories() : Int
    fun getUserInfo() : User
    fun getLocationInfo() : Location
    fun addItem(vararg item: McItem)
    fun deleteItem(vararg item: String)
    fun increaseAmount(item: String, amount : Int)
    fun decreaseAmount(item: String, amount : Int)
    fun sendOrder()
    fun cloneOrder(order: Order)
    fun cancelOrder()
}