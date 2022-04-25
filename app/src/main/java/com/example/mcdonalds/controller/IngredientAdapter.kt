package com.example.mcdonalds.controller

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
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
        holder.itemName.text = ingredient[position].name
        holder.itemDetails.text = "peso : " + ingredient[position].weight + Constants.WEIGHT_UNITY +" calorie : " + ingredient[position].calories + Constants.CALORIES_UNITY
    }

    override fun getItemCount(): Int {
        return ingredient.size
    }


    inner class ViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){
        var itemName : TextView = itemView.findViewById(R.id.txt_ingredient)
        var itemDetails : TextView = itemView.findViewById(R.id.txt_ingredient_details)
    }
}