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
import androidx.recyclerview.widget.RecyclerView
import com.example.mcdonalds.R
import com.example.mcdonalds.activity.MapsActivity
import com.example.mcdonalds.controller.CartAdapter
import com.example.mcdonalds.model.*
import com.example.mcdonalds.utils.FragmentUtils
import com.google.android.gms.maps.MapFragment


class CartFragment : Fragment() {

    private lateinit var cartView: RecyclerView
    private lateinit var cartAdapter: CartAdapter
    private lateinit var buttonPayment : Button

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if(activity != null){
            bindComponents(activity as AppCompatActivity)
            setCategoryRecyclerView()
            modifyPrice()
            setAllListener()
            FragmentUtils.changeAppBarName(activity as AppCompatActivity, getString(R.string.carrello))
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
        this.buttonPayment.text = "Paga " + McOrder.getTotalPrice().toString().substring(0,3) + "€"
    }

    private fun setAllListener(){
        this.buttonPayment.setOnClickListener {
            if(this.cartAdapter.itemCount > 0){
                //FragmentUtils.changeCurrentFragment(activity as AppCompatActivity, MapsFragment(), "Maps")
                startActivity(Intent(activity,MapsActivity::class.java))
                activity?.finish()
            }
        }
    }
}