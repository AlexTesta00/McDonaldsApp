package com.example.mcdonalds.fragments

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.example.mcdonalds.R
import com.example.mcdonalds.controller.CategoryAdapter
import com.example.mcdonalds.controller.ProductAdapter
import com.example.mcdonalds.model.Category
import com.example.mcdonalds.model.Ingredient
import com.example.mcdonalds.model.SingleMcItem
import com.example.mcdonalds.utils.Constants
import com.example.mcdonalds.utils.FragmentUtils
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import pl.droidsonroids.gif.GifImageView
import java.util.stream.Collectors
import kotlin.collections.ArrayList


class HomeFragment : Fragment() {

    private lateinit var loadView : GifImageView
    private lateinit var qrButton : FloatingActionButton

    companion object{
        private lateinit var productView: RecyclerView
        private lateinit var categoryView : RecyclerView
        private lateinit var productAdapter: ProductAdapter
        private lateinit var categoryAdapter: CategoryAdapter
        private val categories : MutableList<Category> = mutableListOf() //Cache category
        private val items : MutableList<SingleMcItem> = mutableListOf() //Cache Item

        //This is use to refresh item, when the user change category
        fun refreshProductView(category: String, activity: Activity){
            productAdapter = ProductAdapter(items.stream().filter { it.getCategory() == category }.collect(Collectors.toList()), activity as AppCompatActivity)
            productView.adapter = productAdapter
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if(activity != null){

            //Bind All Components in View
            this.bindComponents(activity as AppCompatActivity)

            //Set all view listener
            this.setAllListener()

            //Set Visible Load View
            this.loadView.isVisible = true

            if(categories.isEmpty()){
                //Get All Categories
                this.getAllCategories()
            }else{
                //Not request firebase category
                this.loadView.isVisible = false
                this.setCategoryRecyclerView(categories)
            }

            if(items.isEmpty()){
                //Get All Items From Server filter by category
                this.getItems(Constants.DEFAULT_CATEGORY)
            }else{
                //Not request firebase item
                this.loadView.isVisible = false
                this.setProductRecyclerView(items)
            }

            //Change the AppBar Name
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
        categoryAdapter = CategoryAdapter(categories, activity as AppCompatActivity)
        categoryView.adapter = categoryAdapter
    }

    private fun setProductRecyclerView(items : MutableList<SingleMcItem>){
        productView.setHasFixedSize(true)
        productAdapter = ProductAdapter(items, activity as AppCompatActivity)
        productView.adapter = productAdapter
    }

    private fun bindComponents(activity: Activity){
        this.loadView = activity.findViewById(R.id.load)
        this.qrButton = activity.findViewById(R.id.fab_qr)
        categoryView = activity.findViewById(R.id.rv_categories)
        productView = activity.findViewById(R.id.rv_products)
    }


    private fun getItems(selectedCategory : String) {
        val db = Firebase.firestore
        db.collection("item")
            .get()
            .addOnSuccessListener{
                for(document in it){
                    val pseudoCategory = document["category"] as DocumentReference
                    val category = Category(pseudoCategory.id)

                    val onlyName = items.stream().map { item -> item.getName() }.collect(Collectors.toList())

                    if (!onlyName.contains(document["name"] as String)) {

                        //SingleMcItem Attribute
                        val ingredientsItem : ArrayList<DocumentReference> = document["ingredients"] as ArrayList<DocumentReference>
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
                }
            }
            .addOnCompleteListener{
                this.setProductRecyclerView(items.stream().filter{it.getCategory() == selectedCategory}.collect(Collectors.toList()))
                this.loadView.isVisible = false
                Log.d("task", "Aggiorno la view")
            }
            .addOnFailureListener{

            }
    }

    private fun getAllCategories() {
        val db = Firebase.firestore
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
                Log.d("item", "Setto le categorie")
                this.setCategoryRecyclerView(categories)
            }
    }

    override fun onResume() {
        super.onResume()
        if(activity != null){

            //Bind All Components in View
            this.bindComponents(activity as AppCompatActivity)

            //Set all view listener
            this.setAllListener()

            this.loadView.isVisible = true
            this.setCategoryRecyclerView(categories)

            if(items.isEmpty()){
                //Get All Items From Server filter by category
                this.getItems(Constants.DEFAULT_CATEGORY)
            }else{
                this.loadView.isVisible = false
                refreshProductView(Constants.DEFAULT_CATEGORY,activity as AppCompatActivity)
            }

            //Change the AppBar Name
            FragmentUtils.changeAppBarName(activity as AppCompatActivity, getString(R.string.home))
        }
    }

    private fun setAllListener(){
        this.qrButton.setOnClickListener {
            FragmentUtils.changeCurrentFragment(activity as AppCompatActivity, QrCodeScannerFragment(), "QrCodeFragment")
        }
    }
}