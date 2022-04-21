package com.example.mcdonalds.model

import java.lang.IllegalArgumentException
import java.util.*
import java.util.stream.Collectors

class McItemImpl(
    val name : String,
    val image : Int,
    val imageDescription : String,
    private var price : Float,
    private var amount: Int = 0,
    private var ingredients: Map<Ingredient, Int>) : McItem {


    override fun getTotalPrice(): Float {
        return (this.price * getAmount())
    }

    override fun getCalories(): Int {
        return ingredients.keys.stream().mapToInt{ it -> it.calories }.sum()
    }

    override fun getAmount(): Int {
        return this.amount
    }

    override fun getAllIngredients(): Map<Ingredient, Int> {
        return Collections.unmodifiableMap(this.ingredients)
    }

    override fun getOnlyIngredientsName(): List<String> {
        return getAllIngredients().keys.stream().map { it -> it.name }.collect(Collectors.toList())
    }

    override fun addAmount(value: Int) {
        if(value <= 0){
            throw IllegalArgumentException("The value must be > 0")
        }else{
            this.amount += value
        }
    }

    override fun deleteAmount(value: Int) {
        if(getAmount() < value) this.amount = 0 else if(value <= 0) throw IllegalArgumentException("The value must be > 0") else this.amount -= value
    }

    override fun deleteIngredient(ingredient: Ingredient) {
        if(this.ingredients.containsKey(ingredient)){
            this.ingredients - ingredient
        }else{
            throw IllegalArgumentException("Map not contains ${ingredient.name}")
        }
    }

    override fun deleteAllIngredient(vararg ingredient: Ingredient) {
        ingredient.forEach { it ->
            if(this.ingredients.containsKey(it)){
                deleteIngredient(it)
            }else{
                throw IllegalArgumentException("Map not contains ${it.name}")
            }
        }
    }

    override fun removeIngredient(ingredient: Ingredient, amount: Int) {
        this.updateIngredient(ingredient)
        if(this.ingredients.containsKey(ingredient) && amount > 0){
            this.ingredients - Pair(ingredient, amount)
        }else{
            throw IllegalArgumentException()
        }
    }

    override fun addIngredientAmount(ingredient: Ingredient, amount: Int) {
        if(this.ingredients.containsKey(ingredient) && amount > 0){
            this.ingredients + Pair(ingredient, amount)
        }else{
            throw IllegalArgumentException()
        }
    }

    override fun addOneUnitOfIngredient(ingredient: Ingredient){
        addIngredientAmount(ingredient, 1)
    }

    override fun removOneUnitOfIngredient(ingredient: Ingredient) {
        removeIngredient(ingredient, 1)
    }

    private fun updateIngredient(ingredient: Ingredient){
        if(this.ingredients.getValue(ingredient) <= 0){
            this.deleteIngredient(ingredient)
        }
    }

}