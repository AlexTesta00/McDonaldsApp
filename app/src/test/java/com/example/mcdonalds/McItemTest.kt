package com.example.mcdonalds

import com.example.mcdonalds.model.Ingredient
import com.example.mcdonalds.model.McItemImpl
import org.junit.Test

import org.junit.Assert.*

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class McItemTest {

    private val bread : Ingredient = Ingredient("Pane", 200.00f, 120)
    private val chicken : Ingredient = Ingredient("Pollo", 300.00f, 200)
    private val mayonnaise : Ingredient = Ingredient("Maionese", 0.50f, 10)
    private val ketchup : Ingredient = Ingredient("ketchup", 0.50f, 10)
    private val tomato : Ingredient = Ingredient("Pomodoro", 50f, 50)
    private val salad : Ingredient = Ingredient("Insalata", 100f, 50)
    private var mcChikenIngredients = mutableMapOf<Ingredient, Int>(
        bread to 2,
        chicken to 2,
        mayonnaise to 1,
        ketchup to 1,
        tomato to 2,
        salad to 3)

    var itemMc = McItemImpl("McChiken",
        11,
        "Pollo",
        4.60f,
        1,
        mcChikenIngredients)

    @Test
    fun correct_ingredients() {
        var ing = mutableMapOf<Ingredient, Int>(
            bread to 2,
            chicken to 2,
            mayonnaise to 1,
            ketchup to 1,
            tomato to 2,
            salad to 3)

        assertEquals(ing, itemMc.getAllIngredients())
    }

    @Test
    fun correct_amount(){

    }
}