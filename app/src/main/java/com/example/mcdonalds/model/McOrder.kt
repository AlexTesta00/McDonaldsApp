package com.example.mcdonalds.model

import java.util.*
import java.util.stream.Collectors

class McOrder : Order{

    private var id : String = ""
    private var date: Date = Date()
    private var items : MutableMap<McItem, Int> = mutableMapOf()

    companion object{
        private const val UNIT : Int = 0
    }

    constructor(){

    }

    override fun getId(): String {
        return this.id
    }

    override fun getDate(): Date {
        return this.date
    }

    override fun getAllItems(): MutableMap<McItem, Int> {
        return Collections.unmodifiableMap(this.items)
    }

    override fun getTotalPrice(): Double {
        val priceSingleItem = this.items.keys.stream().map { it.getTotalPrice() }.collect(Collectors.toList())
        val quantity = this.items.values.stream().collect(Collectors.toList())
        var price = 0.00
        for (i in 0..priceSingleItem.size){
            price += priceSingleItem[i] * quantity[i]
        }
        return price
    }

    override fun getTotalCalories(): Int {
        val caloriesSingleItem = this.items.keys.stream().map { it.getCalories() }.collect(Collectors.toList())
        val quantity = this.items.values.stream().collect(Collectors.toList())
        var calories = 0
        for (i in 0..caloriesSingleItem.size){
            calories += caloriesSingleItem[i] * quantity[i]
        }
        return calories
    }

    override fun getUserInfo(): User {
        TODO("Not yet implemented")
    }

    override fun getLocationInfo(): Location {
        TODO("Not yet implemented")
    }

    override fun addItem(item: McItem) {
        TODO("Not yet implemented")
    }

    override fun deleteItem(item: String) {
        TODO("Not yet implemented")
    }

    override fun increaseAmount(item: McItem, amount: Int) {
        TODO("Not yet implemented")
    }

    override fun decreaseAmount(item: McItem, amount: Int) {
        TODO("Not yet implemented")
    }

    override fun sendOrder() {
        TODO("Not yet implemented")
    }

    override fun cloneOrder(order: Order) {
        TODO("Not yet implemented")
    }

    override fun cancelOrder() {
        this.items.clear()
    }

}