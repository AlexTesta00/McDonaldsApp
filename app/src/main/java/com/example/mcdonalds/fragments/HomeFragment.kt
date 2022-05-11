package com.example.mcdonalds.fragments

import android.app.Activity
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.mcdonalds.R
import com.example.mcdonalds.controller.CategoryAdapter
import com.example.mcdonalds.controller.ProductAdapter
import com.example.mcdonalds.model.Category
import com.example.mcdonalds.model.Ingredient
import com.example.mcdonalds.model.McItem
import com.example.mcdonalds.model.SingleMcItem
import com.example.mcdonalds.utils.Constants
import com.example.mcdonalds.utils.FragmentUtils
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.util.ArrayList


class HomeFragment : Fragment() {

    private lateinit var productView: RecyclerView
    private lateinit var categoryView : RecyclerView
    private lateinit var productAdapter: ProductAdapter
    private lateinit var categoryAdapter: CategoryAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if(activity != null){
            this.bindComponents(activity as AppCompatActivity)
            this.getAllCategories()
            this.getItems(Constants.currentCategory)
            FragmentUtils.changeAppBarName(activity as AppCompatActivity, getString(R.string.home))
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    private fun setCategoryRecyclerView(categories : MutableList<Category>){
        categoryView.setHasFixedSize(true)
        this.categoryAdapter = CategoryAdapter(categories)
        this.categoryView.adapter = categoryAdapter
    }

    private fun setProductRecyclerView(items : MutableList<SingleMcItem>){
        this.productAdapter = ProductAdapter(items, activity as AppCompatActivity)
        this.productView.adapter = this.productAdapter
    }

    private fun bindComponents(activity: Activity){
        this.categoryView = activity.findViewById(R.id.rv_categories)
        this.productView = activity.findViewById(R.id.rv_products)
    }

    private fun getItems(selectedCategory : String) : List<McItem>{
        val db = Firebase.firestore
        val items : MutableList<SingleMcItem> = mutableListOf()
        db.collection("item")
            .get()
            .addOnSuccessListener{
                for(document in it){
                    val pseudoCategory = document["category"] as DocumentReference
                    val category = Category(pseudoCategory.id)

                    if (category.name == selectedCategory) {

                        //SingleMcItem Attribute
                        val ingredientsItem = document["ingredients"] as ArrayList<DocumentReference>
                        val ingredients : MutableList<Ingredient> = mutableListOf()
                        val name : String = document["name"] as String
                        val image : String = document["image"] as String
                        val imageDescription : String = document["imageDescription"] as String
                        val singlePrice : Double = document["singlePrice"] as Double

                        //Recover All Ingredient
                        for(ingredient in ingredientsItem){
                            //Recover Ingredient and Add on Ingredients list
                            db.document(ingredient.path)
                                .get()
                                .addOnSuccessListener{ currentItem ->
                                    val weight = (currentItem["weight"] as Long).toFloat()
                                    val name = currentItem["name"] as String
                                    val image = currentItem["image"] as String
                                    val calories = (currentItem["calories"] as Long).toInt()
                                    var modifiable = (currentItem["modifiable"] as Boolean?)
                                    if(modifiable == null){
                                        modifiable = false
                                    }
                                    ingredients.add(Ingredient(name,image,modifiable,weight,calories))
                                }
                        }

                        //Add Ingredients On List
                        items.add(SingleMcItem(name, image, imageDescription, singlePrice, category, ingredients))
                    }
                    //Ingredients
                    //Log.d("base", "$items")
                }
            }
            .addOnCompleteListener{
                this.setProductRecyclerView(items)
            }
            .addOnFailureListener{

            }
        return listOf()
    }

    private fun getAllCategories() {
        val db = Firebase.firestore
        val categories : MutableList<Category> = mutableListOf()
        db.collection("category")
            .get()
            .addOnSuccessListener {
                for (document in it){
                    categories.add(Category(document.id))
                }
            }
            .addOnFailureListener{
                Log.d("base", "Qualcosa e andato storto...", it)
            }
            .addOnCompleteListener {
                //Add item to view
                this.setCategoryRecyclerView(categories)
            }
    }
}