package com.example.mcdonalds.controller

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.mcdonalds.R
import com.example.mcdonalds.model.Ingredient

class IngredientAdapter(private var ingredient: List<Ingredient>, private var activity : AppCompatActivity) : RecyclerView.Adapter<IngredientAdapter.ViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): IngredientAdapter.ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.ingredient, parent, false))
    }

    override fun onBindViewHolder(holder: IngredientAdapter.ViewHolder, position: Int) {
        val currentIngredient = this.ingredient[position]

        //Recover Internal Image from name
        val resourceString = "@drawable/${currentIngredient.image}"
        val resourcesId = activity.resources.getIdentifier(resourceString, "drawable", activity.packageName)

        holder.itemImage.setImageResource(resourcesId)
        holder.itemName.text = currentIngredient.name
    }

    override fun getItemCount(): Int {
        return this.ingredient.size
    }


    inner class ViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){
        var itemImage : ImageView = itemView.findViewById(R.id.card_ingredient_image)
        var itemName : TextView = itemView.findViewById(R.id.card_ingredient_text)
    }
}