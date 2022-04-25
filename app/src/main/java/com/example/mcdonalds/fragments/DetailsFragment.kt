package com.example.mcdonalds.fragments

import android.app.Activity
import android.content.Context
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
import com.example.mcdonalds.model.SingleMcItem
import com.example.mcdonalds.utils.FragmentUtils
import com.google.android.material.snackbar.Snackbar
import java.util.stream.Collectors
import kotlin.system.exitProcess

class DetailsFragment(private val mcItem: SingleMcItem) : Fragment(){

    private lateinit var mcItemImage : ImageView
    private lateinit var mcItemName : TextView
    private lateinit var mcItemIngredient: RecyclerView
    private lateinit var adapter : IngredientAdapter
    private lateinit var addQuantity : ImageView
    private lateinit var mcItemQuantity : TextView
    private lateinit var removeQuantity : ImageView
    private lateinit var addToCart : Button
    private var quantity : Int = 0

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if(activity != null){
            bindComponents(activity as AppCompatActivity)
            this.initDetails(activity as AppCompatActivity)
            FragmentUtils.changeAppBarName(activity as AppCompatActivity, getString(R.string.home))
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
        this.removeQuantity = activity.findViewById(R.id.img_remove_quantity)
        this.mcItemQuantity = activity.findViewById(R.id.txt_details_quantity)
        this.addToCart = activity.findViewById(R.id.btn_add_to_cart)
    }

    private fun initDetails(activity: AppCompatActivity) {
        this.mcItemImage.setImageResource(this.mcItem.getImage())
        this.mcItemImage.contentDescription = this.mcItem.getImageDesc()
        this.mcItemName.text = this.mcItem.getName()

        //init ingredients
        this.adapter = IngredientAdapter(this.mcItem.getAllIngredients())
        this.mcItemIngredient.adapter = this.adapter

        //Make swappable recycler view
        ItemTouchHelper(swappableRecyclerView()).attachToRecyclerView(this.mcItemIngredient)

    }

    private fun swappableRecyclerView(): ItemTouchHelper.SimpleCallback {
        var simpleCallBack = object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT.or(ItemTouchHelper.DOWN)){
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return true
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                var position = viewHolder.adapterPosition

                when(direction){
                    ItemTouchHelper.LEFT -> {
                        if(mcItem.getAllIngredients().stream().count() <= 1){

                        }else{
                            var itemToDelete : String = mcItem.getAllIngredients()[position].name
                            mcItem.deleteItem(itemToDelete)
                            adapter.notifyItemRemoved(position)
                            Snackbar.make(mcItemIngredient, "$itemToDelete è stato cancellato", Snackbar.LENGTH_LONG).show()
                        }
                    }
                }
            }
        }

        return simpleCallBack
    }

    //Display Error message because all ingredients are cancel
    private fun displayErrorMessage(context: Context){
        androidx.appcompat.app.AlertDialog.Builder(context)
            .setTitle("Ingredienti")
            .setMessage("Non puoi eliminare tutti gli ingredienti")
            .setIcon(R.drawable.ic_not_signals)
            .setPositiveButton("Ho capito"){ _, _ -> }
            .setCancelable(false)
            .show()
    }

}