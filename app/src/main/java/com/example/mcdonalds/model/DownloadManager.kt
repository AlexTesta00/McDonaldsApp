package com.example.mcdonalds.model

import android.app.Activity
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.mcdonalds.controller.CategoryAdapter
import com.example.mcdonalds.controller.ProductAdapter
import com.example.mcdonalds.utils.MessageManager
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.*
import kotlinx.coroutines.tasks.await
import java.lang.Exception
import java.lang.IllegalStateException
import java.util.stream.Collectors


@Suppress("OPT_IN_IS_NOT_ENABLED")
@OptIn(DelicateCoroutinesApi::class)
class DownloadManager (private var itemsView: RecyclerView,
                       private var categoryView: RecyclerView,
                       private var defaultCategory : Category,
                       private var currentActivity : AppCompatActivity){

    //Primary Constructors
    init {

        //Download All Items
        this.downloadItems()

        //Download All Categories
        this.downloadCategories()

    }

    companion object{
        private var mcItems : MutableList<McItem> = mutableListOf()
        private var categories : MutableList<Category> = mutableListOf()

        fun getItemsFromName(vararg itemName : String) : MutableList<McItem>{
            val list = mutableListOf<McItem>()
            if(!itemsAreEmpty()){
                itemName.forEach { name ->
                    val correctElements = mcItems.stream().filter { it.getName() == name }.findAny()
                    if(correctElements.isPresent){
                        list.add(correctElements.get())
                    }
                }
            }else{
                throw IllegalStateException("Class DownloadManager not instantiated")
            }
            return list
        }

        private fun itemsAreEmpty() : Boolean{
            return mcItems.isEmpty()
        }

        private fun categoriesAreEmpty() : Boolean{
            return categories.isEmpty()
        }
    }

    /*This is caused because firebase return an ArrayList of element
    * but kotlin compiler know element of instance Any?*/
    @Suppress("UNCHECKED_CAST")
    private fun downloadItems() {
        if(itemsAreEmpty()){
            GlobalScope.launch(Dispatchers.IO) {
                val db = Firebase.firestore
                db.collection("item")
                    .get()
                    .addOnSuccessListener {
                        for (document in it) {
                            val pseudoCategory = document["category"] as DocumentReference
                            val category = Category(pseudoCategory.id)

                                //SingleMcItem Attribute
                                val ingredientsItem: MutableList<DocumentReference> =
                                    document["ingredients"] as MutableList<DocumentReference>
                                val ingredients: MutableList<Ingredient> = mutableListOf()
                                val name: String = document["name"] as String
                                val image: String = document["image"] as String
                                val imageDescription: String = document["imageDescription"] as String
                                val singlePrice: Double = document["singlePrice"] as Double

                                //Recover All Ingredient
                                for (ingredient in ingredientsItem) {
                                    //Recover Ingredient and Add on Ingredients list
                                    db.document(ingredient.path)
                                        .get()
                                        .addOnSuccessListener { currentItem ->
                                            val weight = (currentItem["weight"] as Long).toFloat()
                                            val nameOfItem = currentItem["name"] as String
                                            val imageOfItem = currentItem["image"] as String
                                            val calories = (currentItem["calories"] as Long).toInt()
                                            var modifiable = (currentItem["modifiable"] as Boolean?)
                                            if (modifiable == null) {
                                                modifiable = false
                                            }
                                            ingredients.add(
                                                Ingredient(
                                                    nameOfItem,
                                                    imageOfItem,
                                                    modifiable,
                                                    weight,
                                                    calories
                                                )
                                            )
                                        }
                                }
                                //Add Ingredients On List
                                mcItems.add(
                                    SingleMcItem(
                                        name,
                                        image,
                                        imageDescription,
                                        singlePrice,
                                        category,
                                        ingredients
                                    )
                                )
                        }
                    }
                    .addOnFailureListener {
                        throw Exception("Download Items Failed : $it")
                    }.await()

                withContext(Dispatchers.Main){
                    updateItems(itemsView, defaultCategory)
                }
            }
        }else{
            updateItems(itemsView, defaultCategory)
        }
    }

    private fun downloadCategories() {
        if(categoriesAreEmpty()){
            GlobalScope.launch(Dispatchers.IO) {
                val db = Firebase.firestore
                db.collection("category")
                    .get()
                    .addOnSuccessListener {
                        for (document in it){
                            categories.add(Category(document.id))
                        }
                    }
                    .addOnFailureListener{
                        throw Exception("Download Category Failed : $it")
                    }
                    .await()

                withContext(Dispatchers.Main){
                    updateCategories(categoryView)
                }
            }
        }else{
            updateCategories(categoryView)
        }
    }

    private fun getItemsByCategory(category: Category) : MutableList<McItem>{
        return mcItems.stream().filter{it.getCategory() == category.name}.collect(Collectors.toList())
    }

    private infix fun updateCategories(recyclerView: RecyclerView){
        val adapter = CategoryAdapter(categories)
        recyclerView.adapter = adapter
    }

    private fun updateItems(recyclerView: RecyclerView, category: Category){
        val adapter = ProductAdapter(this.getItemsByCategory(category),
                                    this.currentActivity)
        recyclerView.adapter = adapter
    }

    fun changeCurrentCategory(newCategory: Category){
        this.updateItems(this.itemsView, newCategory)
    }

    @Suppress("UNCHECKED_CAST")
    fun recoverHistory(idOrder : String, activity : Activity){
        val database = Firebase.database
        val reference = database.reference


        reference.addValueEventListener(object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if(snapshot.value != null){
                    val value = snapshot.value as Map<String, Map<String,List<String>>>
                    val containsKey = value.values.stream().map{it.keys}.filter{it.contains(idOrder)}.count()

                    if(containsKey > 0){
                        //Get List of Items
                        val items = value.values.stream()
                                                .map{ it[idOrder]}
                                                .collect(Collectors.toList())


                        MessageManager.displayReplaceOrderMessage(activity as AppCompatActivity,
                            idOrder,
                            *items[0]!!.toTypedArray())

                    }else{
                        MessageManager.displayNoHolderOrderPresent(activity)
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {

            }

        })
    }
}