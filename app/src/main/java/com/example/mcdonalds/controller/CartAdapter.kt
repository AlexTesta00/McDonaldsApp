package com.example.mcdonalds.controller

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.mcdonalds.R
import com.example.mcdonalds.model.McItem
import java.util.stream.Collectors

class CartAdapter(private val mcItem: MutableMap<McItem, Int>) : RecyclerView.Adapter<CartAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartAdapter.ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.card_cart, parent, false))
    }

    override fun onBindViewHolder(holder: CartAdapter.ViewHolder, position: Int) {
        holder.title.text = mcItem.keys.stream().map { it.getName() }.collect(Collectors.toList())[position]
        holder.ingredients.text = "Ingredienti"
        holder.image.setImageResource(mcItem.keys.stream().map { it.getImage() }.collect(Collectors.toList())[position])
    }

    override fun getItemCount(): Int {
        return mcItem.size
    }

    inner class ViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){
        val title : TextView = itemView.findViewById(R.id.txt_title_cart)
        val ingredients : TextView = itemView.findViewById(R.id.txt_ingredients_cart)
        val image : ImageView = itemView.findViewById(R.id.img_card_cart)
    }
}