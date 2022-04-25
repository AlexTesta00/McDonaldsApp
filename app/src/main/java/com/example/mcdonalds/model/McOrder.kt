package com.example.mcdonalds.model

import android.os.Build
import java.lang.IllegalArgumentException
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*
import java.util.stream.Collectors

class McOrder(user: McUser) : Order{

    private var id : String = ""
    private var date: String = ""
    private var items : MutableMap<McItem, Int> = mutableMapOf()
    private var location : Location = McLocation()
    private var user : User = user
    private val charPool : List<Char> = ('a'..'z') + ('A'..'Z') + ('0'..'9')

    companion object{
        private const val UNIT : Int = 0
        private const val STRING_LENGTH = 4
    }

    init {
        this.id = this.generateId()
        this.date = this.getCurrentDate()
    }

    override fun getId(): String {
        return this.id.substring(0, STRING_LENGTH)
    }

    override fun getDate(): String {
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
        return this.user
    }

    override fun getLocationInfo(): Location {
        return this.location
    }

    override fun addItem(vararg item: McItem) {
        item.forEach {
            if(this.containsItem(it.getName())){
                this.increaseAmount(it.getName(), UNIT)
            }else{
                this.items[it] = UNIT
            }
        }
    }

    override fun deleteItem(vararg item: String) {
        item.forEach {
            if(this.containsItem(it)) {
                this.items.remove(this.getMcItemFromString(it))
            }else{
                throw IllegalArgumentException("$it is not present on order")
            }
        }
    }

    override fun increaseAmount(item: String, amount: Int) {
        if(this.containsItem(item) && amount > 0){
            this.items[this.getMcItemFromString(item)] = (this.getValuesFromMcItem(item) + amount)
        }else{
            throw IllegalArgumentException("$item doesn't exist or amount must be > 0")
        }
    }

    override fun decreaseAmount(item: String, amount: Int) {
        val itemValue = this.getValuesFromMcItem(item)
        if(this.containsItem(item) && amount > 0 &&  itemValue >= amount){
            this.items[this.getMcItemFromString(item)] = (itemValue - amount)
            this.updateMcItems() //Delete all McItem have 0 or negative value
        }else{
            throw IllegalArgumentException("$item doesn't exist or amount must be > 0")
        }
    }

    override fun sendOrder() {
        TODO("Not yet implemented")
    }

    override fun cloneOrder(order: Order) {
        this.items = order.getAllItems()
    }

    override fun cancelOrder() {
        this.items.clear()
    }

    private fun generateId() : String{
        val randomString = (1..STRING_LENGTH)
            .map { kotlin.random.Random.nextInt(0, charPool.size) }
            .map(charPool::get)
            .joinToString("")
        return randomString + getCurrentDate()
    }

    private fun getCurrentDate(): String {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val current = LocalDateTime.now()
            val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS")
            current.format(formatter)
        } else {
            val sdf = SimpleDateFormat("dd/M/yyyy hh:mm:ss")
            sdf.format(Date())
        }
    }

    private fun containsItem(item: String) : Boolean{
        return this.items.keys.map { it.getName().lowercase() }.contains(item)
    }

    private fun getMcItemFromString(item: String) : McItem{
        return this.items.entries.stream().filter{it.key.getName() == item}.findAny().get().key
    }

    private fun getValuesFromMcItem(item: String): Int {
        return this.items[this.getMcItemFromString(item)]!!
    }

    private fun updateMcItems() {
        if(this.items.values.stream().filter{it <= 0}.count() >= 1){
            val itemName = this.items.entries.stream().filter{it.value <= 0}.map { it.key.getName() }.collect(Collectors.toList())
            this.deleteItem(*itemName.toTypedArray())
        }
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as McOrder

        if (id != other.id) return false

        return true
    }

    override fun hashCode(): Int {
        return id.hashCode()
    }

    override fun toString(): String {
        return "McOrder(id='$id', date='$date', items=$items, location=$location, user=$user, charPool=$charPool)"
    }


}