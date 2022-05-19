package com.example.mcdonalds.model

import android.os.Build
import java.lang.IllegalArgumentException
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*
import java.util.stream.Collectors

class McOrder {

    companion object {
        //private lateinit var user : User
        private lateinit var user : McUser
        private var items : MutableMap<McItem, Int> = mutableMapOf()
        private var location : Location = McLocation()
        private val charPool : List<Char> = ('a'..'z') + ('A'..'Z') + ('0'..'9')
        private var id : String = this.generateId()
        private var date: String = this.getCurrentDate()
        private const val STRING_LENGTH = 4

        fun getId(): String {
            return this.id.substring(0, STRING_LENGTH)
        }

        fun getAllItems(): MutableMap<McItem, Int> {
            return Collections.unmodifiableMap(this.items)
        }

        fun getTotalPrice(): Double {
            var price = 0.00
            if(this.items.isNotEmpty()){
                val priceSingleItem =
                    this.items.keys.stream().map{ it.getTotalPrice() }.collect(Collectors.toList())
                val quantity = this.items.values.stream().map { it.toDouble() }.collect(Collectors.toList())
                for (i in 0 until priceSingleItem.size) {
                    price += priceSingleItem[i] * quantity[i]
                }

            }
            return price
        }

        fun getTotalCalories(): Int {
            val caloriesSingleItem =
                this.items.keys.stream().map { it.getCalories() }.collect(Collectors.toList())
            val quantity = this.items.values.stream().collect(Collectors.toList())
            var calories = 0
            for (i in 0..caloriesSingleItem.size) {
                calories += caloriesSingleItem[i] * quantity[i]
            }
            return calories
        }

        fun setUser(user: McUser){
            this.user = user
        }

        fun getUserInfo(): McUser? {
            if(this.user != null){
                return this.user
            }
            return null
        }

        fun getLocationInfo(): Location {
            return this.location
        }

        fun addItem(item: McItem, value : Int){
            if(this.items.contains(item)){
                val totalValue = this.items[item]!! + (value)
                this.items.replace(item, totalValue)
            }else{
                this.items[item] = value
            }
        }

        private fun deleteItem(vararg item: String) {
            item.forEach {
                if (this.containsItem(it)) {
                    this.items.remove(this.getMcItemFromString(it))
                } else {
                    throw IllegalArgumentException("$it is not present on order")
                }
            }
        }

        fun sendOrder() {
            /*
            if(user != null){

            }else{
                throw IllegalStateException("The user isn't set")
            }
            */
        }

        fun cloneOrder(order: Order) {
            this.items = order.getAllItems()
        }

        fun cancelOrder() {
            this.items.clear()
        }

        private fun generateId(): String {
            val randomString = (1..STRING_LENGTH)
                .map { kotlin.random.Random.nextInt(0, charPool.size) }
                .map(charPool::get)
                .joinToString("")
            return randomString + getCurrentDate() //+ this.user.getEmail()
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

        private fun containsItem(item: String): Boolean {
            return this.items.keys.map { it.getName().lowercase() }.contains(item)
        }

        private fun getMcItemFromString(item: String): McItem {
            return this.items.entries.stream().filter { it.key.getName() == item }.findAny()
                .get().key
        }

        private fun getValuesFromMcItem(item: String): Int {
            return this.items[this.getMcItemFromString(item)]!!
        }

        private fun updateMcItems() {
            if (this.items.values.stream().filter { it <= 0 }.count() >= 1) {
                val itemName =
                    this.items.entries.stream().filter { it.value <= 0 }.map { it.key.getName() }
                        .collect(Collectors.toList())
                this.deleteItem(*itemName.toTypedArray())
            }
        }

        override fun hashCode(): Int {
            return id.hashCode()
        }

        override fun toString(): String {
            return "McOrder(id='$id', date='$date', items=$items, location=$location, user=user, charPool=$charPool)"
        }
    }

}