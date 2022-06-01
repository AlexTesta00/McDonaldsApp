package com.example.mcdonalds.model

import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import java.lang.IllegalArgumentException
import java.util.*
import java.util.stream.Collectors
import kotlin.random.Random

class McOrder {

    companion object {
        private lateinit var user : McUser
        private lateinit var location : McLocation
        private var items : MutableMap<McItem, Int> = mutableMapOf()
        private val charPool : List<Char> = ('a'..'z') + ('A'..'Z') + ('0'..'9')
        private var id : String = this.generateId()
        private const val STRING_LENGTH = 4

        fun getId(): String {
            return this.id
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

        fun setUser(user: McUser){
            this.user = user
        }

        fun getUserInfo(): McUser? {
            if(this.user != null){
                return this.user
            }
            return null
        }

        fun setLocationInfo(location: McLocation){
            this.location = location
        }

        fun getLocationInfo(): McLocation? {
            if(this.location != null){
                return this.location
            }
            return null
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
            if(user != null){
                this.writeNewOrder()
            }else{
                throw IllegalStateException("The user isn't set")
            }
        }

        private fun writeNewOrder(){
            val database = Firebase.database.reference
            database.child(getUserInfo()!!.email.split("@")[0]).child(getId()).setValue(this.getOnlyItemsName())
        }

        private fun getOnlyItemsName() : MutableList<String>{
            return this.getAllItems().keys.stream().map{it.getName()}.collect(Collectors.toList())
        }

        fun cloneOrder(vararg itemName : String) {
            this.generateId() //Generate New Id for Order

            var mcItems = DownloadManager.getItemsFromName(*itemName) //Get Items From Name
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