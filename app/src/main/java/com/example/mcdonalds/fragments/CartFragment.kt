package com.example.mcdonalds.fragments

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.mcdonalds.R
import com.example.mcdonalds.activity.MapsActivity
import com.example.mcdonalds.controller.CartAdapter
import com.example.mcdonalds.model.*
import com.example.mcdonalds.utils.FragmentUtils
import com.google.android.material.snackbar.Snackbar
import java.util.stream.Collectors


class CartFragment : Fragment() {

    private lateinit var cartView: RecyclerView
    private lateinit var cartAdapter: CartAdapter
    private lateinit var buttonPayment : Button

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if(activity != null){
            this.bindComponents(activity as AppCompatActivity)
            this.setCategoryRecyclerView()
            this.modifyPrice()
            this.setAllListener()

            //This is used to increase UI / UX experience
            this.updateButton()

            //Make recycler view swappable
            ItemTouchHelper(this.swappableRecyclerView()).attachToRecyclerView(this.cartView)

            FragmentUtils.changeAppBarName(activity as AppCompatActivity, getString(R.string.cart))
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_cart, container, false)
    }

    private fun setCategoryRecyclerView(){
        cartView.setHasFixedSize(true)

        this.cartAdapter = CartAdapter(McOrder.getAllItems(), activity as AppCompatActivity)
        this.cartView.adapter = cartAdapter
    }

    private fun bindComponents(activity: Activity){
        this.cartView = activity.findViewById(R.id.rv_cart)
        this.buttonPayment = activity.findViewById(R.id.btn_pay)
    }

    private fun modifyPrice(){
        val price = McOrder.getTotalPrice().toFloat()
        this.buttonPayment.text = getString(R.string.pay, price.toString())
    }

    private fun setAllListener(){
        this.buttonPayment.setOnClickListener {
            if(this.cartAdapter.itemCount > 0){
                startActivity(Intent(activity,MapsActivity::class.java))
                activity?.finish()
            }
        }
    }

    private fun swappableRecyclerView() : ItemTouchHelper.SimpleCallback {
        val simpleCallBack = object :
            ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT.or(ItemTouchHelper.DOWN)) {
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
                        val itemToDelete = McOrder.getAllItems().keys.stream().collect(Collectors.toList())[position]
                        McOrder.deleteItem(itemToDelete)
                        cartAdapter.notifyItemRemoved(position)
                        Snackbar.make(cartView, "${itemToDelete.getName()} è stato cancellato", Snackbar.LENGTH_SHORT).show()
                        modifyPrice()
                        updateButton()
                    }
                }
            }
        }

        return simpleCallBack
    }

    private fun updateButton(){
        this.buttonPayment.isEnabled = this.cartAdapter.itemCount > 0
    }
}