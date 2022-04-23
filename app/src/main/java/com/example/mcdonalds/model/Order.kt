package com.example.mcdonalds.model

import java.util.*

interface Order {
    fun getId() : String
    fun getDate() : Date
    fun getAllItems() : MutableMap<McItem, Int>
    fun getTotalPrice() : Double
    fun getTotalCalories() : Int
    fun getUserInfo() : User
    fun getLocationInfo() : Location
    fun addItem(item: McItem)
    fun deleteItem(item: String)
    fun increaseAmount(item: McItem, amount : Int)
    fun decreaseAmount(item: McItem, amount : Int)
    fun sendOrder()
    fun cloneOrder(order: Order)
    fun cancelOrder()
}