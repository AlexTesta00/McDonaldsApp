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
        if(this.isModifiable() && this.containIngredient(item.lowercase())){
            this.ingredients.removeAt(this.getItemIndex(item.lowercase()))
        }else{
            throw IllegalStateException("The item is not modifiable or the ingredient is not present")
        }
    }

    private fun containIngredient(item: String): Boolean {
        return this.ingredients.stream().map { it.name.lowercase() }.collect(Collectors.toList()).contains(item.lowercase())
    }

    private fun getItemIndex(item: String) : Int{
        return this.ingredients.stream().map { it.name.lowercase() }.collect(Collectors.toList()).indexOf(item.lowercase())
    }

    fun getAllIngredients() : List<Ingredient>{
        return Collections.unmodifiableList(this.ingredients)
    }

    fun getOnlyIngredientsName() : List<String>{
        return this.ingredients.stream().map { it.name.lowercase() }.collect(Collectors.toList())
    }

    fun isModifiable() : Boolean{
        return this.modifiable
    }

    fun getCategory() : String{
        return this.category.name
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as SingleMcItem

        if (name != other.name) return false

        return true
    }

    override fun hashCode(): Int {
        return name.hashCode()
    }

    override fun toString(): String {
        return "${name.uppercase()}(image=$image, imageDescription='$imageDescription', singlePrice=$singlePrice, modifiable=$modifiable, category=$category, ingredients=$ingredients)"
    }


}