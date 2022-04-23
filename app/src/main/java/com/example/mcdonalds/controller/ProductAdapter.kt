package com.example.mcdonalds.controller

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.mcdonalds.R
import com.example.mcdonalds.model.McItem

class ProductAdapter(private val items : List<McItem>) : RecyclerView.Adapter<ProductAdapter.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.card_menu, parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.itemTitle.text = items[position].getName()
        holder.itemImage.setImageResource(items[position].getImage())
        holder.itemImage.contentDescription = items[position].getImageDesc()

        holder.itemView.setOnClickListener{
            Log.d("position", "Io sono un ${items[holder.layoutPosition]}")
        }
    }

    override fun getItemCount(): Int {
        return items.size
    }


    inner class ViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){
        var itemImage: ImageView = itemView.findViewById(R.id.card_menu_image)
        var itemTitle : TextView = itemView.findViewById(R.id.card_menu_text)
    }
}