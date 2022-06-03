package com.example.mcdonalds.model

import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import java.lang.IllegalArgumentException
import java.util.*
import java.util.stream.Collectors
import kotlin.random.Random

class McOrder {

    companion object {
        private var items : MutableMap<McItem, Int> = mutableMapOf()
        private val charPool : List<Char> = ('a'..'z') + ('A'..'Z') + ('0'..'9')
        var user : McUser? = null
        var location : McLocation? = null
        var id : String = this.generateId()
        private const val STRING_LENGTH = 4

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

        fun addItem(item: McItem, value : Int){
            if(this.items.contains(item)){
                val totalValue = this.items[item]!! + (value)
                this.items.replace(item, totalValue)
            }else{
                this.items[item] = value
            }
        }

        fun deleteItem(vararg item: McItem) {
            item.forEach {
                if (items.contains(it)) {
                    this.items.remove(it)
                } else {
                    throw IllegalArgumentException("$it is not present on order")
                }
            }
        }

        fun sendOrder() {
            if(user != null && location != null){
                this.writeNewOrder()
                this.deleteItem(*items.keys.toTypedArray())
                this.location = null
            }else{
                throw IllegalStateException("The user isn't set or Location not set")
            }
        }

        private fun writeNewOrder(){
            val database = Firebase.database.reference
            database.child(this.user!!.email.split("@")[0]).child(id).setValue(this.getOnlyItemsName())
        }

        private fun getOnlyItemsName() : MutableList<String>{
            return this.getAllItems().keys.stream().map{it.getName()}.collect(Collectors.toList())
        }

        fun cloneOrder(vararg itemName : String) {
            this.generateId() //Generate New Id for Order

            val mcItems = DownloadManager.getItemsFromName(*itemName) //Get Items From Name
            this.items.clear() //Clear Cart

            /*Transform List in to Mutable Map
            * Example : {McChiken=1, ...}*/
            this.items = mcItems.associateWith { 1 }.toMutableMap()
        }

        fun cancelOrder() {
            this.items.clear()
        }

        fun changeId(){
            this.id = this.generateId()
        }

        private fun generateId(): String {
            return (1..STRING_LENGTH)
                .map { Random.nextInt(0, charPool.size) }
                .map(charPool::get)
                .joinToString("")
        }

        override fun hashCode(): Int {
            return id.hashCode()
        }

        override fun toString(): String {
            return "McOrder(id='$id', items=$items, location=$location, user=user, charPool=$charPool)"
        }
    }

}