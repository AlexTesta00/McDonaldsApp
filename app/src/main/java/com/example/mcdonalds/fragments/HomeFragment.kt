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
import com.example.mcdonalds.utils.FragmentUtils
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase


class HomeFragment : Fragment() {

    private lateinit var productView: RecyclerView
    private lateinit var categoryView : RecyclerView
    private lateinit var productAdapter: ProductAdapter
    private lateinit var categoryAdapter: CategoryAdapter
    private lateinit var database : Firebase

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if(activity != null){
            this.bindComponents(activity as AppCompatActivity)
            this.getAllCategories()
            this.getItems()
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

    /*
    private fun getItems(category : String) : List<McItem>{
        /*
        //McChiken
        val mcChikenBread = Ingredient("Bread",R.drawable.bread,false, 200.00f, 156)
        val mcChikenChiken = Ingredient("Chiken",R.drawable.chiken,false, 200.00f, 200)
        val mcChikenSalad = Ingredient("Salad",R.drawable.salad,true, 100.00f, 100)
        val mcChikenSauls = Ingredient("McChikenSauls",R.drawable.salad,true, 0.50f, 45)
        val cheddar = Ingredient("Cheddar",R.drawable.cheddar,true, 10.00f, 35)
        val bacon = Ingredient("Bacon",R.drawable.bacon,true, 10.00f, 105)
        val cucumber = Ingredient("Cucumber",R.drawable.cucumber,true, 40.00f, 30)
        val onion = Ingredient("Onion",R.drawable.onion,true, 30.00f, 50)

        val mcChiken : McItem = SingleMcItem("McChiken",
            R.drawable.mcchiken,
            "McChikenDescription",
            4.60,
            Category("Hamburger"),
            mutableListOf(mcChikenBread, mcChikenChiken, mcChikenSalad, mcChikenSauls)
        )

        //McCrispy
        val mcCrispy : McItem = SingleMcItem("McCrispy",
            R.drawable.crispy,
            "McCrispyDescription",
            5.20,
            Category("Hamburger"),
            mutableListOf(mcChikenBread, mcChikenChiken, mcChikenSalad, mcChikenSauls, cheddar, bacon)
        )

        //BigMac
        val bigMac : McItem = SingleMcItem("BigMac",
            R.drawable.bigmac,
            "BigMacDesc",
            5.00,
            Category("Hamburger"),
            mutableListOf(mcChikenBread, mcChikenChiken, mcChikenSalad, mcChikenSauls, cheddar, onion, cucumber)
        )

        //Hamburger
        val hamburger : McItem = SingleMcItem("Hamburger",
            R.drawable.hamburger,
            "Hamburger Description",
            1.50,
            Category("Hamburger"),
            mutableListOf(mcChikenBread, mcChikenChiken, mcChikenSauls, cheddar, onion, cucumber)
        )
        */
        //FiletFish

        return listOf()//listOf(mcChiken, mcCrispy, bigMac, hamburger)
    }
    */

    private fun getItems() : List<McItem>{
        val db = Firebase.firestore
        var items : MutableList<SingleMcItem> = mutableListOf()
        db.collection("item")
            .get()
            .addOnSuccessListener{
                for(document in it){
                    val name : String = document["name"] as String
                    val image : String = document["image"] as String
                    val imageDecription : String = document["imageDescription"] as String
                    val singlePrice : Double = document["singlePrice"] as Double
                    val category = document["category"] as DocumentReference


                    //Ingredients
                    items.add(SingleMcItem(name,image,imageDecription,singlePrice,Category(category.id), mutableListOf(Ingredient("Chiken", 0, false, 20f, 200))))
                    Log.d("base", "${items}")
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
                    Log.d("base", "${document.id}")
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