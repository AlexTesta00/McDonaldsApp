package com.example.mcdonalds.controller

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.mcdonalds.R
import com.example.mcdonalds.model.Ingredient
import com.example.mcdonalds.utils.Constants

class IngredientAdapter(private var ingredient: List<Ingredient>) : RecyclerView.Adapter<IngredientAdapter.ViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): IngredientAdapter.ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.ingredient, parent, false))
    }

    override fun onBindViewHolder(holder: IngredientAdapter.ViewHolder, position: Int) {
        val currentIngredient = ingredient[position]
        holder.itemImage.setImageResource(currentIngredient.image)
        holder.itemName.text = currentIngredient.name
    }

    override fun getItemCount(): Int {
        return ingredient.size
    }


    inner class ViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){
        var button : CardView = itemView.findViewById(R.id.btn_ingredients)
        var itemImage : ImageView = itemView.findViewById(R.id.card_ingredient_image)
        var itemName : TextView = itemView.findViewById(R.id.card_ingredient_text)
    }
}