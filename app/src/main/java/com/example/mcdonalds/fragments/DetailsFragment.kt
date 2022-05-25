package com.example.mcdonalds.fragments

import android.app.Activity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.mcdonalds.R
import com.example.mcdonalds.controller.IngredientAdapter
import com.example.mcdonalds.model.*
import com.example.mcdonalds.utils.Constants
import com.example.mcdonalds.utils.FragmentUtils
import com.example.mcdonalds.utils.MessageManager
import com.google.android.material.snackbar.Snackbar

class DetailsFragment(private val mcItem: SingleMcItem) : Fragment(){

    private lateinit var mcItemImage : ImageView
    private lateinit var mcItemName : TextView
    private lateinit var mcItemIngredient: RecyclerView
    private lateinit var adapter : IngredientAdapter
    private lateinit var addQuantity : ImageView
    private lateinit var mcItemQuantity : TextView
    private lateinit var calories : TextView
    private lateinit var removeQuantity : ImageView
    private lateinit var addToCart : Button
    private var quantity : Int = 1

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if(activity != null){
            bindComponents(activity as AppCompatActivity)
            this.initDetails(activity as AppCompatActivity)
            FragmentUtils.changeAppBarName(activity as AppCompatActivity, getString(R.string.home))
            this.setAllListener()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_details, container, false)
    }


    private fun bindComponents(activity: Activity){
        this.mcItemImage = activity.findViewById(R.id.img_details)
        this.mcItemName = activity.findViewById(R.id.txt_details_name)
        this.mcItemIngredient = activity.findViewById(R.id.lst_ingredients)
        this.addQuantity = activity.findViewById(R.id.img_add_quantity)
        this.calories = activity.findViewById(R.id.txt_kcal)
        this.removeQuantity = activity.findViewById(R.id.img_remove_quantity)
        this.mcItemQuantity = activity.findViewById(R.id.txt_details_quantity)
        this.addToCart = activity.findViewById(R.id.btn_add_to_cart)
    }

    private fun initDetails(activity: AppCompatActivity) {
        val resourceString = "@drawable/${this.mcItem.getImage()}"
        val resourcesId = activity.resources.getIdentifier(resourceString, "drawable", activity.packageName)
        this.mcItemImage.setImageResource(resourcesId)
        this.mcItemImage.contentDescription = this.mcItem.getImageDesc()
        this.mcItemName.text = this.mcItem.getName()

        //init ingredients
        this.adapter = IngredientAdapter(this.mcItem.getAllIngredients(), activity)
        this.mcItemIngredient.adapter = this.adapter

        //Make swappable recycler view
        ItemTouchHelper(this.swappableRecyclerView()).attachToRecyclerView(this.mcItemIngredient)

        //init calories
        this.calories.text = this.computeCalories()

    }

    private fun swappableRecyclerView(): ItemTouchHelper.SimpleCallback {
        val simpleCallBack = object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT.or(ItemTouchHelper.DOWN)){
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return true
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition

                when(direction){
                    ItemTouchHelper.LEFT -> {
                        val item : Ingredient = mcItem.getAllIngredients()[position]
                        val itemToDelete : String = mcItem.getAllIngredients()[position].name
                        if(item.modifiable){
                            mcItem.deleteItem(itemToDelete)
                            adapter.notifyItemRemoved(position)
                            Snackbar.make(mcItemIngredient, "$itemToDelete è stato cancellato", Snackbar.LENGTH_SHORT).show()
                            calories.text = computeCalories()
                        }else{
                            MessageManager.displayNoIngredientModifiable(activity as AppCompatActivity)
                            adapter.notifyItemChanged(position)
                        }
                    }
                }
            }
        }

        return simpleCallBack
    }

    //Set listener
    private fun setAllListener(){
        //Add Quantity Item
        this.addQuantity.setOnClickListener {
            if(this.quantity < Constants.QUANTITY_LIMITER){
                this.quantity += Constants.UNIT
                this.mcItemQuantity.text = this.quantity.toString()
            }
        }

        //Remove Quantity Item
        this.removeQuantity.setOnClickListener {
            if(this.quantity > 1){
                this.quantity -= Constants.UNIT
                this.mcItemQuantity.text = this.quantity.toString()
            }
        }

        //Add item on order
        this.addToCart.setOnClickListener {
            McOrder.addItem(this.mcItem, quantity)
            Snackbar.make(mcItemIngredient, "${mcItem.getName()} è stato aggiunto al carrello", Snackbar.LENGTH_SHORT).show()

            //Return to home view
            FragmentUtils.returnToBackFragment(activity as AppCompatActivity)
        }
    }

    //Refresh Calories when item are deleted
    fun computeCalories() : String{
        return this.mcItem.getCalories().toString() + Constants.CALORIES_UNITY
    }
}