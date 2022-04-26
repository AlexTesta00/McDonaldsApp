package com.example.mcdonalds

import android.util.Log
import com.example.mcdonalds.model.Category
import com.example.mcdonalds.model.Ingredient
import com.example.mcdonalds.model.McOrder
import com.example.mcdonalds.model.SingleMcItem
import org.junit.Test


class OrderTest {

    private val bread : Ingredient = Ingredient("Bread", 200.00f, 200)
    private val chicken : Ingredient = Ingredient("Chiken", 200.00f, 250)
    private val salad : Ingredient = Ingredient("Salad", 50.00f, 100)
    private val ketchup : Ingredient = Ingredient("ketchup", 0.50f, 10)
    private val mcChicken : SingleMcItem = SingleMcItem("McChiken", 1, "Bread whit Chiken", 4.60, true, Category("Hamburger"),
    mutableListOf(bread,chicken,salad,ketchup)
    )


    @Test
    fun price(){
        McOrder.addItem(mcChicken,2)
        McOrder.getTotalPrice()
        Log.d("order",McOrder.getTotalPrice().toString())
    }
}