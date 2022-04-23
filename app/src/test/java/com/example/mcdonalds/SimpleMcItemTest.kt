package com.example.mcdonalds

import com.example.mcdonalds.model.Category
import com.example.mcdonalds.model.Ingredient
import com.example.mcdonalds.model.SingleMcItem
import com.google.gson.Gson
import org.junit.Test

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class SimpleMcItemTest {

    private val bread : Ingredient = Ingredient("Bread", 200.00f, 200)
    private val chicken : Ingredient = Ingredient("Chiken", 200.00f, 250)
    private val salad : Ingredient = Ingredient("Salad", 50.00f, 100)
    private val ketchup : Ingredient = Ingredient("ketchup", 0.50f, 10)
    private val mcChicken : SingleMcItem = SingleMcItem("McChiken", 1, "Bread whit Chiken", 4.60, true, Category("Hamburger"),
    mutableListOf(bread,chicken,salad,ketchup)
    )
    @Test
    fun serializable(){
        val serialize = Gson().toJson(mcChicken)
        print(serialize)
        val deserialize = Gson().fromJson<SingleMcItem>(serialize, SingleMcItem::class.java)
        print(deserialize.isModifiable())
    }
}