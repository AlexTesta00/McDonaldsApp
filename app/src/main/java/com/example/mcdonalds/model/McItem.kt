package com.example.mcdonalds.model

interface McItem {

    fun getTotalPrice() : Float
    fun getCalories() : Int
    fun getAmount() : Int
    fun getAllIngredients() : Map<Ingredient, Int>
    fun getOnlyIngredientsName() : List<String>
    fun addAmount(value : Int)
    fun deleteAmount(value : Int)
    fun deleteIngredient(ingredient: Ingredient)
    fun deleteAllIngredient(vararg ingredient: Ingredient)
    fun removeIngredient(ingredient: Ingredient, amount: Int)
    fun addIngredientAmount(ingredient: Ingredient, amount: Int)
    fun addOneUnitOfIngredient(ingredient: Ingredient)
    fun removOneUnitOfIngredient(ingredient: Ingredient)

}