package com.example.mcdonalds.model

import java.lang.IllegalArgumentException
import java.util.*
import java.util.stream.Collectors

class McMenu (
        private val name : String,
        private val image : Int,
        private val imageDescription : String,
        private val price : Double,
        private var singleMcItem: MutableList<SingleMcItem>
        )
    : McItem {
    override fun getName(): String {
        return this.name
    }

    override fun getImage(): Int {
        return this.image
    }

    override fun getImageDesc(): String {
        return this.imageDescription
    }

    override fun getTotalPrice(): Double {
        return this.price
    }

    override fun getCalories(): Int {
        return  this.singleMcItem.stream().mapToInt{ it.getCalories() }.sum()
    }

    override fun deleteItem(item: String) {
        if(this.itemIsPresent(item)){
            this.singleMcItem.removeAt(this.getItemIndex(item))
        }else{
            throw IllegalArgumentException("$item is not present in menu")
        }
    }

    fun getMenuItems() : List<SingleMcItem>{
        return Collections.unmodifiableList(this.singleMcItem)
    }

    private fun itemIsPresent(item: String) : Boolean{
        return this.singleMcItem.stream().map { it.getName().lowercase() }.collect(Collectors.toList()).contains(item.lowercase())
    }

    private fun getItemIndex(item: String) : Int {
        return this.singleMcItem.stream().map { it.getName().lowercase() }.collect(Collectors.toList()).indexOf(item.lowercase())
    }
}