package com.example.mcdonalds.controller

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.mcdonalds.R

class ProductAdapter(private var titles : List<String>, private var images : List<Int>) : RecyclerView.Adapter<ProductAdapter.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.card_menu, parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.itemTitle.text = titles[position]
        holder.itemImage.setImageResource(images[position])

        holder.itemView.setOnClickListener{
            Log.d("position", "Io sono un ${titles[holder.layoutPosition]}")
        }
    }

    override fun getItemCount(): Int {
        return titles.size
    }


    inner class ViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){
        var itemImage: ImageView = itemView.findViewById(R.id.card_menu_image)
        var itemTitle : TextView = itemView.findViewById(R.id.card_menu_text)
    }
}