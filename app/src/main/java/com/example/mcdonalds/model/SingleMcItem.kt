package com.example.mcdonalds.model

import java.lang.IllegalStateException
import java.util.*
import java.util.stream.Collectors


class SingleMcItem (
    private val name: String,
    private val image: Int,
    private val imageDescription : String,
    private val singlePrice : Double,
    private val modifiable : Boolean,
    private val category: Category,
    private val ingredients : MutableList<Ingredient>

) : McItem{

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
        return this.singlePrice
    }

    override fun getCalories(): Int {
        return this.ingredients.stream().mapToInt{it.calories}.sum()
    }

    override fun deleteItem(item: String) {
        if(this.isModifiable() && this.containIngredient(item)){
            this.ingredients.removeAt(this.getItemIndex(item))
        }else{
            throw IllegalStateException("The item is not modifiable or the ingredient is not present")
        }
    }

    private fun containIngredient(item: String): Boolean {
        return this.ingredients.stream().map { it.name }.collect(Collectors.toList()).contains(item)
    }

    private fun getItemIndex(item: String) : Int{
        return this.ingredients.stream().map { it.name }.collect(Collectors.toList()).indexOf(item)
    }

    fun getAllIngredients() : List<Ingredient>{
        return Collections.unmodifiableList(this.ingredients)
    }

    fun getOnlyIngredientsName() : List<String>{
        return this.ingredients.stream().map { it.name }.collect(Collectors.toList())
    }

    fun isModifiable() : Boolean{
        return this.modifiable
    }

    fun getCategory() : String{
        return this.category.name
    }
}